package org.hishmalif.supporthelperbot.configuration;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.hishmalif.supporthelperbot.controller.HelperLongPollingBot;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@Configuration
@ConfigurationProperties(prefix = "telegram.bot")
public class BotConfig {
    private String name;
    private String token;

    @Bean
    public HelperLongPollingBot helperLongPollingBot() {
        return new HelperLongPollingBot(name, token);
    }
}