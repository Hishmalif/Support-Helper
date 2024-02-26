package org.hishmalif.supporthelperbot.configuration;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@Configuration
@ConfigurationProperties("grt.bot")
public class GPTConfig {
    private String token;
}