package org.hishmalif.supporthelperbot.data.gpt;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.ArrayList;

@Data
public class GPTRequest {
    @JsonProperty("model")
    private final String model;
    @JsonProperty("messages")
    private final List<Message> messages;
    @JsonProperty("n")
    private final int n;

    public GPTRequest(String model, String message) {
        this.model = model;
        messages = new ArrayList<>();
        this.messages.add(new Message(message));
        this.n = 10;
    }
}