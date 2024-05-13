package org.hishmalif.supporthelperbot.service.gpt;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import com.fasterxml.jackson.databind.JsonNode;
import org.hishmalif.supporthelperbot.data.gpt.GPTRequest;

import java.util.Map;

@FeignClient(name = "gptWorker", url = "${gpt.bot.url}")
public interface GPTWorker {
    @PostMapping("/v1/chat/completions")
    JsonNode generate(@RequestHeader Map<String, String> headers, @RequestBody GPTRequest request);
}