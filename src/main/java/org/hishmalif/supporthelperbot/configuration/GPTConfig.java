package org.hishmalif.supporthelperbot.configuration;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.hishmalif.supporthelperbot.service.gpt.GPTBot;
import org.hishmalif.supporthelperbot.service.gpt.GPTWorker;

@Configuration
public class GPTConfig {
    @Bean
    public GPTWorker gptWorker(@Value("${gpt.bot.url}") String url) {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(GPTWorker.class, url);
    }

    @Bean
    public GPTBot gptBot(@Value("${gpt.bot.token}") String token,
                         @Value("${gpt.bot.model}") String model,
                         GPTWorker gptWorker) {
        return new GPTBot(token, model, gptWorker);
    }
}