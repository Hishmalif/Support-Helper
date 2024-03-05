package org.hishmalif.supporthelperbot.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface JsonHandler {
    /**
     * Получение JSON ноды по определенному пути
     */
    JsonNode parseJson(String json, String path);
}