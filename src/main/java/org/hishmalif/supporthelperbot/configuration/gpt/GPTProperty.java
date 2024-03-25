package org.hishmalif.supporthelperbot.configuration.gpt;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@Component
@ConfigurationProperties("gpt.bot")
public class GPTProperty {
    private String model;
    private String url;
    private String token;
}