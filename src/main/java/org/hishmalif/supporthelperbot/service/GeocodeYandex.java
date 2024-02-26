package org.hishmalif.supporthelperbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class GeocodeYandex {
    private final String apikey;
    private final String url;
    private final WebClient webClient;

    public String getCoordinates(String address) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(url)
                .queryParam("format", "json")
                .queryParam("apikey", apikey)
                .queryParam("geocode", address);

        System.out.println(uriBuilder.toUriString());

        return webClient.get()
                .uri(uriBuilder.toUriString())
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}