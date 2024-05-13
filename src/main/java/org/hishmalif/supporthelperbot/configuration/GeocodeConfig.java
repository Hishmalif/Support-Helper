package org.hishmalif.supporthelperbot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.hishmalif.supporthelperbot.service.geocode.yandex.GeocodeYandex;
import org.hishmalif.supporthelperbot.service.geocode.yandex.GeocodeYandexWorker;

@Configuration
public class GeocodeConfig {
    @Bean
    public GeocodeYandex geocodeYandex(@Value("${yandex.geo.ui}") String ui,
                                       @Value("${yandex.geo.apikey}") String apikey,
                                       GeocodeYandexWorker worker) {
        return new GeocodeYandex(ui, apikey, worker);
    }
}