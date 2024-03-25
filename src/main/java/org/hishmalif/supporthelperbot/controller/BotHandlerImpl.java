package org.hishmalif.supporthelperbot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.hishmalif.supporthelperbot.service.GPTBot;
import org.hishmalif.supporthelperbot.data.enums.Answers;
import org.hishmalif.supporthelperbot.data.enums.Commands;
import org.hishmalif.supporthelperbot.service.GeocodeYandex;

@Slf4j
@Component
public class BotHandlerImpl implements BotHandler {
    private final GPTBot gpt;
    private final BotDataHandler dataHandler;
    private final GeocodeYandex geocodeYandex;

    @Autowired
    public BotHandlerImpl(GPTBot gptBot, BotDataHandler dataHandler, GeocodeYandex geocodeYandex) {
        this.gpt = gptBot;
        this.dataHandler = dataHandler;
        this.geocodeYandex = geocodeYandex;
    }

    @Override
    public String getStart(Long id) {
        writeLog(id, Commands.START);
        return Answers.START.getValue();
    }

    @Override
    public String getAbout(Long id) {
        writeLog(id, Commands.ABOUT);
        return Answers.ABOUT.getValue();
    }

    @Override
    public String getGeo(Long id, Message message) {
        writeLog(id, Commands.GEO);
        return geocodeYandex.getGeo(message.getText());
    }

    @Override
    public String getResponseGPT(Long id, Message message) {
        writeLog(id, Commands.CHAT);
        return gpt.getGPTResponse(message.getText());
    }

    @Override
    public String parseObject(Long id, Message message) {
        writeLog(id, Commands.PARSE);
        return "Сервис временно не работает";
    }

    private void writeLog(Long id, String type) {
        dataHandler.insertUsageOperation(id, type);
        log.info("UserId: " + id + " - Send response from " + type);
    }
}