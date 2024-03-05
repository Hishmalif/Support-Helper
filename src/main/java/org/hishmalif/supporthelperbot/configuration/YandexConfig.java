package org.hishmalif.supporthelperbot.configuration;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.hishmalif.supporthelperbot.service.GeocodeYandex;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.reactive.function.client.WebClient;

@Data
@Configuration
@ConfigurationProperties(prefix = "yandex")
public class YandexConfig {
    private String url;
    private String apikey;
    private String ui;

    @Bean
    public GeocodeYandex geocodeYandex() {
        return new GeocodeYandex(ui, url, apikey,  WebClient.builder().build());
    }
}