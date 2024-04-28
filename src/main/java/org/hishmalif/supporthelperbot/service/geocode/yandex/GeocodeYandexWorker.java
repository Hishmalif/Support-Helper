package org.hishmalif.supporthelperbot.service.geocode.yandex;

import feign.Param;
import feign.RequestLine;
import com.fasterxml.jackson.databind.JsonNode;

public interface GeocodeYandexWorker {
    @RequestLine("GET /1.x/?apikey={apiKey}&format=json&results=1&geocode={geocode}")
    JsonNode getGeo(@Param String apiKey, @Param String geocode);
}