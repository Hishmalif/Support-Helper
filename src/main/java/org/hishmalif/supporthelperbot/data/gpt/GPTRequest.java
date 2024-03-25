package org.hishmalif.supporthelperbot.data.gpt;

import lombok.Data;

import java.util.List;
import java.util.ArrayList;

@Data
public class GPTRequest {
    private final String model;
    private final List<Message> messages;
    private final int n;

    public GPTRequest(String model, String message) {
        this.model = model;
        messages = new ArrayList<>();
        this.messages.add(new Message(message));
        this.n = 1;
    }
}