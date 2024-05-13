package org.hishmalif.supporthelperbot.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.hishmalif.supporthelperbot.data.GeoData;
import org.hishmalif.supporthelperbot.data.TelegramUser;
import org.hishmalif.supporthelperbot.data.enums.Answers;
import org.hishmalif.supporthelperbot.service.gpt.GPTBot;
import org.hishmalif.supporthelperbot.data.enums.Commands;
import org.hishmalif.supporthelperbot.service.sql.SQLService;
import org.hishmalif.supporthelperbot.service.geocode.Geocode;

import java.io.File;
import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
public class BotHandlerImpl implements BotHandler {
    private final GPTBot gpt;
    private final BotDataHandler dataHandler;
    private final Geocode geocode;
    private final SQLService sqlService;

    @Override
    public String getStart(Long id) {
        String uuid = generateUUID();
        writeRequestLog(id, uuid, Commands.START);
        writeResponseLog(id, uuid, Commands.START);
        return Answers.START.getValue();
    }

    @Override
    public String getAbout(Long id) {
        String uuid = generateUUID();
        writeRequestLog(id, uuid, Commands.ABOUT);
        writeResponseLog(id, uuid, Commands.ABOUT);
        return Answers.ABOUT.getValue();
    }

    @Override
    public String getLocation(Long userId, String geo) {
        String uuid = generateUUID();
        writeRequestLog(userId, uuid, Commands.GEO);

        GeoData data = geocode.getLocation(geo, uuid);
        writeResponseLog(userId, uuid, Commands.GEO);
        if (data == null) {
            return Answers.EMPTY_GEO.getValue();
        }
        return String.format(Answers.GEO.getValue(),
                data.getAddress(), data.getLatitude(), data.getLongitude(), data.getUi());
    }

    @Override
    public String getResponseGPT(Long id, Message message) {
        String uuid = generateUUID();
        writeResponseLog(id, generateUUID(), Commands.CHAT);
        return gpt.getGPTResponse(message.getText(), uuid);
    }

    @Override
    public String drawSVG(Long id, Message message) {
        writeResponseLog(id, generateUUID(), Commands.ART);
        return "Сервис временно не работает";
    }

    @Override
    public String buildSQL(Long id, File file) {
        writeResponseLog(id, generateUUID(), Commands.SQL);
        return sqlService.insertDatabase(file) ? "OK" : "ERROR";
    }

    @Override
    public TelegramUser getUser(User telegramUser) { // Переработать
        TelegramUser user = dataHandler.getUser(telegramUser.getId());

        if (user == null) {
            user = dataHandler.insertNewUser(telegramUser);
        }
        return user;
    }

    private void writeRequestLog(Long userId, String uuid, String type) { // Переделать на Commands
        log.info("userId: {} | request: {} | get request on operation {}", userId, uuid, type);
    }

    private void writeResponseLog(Long userId, String uuid, String type) { // Переделать на Commands
        dataHandler.insertUsageOperation(userId, type);
        log.info("userId: {} | request: {} | send to user response on operation {}", userId, uuid, type);
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }
}