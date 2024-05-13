package org.hishmalif.supporthelperbot.service.geocode.yandex;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.fasterxml.jackson.databind.JsonNode;

@FeignClient(name = "geocodeYandexWorker", url = "${yandex.geo.url}")
public interface GeocodeYandexWorker {
    @GetMapping("/1.x/?apikey={apiKey}&format=json&results=1&geocode={geocode}")
    JsonNode getGeo(@PathVariable("apiKey") String apiKey, @PathVariable("geocode") String geocode);
}