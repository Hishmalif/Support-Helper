package org.hishmalif.supporthelperbot.service.gpt;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import org.hishmalif.supporthelperbot.data.gpt.GPTRequest;

@Slf4j
@Service(value = "gptBot")
@AllArgsConstructor
public class GPTBot {
    private final String token;
    private final String model;
    private final GPTWorker worker;

    public String getGPTResponse(String message, String uuid) {
        GPTRequest request = new GPTRequest(model, message);

        log.info("request: {} | sending message to GPT", uuid);
        JsonNode response = worker.generate(token, request);
        log.info("request: {} | received data from GPT", uuid);
        return response.findPath("content").asText();
    }
}