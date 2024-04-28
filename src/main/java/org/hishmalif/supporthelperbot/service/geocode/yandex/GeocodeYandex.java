package org.hishmalif.supporthelperbot.service.geocode.yandex;

import lombok.SneakyThrows;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import org.hishmalif.supporthelperbot.data.GeoData;
import org.hishmalif.supporthelperbot.service.geocode.Geocode;
import org.hishmalif.supporthelperbot.data.exception.GeocodeException;

@Slf4j
@Service
@AllArgsConstructor
public class GeocodeYandex implements Geocode {
    private final String ui;
    private final String apiKey;
    private final GeocodeYandexWorker geocodeWorker;

    @Override
    public GeoData getLocation(String geo, String uuid) {
        log.info("request: {} | get user location: {} ", uuid, geo);

        // Checking whether the request is coordinates
        if (geo.matches("^\\s*(\\d+([,.]\\d+)?[,.]?\\s*\\d+([,.]\\d+)?)\\s*?$")) {
            String[] position = geo.split("\\b[.,]\\s+|\\s.\\b|\\s");
            geo = position[1] + " " + position[0];
            log.warn("request: {} | location is coordinates", uuid);
        }

        log.info("request: {} | send location to geocode service", uuid);
        JsonNode json = geocodeWorker.getGeo(apiKey, geo);
        log.info("request: {} | received data from geocode service", uuid);

        // Handle response from yandex API
        return createGeoData(json, uuid);
    }

    /**
     * Defining location data from JSON
     */
    @SneakyThrows
    private GeoData createGeoData(JsonNode json, String uuid) {
        GeoData geo = new GeoData();
        var mainNode = json.findPath("GeoObject");

        if (mainNode.isEmpty()) {
            log.error("response: {} | there is no data in the response from geo service!", uuid, new GeocodeException());
            return null;
        }

        var coordinates = mainNode.path("Point");
        var address = mainNode.findPath("Components");

        if (!coordinates.isEmpty()) {
            String[] position = coordinates.get("pos").asText().split(" ");

            geo.setLongitude(formatCoordinates(position[0]));
            geo.setLatitude(formatCoordinates(position[1]));
            String uiClient = UriComponentsBuilder.fromHttpUrl(ui)
                    .queryParam("mode", "search")
                    .queryParam("text", geo.getLatitude() + " " + geo.getLongitude())
                    .toUriString();

            geo.setUi(uiClient);
        } else {
            log.error("response: {} | coordinates from service response aren't determined!", uuid, new GeocodeException());
        }

        if (!address.isEmpty()) {
            for (var j : address) {
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
            log.error("response: {} | address from service response aren't determined!", uuid, new GeocodeException());
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