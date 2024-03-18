package org.hishmalif.supporthelperbot.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

@Service
public interface JsonHandler {
    /**
     * Получение JSON ноды по определенному пути
     */
    JsonNode parseJson(String json, String path);
}