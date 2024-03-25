package org.hishmalif.supporthelperbot.configuration.telegram;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@Component
@ConfigurationProperties(prefix = "telegram.bot")
public class BotProperty {
    private String name;
    private String token;
}