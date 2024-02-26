package org.hishmalif.supporthelperbot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hishmalif.supporthelperbot.controller.enums.Answers;
import org.hishmalif.supporthelperbot.controller.enums.Commands;
import org.hishmalif.supporthelperbot.service.GeocodeYandex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class HelperLongPollingBot extends TelegramLongPollingBot implements HelperBot {
    private final String name;
    @Autowired
    public GeocodeYandex geocodeYandex;

    @Autowired
    public HelperLongPollingBot(String name, String token) {
        super(token);
        this.name = name;
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public void onUpdateReceived(Update update) {
        String chatId = update.getMessage().getChatId().toString();


        switch (update.getMessage().getText()) {
            case Commands.START:
                sendMessage(chatId, Answers.START.getValue());
                break;
            case Commands.PARSE:
                sendMessage(chatId, geocodeYandex.getCoordinates(update.getMessage().getText()));
                break;
            case Commands.GEO:
                System.out.println(3);
                break;
            case Commands.CHAT:
                System.out.println(4);
            case Commands.ABOUT:
                sendMessage(chatId, Answers.ABOUT.getValue());
                break;
            default:
                sendMessage(chatId, geocodeYandex.getCoordinates(update.getMessage().getText()));
                break;
        }
        System.out.println(update.getMessage().getText());
    }

    private void sendMessage(String chatId, String value) {
        try {
            this.execute(new SendMessage(chatId, value));
        } catch (TelegramApiException e) {
            log.error("Error sending a message to chat", e);
        }
    }
}