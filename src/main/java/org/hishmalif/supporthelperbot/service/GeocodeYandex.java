package org.hishmalif.supporthelperbot.service;

import lombok.extern.slf4j.Slf4j;
import org.hishmalif.supporthelperbot.repository.HttpClient;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.hishmalif.supporthelperbot.data.GeoData;
import org.hishmalif.supporthelperbot.data.HttpRequest;
import org.hishmalif.supporthelperbot.data.enums.Answers;
import org.hishmalif.supporthelperbot.data.exception.GeocodeException;
import org.hishmalif.supporthelperbot.configuration.yandex.YandexProperty;

import java.util.Map;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class GeocodeYandex {
    private final YandexProperty properties;
    @Autowired
    private HttpClient httpClient;
    @Autowired
    private JsonHandler jsonHandler;

    public GeocodeYandex(YandexProperty property) {
        this.properties = property;
    }

    public String getGeo(String geo) {
        // Checking whether the request is coordinates
        if (geo.matches("^\\s*(\\d+([,.]\\d+)?[,.]?\\s*\\d+([,.]\\d+)?)\\s*?$")) {
            String[] position = geo.split("\\b[.,]\\s+|\\s.\\b|\\s");
            geo = position[1] + " " + position[0]; // Меняем координаты местами (Особенность Yandex API)
        }

        Map<String, String> params = Map.of("format", "json",
                "apikey", properties.getApikey(),
                "geocode", UriUtils.encode(geo, StandardCharsets.UTF_8),
                "results", "1");

        HttpRequest request = HttpRequest.builder()
                .url(properties.getUrl())
                .method(HttpMethod.GET)
                .params(params)
                .build();

        // Handle response from yandex API
        GeoData geoData = createGeoData(httpClient.getResponse(request));

        // Response
        if (geoData == null) {
            return Answers.EMPTY_GEO.getValue();
        }
        return String.format(Answers.GEO.getValue(),
                geoData.getAddress(), geoData.getLatitude(), geoData.getLongitude(), geoData.getUi());
    }

    /**
     * Defining location data from JSON
     */
    private GeoData createGeoData(String json) {
        GeoData geo = new GeoData();
        JsonNode mainNode = jsonHandler.parseJson(json, "/response/GeoObjectCollection/featureMember/0/GeoObject");

        if (mainNode.isEmpty()) {
            log.error("There is no data in the response from the third-party service!", new GeocodeException());
            return null;
        }

        JsonNode coordinates = mainNode.at("/Point");
        JsonNode address = mainNode.at("/metaDataProperty/GeocoderMetaData/Address/Components");

        if (!coordinates.isEmpty()) {
            String[] position = coordinates.get("pos").asText().split(" ");

            geo.setLongitude(formatCoordinates(position[0]));
            geo.setLatitude(formatCoordinates(position[1]));
            Map<String, String> params = Map.of("mode", "search",
                    "text", UriUtils.encode(geo.getLatitude() + " " + geo.getLongitude(), StandardCharsets.UTF_8));

            HttpRequest ui = HttpRequest.builder()
                    .url(properties.getUi())
                    .params(params)
                    .build();

            geo.setUi(httpClient.getUri(ui).toString());
        } else {
            log.error("The coordinates from the service response are not determined!", new GeocodeException());
        }

        if (!address.isEmpty()) {
            for (JsonNode j : address) {
                String param = j.get("kind").asText();
                String value = j.get("name").asText();

                switch (param) {
                    case "locality":
                        geo.setLocality(value);
                        break;
                    case "street":
                        geo.setStreet(value);
                        break;
                    case "house":
                        geo.setHouse(value);
                        break;
                }
            }
        } else {
            log.error("The address from the service response is not defined!", new GeocodeException());
        }
        return geo;
    }

    /**
     * Trim to 4 decimal places and replace the comma with a dot
     */
    private String formatCoordinates(String coordinates) {
        return String.format("%.4f", Double.parseDouble(coordinates)).replace(",", ".");
    }
}