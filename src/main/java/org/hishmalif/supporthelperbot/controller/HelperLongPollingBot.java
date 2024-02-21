package org.hishmalif.supporthelperbot.controller;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.hishmalif.supporthelperbot.controller.enums.Answers;
import org.springframework.stereotype.Component;
import org.hishmalif.supporthelperbot.controller.enums.Commands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.hishmalif.supporthelperbot.configuration.ConfigurationBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class HelperLongPollingBot extends TelegramLongPollingBot {
    private final String botName;

    public HelperLongPollingBot(ConfigurationBot configuration) {
        super(configuration.getToken());
        botName = configuration.getName();
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        String chatId = update.getMessage().getChatId().toString();

        switch (update.getMessage().getText()) {
            case Commands.START:
                sendMessage(chatId, Answers.START.getValue());
                break;
            case Commands.PARSE:
                System.out.println(2);
                break;
            case Commands.GEO:
                System.out.println(3);
                break;
            case Commands.CHAT:
                System.out.println(4);
            case Commands.ABOUT:
                sendMessage(chatId, Answers.ABOUT.getValue());
                break;
        }
        System.out.println(update.getMessage().getText());
    }

    public void sendMessage(String chatId, String value) {
        try {
            this.execute(new SendMessage(chatId, value));
        } catch (TelegramApiException e) {
            log.error("Error sending a message to chat");
        }
    }
}