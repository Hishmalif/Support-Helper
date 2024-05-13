package org.hishmalif.supporthelperbot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.hishmalif.supporthelperbot.service.gpt.GPTBot;
import org.hishmalif.supporthelperbot.service.gpt.GPTWorker;

@Configuration
public class GPTConfig {
    @Bean
    public GPTBot gptBot(@Value("${gpt.bot.token}") String token,
                         @Value("${gpt.bot.model}") String model,
                         GPTWorker gptWorker) {
        return new GPTBot(token, model, gptWorker);
    }
}