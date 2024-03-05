package org.hishmalif.supporthelperbot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JsonHandlerImpl implements JsonHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
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