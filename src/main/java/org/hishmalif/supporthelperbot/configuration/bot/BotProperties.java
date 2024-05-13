package org.hishmalif.supporthelperbot.configuration.bot;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@Component
@ConfigurationProperties(prefix = "telegram.bot")
public class BotProperties {
    private String name;
    private String token;
}