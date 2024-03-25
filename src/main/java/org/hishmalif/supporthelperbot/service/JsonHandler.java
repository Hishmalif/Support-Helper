package org.hishmalif.supporthelperbot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

@Slf4j
@Component
public class JsonHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode parseJson(String json, String path) {
        try {
            final JsonNode rootNode = objectMapper.readTree(json);
            return rootNode.at(path);
        } catch (JsonProcessingException e) {
            log.error("Возникла ошибка при парсинге JSON", e);
            throw new RuntimeException(e);
        }
    }
}