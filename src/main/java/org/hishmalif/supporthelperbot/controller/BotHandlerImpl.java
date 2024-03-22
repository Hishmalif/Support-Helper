package org.hishmalif.supporthelperbot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.hishmalif.supporthelperbot.data.Answers;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.hishmalif.supporthelperbot.service.GeocodeYandex;
import org.hishmalif.supporthelperbot.controller.enums.Commands;

@Slf4j
@Component
public class BotHandlerImpl implements BotHandler {
    private final BotDataHandler dataHandler;
    private final GeocodeYandex geocodeYandex;

    @Autowired
    public BotHandlerImpl(BotDataHandler dataHandler, GeocodeYandex geocodeYandex) {
        this.dataHandler = dataHandler;
        this.geocodeYandex = geocodeYandex;
    }

    @Override
    public String getStart(Long id) {
        dataHandler.insertUsageOperation(id, Commands.START);
        log.info("UserId " + id + " - Send start message");
        return Answers.START.getValue();
    }

    @Override
    public String getGeo(Long id, Message message) {
        dataHandler.insertUsageOperation(id, Commands.GEO);
        log.info("UserId: " + id + " - Send message with geo data");
        return geocodeYandex.getGeo(message.getText());
    }
}