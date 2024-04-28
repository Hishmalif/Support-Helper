package org.hishmalif.supporthelperbot.service.gpt;

import com.fasterxml.jackson.databind.JsonNode;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.hishmalif.supporthelperbot.data.gpt.GPTRequest;

public interface GPTWorker {
    @RequestLine("POST /v1/chat/completions")
    @Headers({"Content-Type: application/json", "Authorization: Bearer {token}"})
    JsonNode generate(@Param("token") String token, GPTRequest request);
}