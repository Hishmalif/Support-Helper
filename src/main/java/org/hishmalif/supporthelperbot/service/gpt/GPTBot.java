package org.hishmalif.supporthelperbot.service.gpt;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import org.hishmalif.supporthelperbot.data.gpt.GPTRequest;

import java.util.Map;

@Slf4j
@Service(value = "gptBot")
@AllArgsConstructor
public class GPTBot {
    private final String token;
    private final String model;
    private final GPTWorker worker;

    public String getGPTResponse(String message, String uuid) {
        GPTRequest request = new GPTRequest(model, message);
        Map<String, String> headers = Map.of(
                "Content-Type", "application/json",
                "Authorization", "Bearer " + token);

        log.info("request: {} | sending message to GPT", uuid);
        JsonNode response = worker.generate(headers, request);
        log.info("request: {} | received data from GPT", uuid);
        return response.findPath("content").asText();
    }
}