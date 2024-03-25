package org.hishmalif.supporthelperbot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.hishmalif.supporthelperbot.data.HttpRequest;
import org.hishmalif.supporthelperbot.data.gpt.GPTRequest;
import org.hishmalif.supporthelperbot.repository.HttpClient;
import org.hishmalif.supporthelperbot.configuration.gpt.GPTProperty;

import java.util.Map;

@Slf4j
@Service
public class GPTBot {
    private final GPTProperty properties;
    @Autowired
    private HttpClient httpClient;
    @Autowired
    private JsonHandler jsonHandler;

    public GPTBot(GPTProperty properties) {
        this.properties = properties;
    }

    public String getGPTResponse(String message) {
        Map<String, String> headers = Map.of("Content-Type", "application/json",
                "Authorization", "Bearer " + properties.getToken());

        HttpRequest request = HttpRequest.builder()
                .url(properties.getUrl())
                .method(HttpMethod.POST)
                .headers(headers)
                .body(new GPTRequest(properties.getModel(), message))
                .build();

        return jsonHandler.parseJson(httpClient.getResponse(request), "/choices/0/message/content").asText();
    }
}