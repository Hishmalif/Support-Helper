package org.hishmalif.supporthelperbot.configuration;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.hishmalif.supporthelperbot.service.geocode.yandex.GeocodeYandex;
import org.hishmalif.supporthelperbot.service.geocode.yandex.GeocodeYandexWorker;

@Configuration
public class GeocodeYandexConfig {
    @Bean
    public GeocodeYandexWorker geocodeYandexWorker(@Value("${yandex.geo.url}") String url) {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(GeocodeYandexWorker.class, url);
    }

    @Bean
    public GeocodeYandex geocodeYandex(@Value("${yandex.geo.ui}") String ui,
                                       @Value("${yandex.geo.apikey}") String apikey,
                                       GeocodeYandexWorker worker) {
        return new GeocodeYandex(ui, apikey, worker);
    }
}