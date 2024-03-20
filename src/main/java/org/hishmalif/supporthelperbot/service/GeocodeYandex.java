package org.hishmalif.supporthelperbot.service;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import org.hishmalif.supporthelperbot.data.Answers;
import org.hishmalif.supporthelperbot.data.GeoData;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.reactive.function.client.WebClient;
import org.hishmalif.supporthelperbot.data.exception.GeocodeException;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeocodeYandex {
    private final String ui;
    private final String url;
    private final String apikey;
    private final WebClient webClient; //TODO Вынести в отдельный класс с запросами
    private static final String mainPath = "/response/GeoObjectCollection/featureMember/0/GeoObject";
    private static final String pathToCoordinates = "/Point";
    private static final String pathToAddress = "/metaDataProperty/GeocoderMetaData/Address/Components";

    public String getGeo(String geo) {
        // Выполняем проверку является ли запрос - координатами
        if (geo.matches("^\\s*(\\d+([,.]\\d+)?[,.]?\\s*\\d+([,.]\\d+)?)\\s*?$")) {
            String[] position = geo.split("\\b[.,]\\s+|\\s.\\b|\\s");
            geo = position[1] + " " + position[0]; // Меняем координаты местами (Особенность Yandex API)
        }

        // Формируем ссылку запроса к API
        URI uri = UriComponentsBuilder.fromUriString(url)
                .queryParam("format", "json")
                .queryParam("apikey", apikey)
                .queryParam("geocode", UriUtils.encode(geo, StandardCharsets.UTF_8))
                .queryParam("results", "1")
                .build(true)
                .toUri();

        // Выполняем запрос
        String data = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Обрабатываем запрос
        GeoData geoData = createGeoData(data);

        // Отдаем ответ
        if (geoData == null) {
            return Answers.EMPTY_GEO.getValue(); //TODO Сделать нормальное сообщение об ошибке
        }
        return String.format(Answers.GEO.getValue(),
                geoData.getAddress(), geoData.getLatitude(), geoData.getLongitude(), geoData.getUi());
    }

    /**
     * Определение данных локации из JSON
     */
    private GeoData createGeoData(String json) {
        GeoData geo = new GeoData();
        JsonHandler jsonHandler = new JsonHandlerImpl();
        JsonNode mainNode = jsonHandler.parseJson(json, mainPath);

        if (mainNode.isEmpty()) {
            log.error("В ответе стороннего сервиса нет указанного адреса!", new GeocodeException());
            return null;
        }
        JsonNode coordinates = mainNode.at(pathToCoordinates);
        JsonNode address = mainNode.at(pathToAddress);

        if (!coordinates.isEmpty()) {
            String[] position = coordinates.get("pos").asText().split(" ");

            geo.setLongitude(formatCoordinates(position[0]));
            geo.setLatitude(formatCoordinates(position[1]));
            geo.setUi(UriComponentsBuilder.fromUriString(ui)
                    .queryParam("mode", "search")
                    .queryParam("text", UriUtils.encode(geo.getLatitude() + " " + geo.getLongitude(), StandardCharsets.UTF_8))
                    .build(true)
                    .toUriString());
        } else {
            log.error("Координаты из ответа сервиса не определены!", new GeocodeException());
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
            log.error("Адрес из ответа сервиса не определен!");
            throw new GeocodeException();
        }
        return geo;
    }

    /**
     * Обрезаем коодинаты до 4-х знаков после запятой и заменяем запятую на точку
     */
    private String formatCoordinates(String coordinates) {
        return String.format("%.4f", Double.parseDouble(coordinates)).replace(",", ".");
    }
}