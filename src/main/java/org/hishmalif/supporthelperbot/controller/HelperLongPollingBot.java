package org.hishmalif.supporthelperbot.controller;

import lombok.extern.slf4j.Slf4j;
import org.hishmalif.supporthelperbot.data.Answers;
import org.hishmalif.supporthelperbot.controller.enums.Commands;
import org.hishmalif.supporthelperbot.data.TelegramUser;
import org.hishmalif.supporthelperbot.data.exception.BotException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class HelperLongPollingBot extends TelegramLongPollingBot {
    private final String name;
    @Autowired
    private BotHandler botHandler;
    @Autowired
    private BotDataHandler dataHandler;
    private final HashMap<Long, Answers> userMap = new HashMap<>();

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
        final Message message;
        final TelegramUser user;
        final Long userId;

        if (update.hasCallbackQuery()) {
            message = update.getCallbackQuery().getMessage();
            user = getUser(update.getCallbackQuery().getFrom());
            message.setText(update.getCallbackQuery().getData());
        } else if (update.hasMessage()) {
            message = update.getMessage();
            user = getUser(message.getFrom());
        } else {
            throw new BotException();
        }
        userId = user.getId();

        if (!user.getActive()) {
            sendMessage(message, Answers.USER_IS_BLOCK.getValue() + user.getUnblockDate());
            log.info("UserId: " + userId + " - Profile is blocked");
            return;
        }

        switch (message.getText()) {
            case Commands.START:
                sendMessage(message, botHandler.getStart(userId));
                break;
            case Commands.GEO:
                userMap.put(userId, Answers.GEO_BEFORE);
                sendMessage(message, Answers.GEO_BEFORE.getValue());
                log.info("UserId: " + userId + " - Send first geo message");
                break;
            case Commands.PARSE:
                break;
            case Commands.CHAT:
                break;
            case Commands.ABOUT:
                sendMessage(message, Answers.ABOUT.getValue());
                break;
            default:
                handleBotOperation(userId, message);
                break;
        }
    }

    private void handleBotOperation(Long userId, Message message) {
        final InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        final List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        final List<InlineKeyboardButton> rowInline = new ArrayList<>();

        if (userMap.get(userId).equals(Answers.GEO_BEFORE)) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(Answers.ANSWER_YES.getValue());
            button.setCallbackData(Commands.GEO);
            rowInline.add(button);
            rowsInline.add(rowInline);
            markupInline.setKeyboard(rowsInline);

            sendMessage(message, botHandler.getGeo(userId, message));
            sendReplyMarkup(message, markupInline, Answers.REPLY.getValue());
            userMap.remove(userId);
        }

    }

    private void sendMessage(Message message, String value) {
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), value);

        try {
            this.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error sending a message to chat", e);
        }
    }

    private void sendReplyMarkup(Message message, InlineKeyboardMarkup markupInline, String text) {
        SendMessage reply = new SendMessage(message.getChatId().toString(), text);
        reply.setReplyMarkup(markupInline);

        try {
            execute(reply);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private TelegramUser getUser(User telegramUser) {
        TelegramUser user = dataHandler.getUser(telegramUser.getId());

        if (user == null) {
            user = dataHandler.insertNewUser(telegramUser);
        }
        return user;
    }
}