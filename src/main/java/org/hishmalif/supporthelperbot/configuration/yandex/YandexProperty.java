package org.hishmalif.supporthelperbot.configuration.yandex;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@Component
@ConfigurationProperties(prefix = "yandex")
public class YandexProperty {
    private String url;
    private String apikey;
    private String ui;
}