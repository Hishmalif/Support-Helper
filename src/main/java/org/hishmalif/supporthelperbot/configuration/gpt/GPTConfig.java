package org.hishmalif.supporthelperbot.configuration.gpt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.hishmalif.supporthelperbot.service.GPTBot;

@Configuration
public class GPTConfig {
    @Bean
    public GPTBot gptBot(GPTProperty property) {
        return new GPTBot(property);
    }
}