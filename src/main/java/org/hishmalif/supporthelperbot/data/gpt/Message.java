package org.hishmalif.supporthelperbot.data.gpt;

import lombok.Data;

@Data
public class Message {
    private final String role;
    private final String content;

    public Message(String content) {
        this.role = "user";
        this.content = content;
    }
}