package org.hishmalif.supporthelperbot.data.gpt;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class Message {
    @JsonProperty("role")
    private final String role;
    @JsonProperty("content")
    private final String content;

    public Message(String content) {
        this.role = "user";
        this.content = content;
    }
}