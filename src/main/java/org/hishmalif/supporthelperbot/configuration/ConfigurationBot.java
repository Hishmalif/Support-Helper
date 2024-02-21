package org.hishmalif.supporthelperbot.configuration;


import lombok.Getter;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.beans.factory.annotation.Value;

@Getter
@SpringBootConfiguration
public class ConfigurationBot {
    /**
     * Main settings for bot
     */
    @Value("${telegram.bot.name}")
    private String name;
    @Value("${telegram.bot.token}")
    private String token;
}