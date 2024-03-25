package org.hishmalif.supporthelperbot.configuration.yandex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.hishmalif.supporthelperbot.service.GeocodeYandex;

@Configuration
public class YandexConfig {
    @Bean
    public GeocodeYandex geocodeYandex(YandexProperty property) {
        return new GeocodeYandex(property);
    }
}