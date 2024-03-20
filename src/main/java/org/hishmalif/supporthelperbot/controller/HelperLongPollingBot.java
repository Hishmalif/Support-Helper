package org.hishmalif.supporthelperbot.controller;

import lombok.extern.slf4j.Slf4j;
import org.hishmalif.supporthelperbot.data.Answers;
import org.hishmalif.supporthelperbot.controller.enums.Commands;
import org.hishmalif.supporthelperbot.service.GeocodeYandex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class HelperLongPollingBot extends TelegramLongPollingBot {
    private final String name;
    @Autowired
    public GeocodeYandex geocodeYandex;
    @Autowired
    private BotDataHandler botDataHandler;

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
        Message telegramMessage = update.getMessage();
        String chatId = telegramMessage.getChatId().toString();
        Long userId = telegramMessage.getFrom().getId();
        botDataHandler.insertNewUser(telegramMessage.getFrom());
        Boolean active = botDataHandler.checkActivityUser(telegramMessage.getFrom());

        System.out.println(active);

        switch (update.getMessage().getText()) {
            case Commands.START:
                sendMessage(chatId, Answers.START.getValue());
                log.info("Для чата " + chatId + " запущена инструкция /start"); //TODO Скорректировать сообщение
                break;
            case Commands.PARSE:
                long id = update.getMessage().getChatId();

                InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

                List<InlineKeyboardButton> rowInline = new ArrayList<>();
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText("Нажми меня");
                button.setCallbackData("button_clicked");
                rowInline.add(button);

                rowsInline.add(rowInline);
                markupInline.setKeyboard(rowsInline);

                SendMessage message = new SendMessage();
                message.setChatId(id);
                message.setText("Привет! Нажми кнопку ниже:");
                message.setReplyMarkup(markupInline);

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                break;
            case Commands.GEO:
                sendMessage(chatId, geocodeYandex.getGeo(update.getMessage().getText()));
            case Commands.CHAT:
                System.out.println("ChatId: " + chatId);
                break;
            case Commands.ABOUT:
                sendMessage(chatId, Answers.ABOUT.getValue());
                break;
        }
        System.out.println(update.getMessage().getText());
    }

    private void getGeo(String chatId) {
        sendMessage(chatId, "\uD83C\uDF0F Введи координаты или адрес \uD83C\uDF0F");
    }

    private void sendMessage(String chatId, String value) {
        try {
            this.execute(new SendMessage(chatId, value));
        } catch (TelegramApiException e) {
            log.error("Error sending a message to chat", e);
        }
    }
}