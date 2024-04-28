package org.hishmalif.supporthelperbot.controller;

import lombok.extern.slf4j.Slf4j;
import org.hishmalif.supporthelperbot.data.enums.Answers;
import org.hishmalif.supporthelperbot.data.enums.Commands;
import org.hishmalif.supporthelperbot.data.TelegramUser;
import org.hishmalif.supporthelperbot.data.exception.BotException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class HelperLongPollingBot extends TelegramLongPollingBot {
    private final String name;
    private final BotHandler botHandler;
    private final Map<Long, Answers> userMap; // Переделать на сессии

    public HelperLongPollingBot(String token, String name, BotHandler botHandler) {
        super(token);
        this.name = name;
        this.botHandler = botHandler;
        userMap = new HashMap<>();
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
            user = botHandler.getUser(update.getCallbackQuery().getFrom());
            message.setText(update.getCallbackQuery().getData());
        } else if (update.hasMessage()) {
            message = update.getMessage();
            user = botHandler.getUser(message.getFrom());
        } else {
            throw new BotException(); // Все равно сделать вставку user'a
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
                userMap.put(userId, Answers.GEO_WELCOME);
                log.info("UserId: " + userId + " - Send first geo message");
                sendMessage(message, Answers.GEO_WELCOME.getValue());
                break;
            case Commands.ART:
                final InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                final List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                final List<InlineKeyboardButton> rowInline = new ArrayList<>();

                WebAppInfo webAppInfo = new WebAppInfo();
                webAppInfo.setUrl("https://hishmalif.github.io/PagesForHelperBot/art/");
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText("App");
                button.setWebApp(webAppInfo);
                rowInline.add(button);
                rowsInline.add(rowInline);
                markupInline.setKeyboard(rowsInline);

                SendMessage message1 = new SendMessage(message.getChatId().toString(), "Укажите данные:");
                message1.setReplyMarkup(markupInline);
                message1.disableWebPagePreview();
                try {
                    this.execute(message1);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }

                sendMessage(message, botHandler.drawSVG(userId, message));
                break;
            case Commands.CHAT:
                userMap.put(userId, Answers.GPT_WELCOME);
                log.info("UserId: " + userId + " - Send first chatGPT message");
                sendMessage(message, Answers.GPT_WELCOME.getValue());
                break;
            case Commands.SQL:
                sendMessage(message, botHandler.buildSQL(userId, message));
                break;
            case Commands.ABOUT:
                sendMessage(message, botHandler.getAbout(userId));
                break;
            default:
                handleBotOperation(userId, message);
                break;
        }
    }

    private void handleBotOperation(Long userId, Message message) {
        final Answers reply = userMap.get(userId);
        final InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        final List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        final List<InlineKeyboardButton> rowInline = new ArrayList<>();

        if (reply == null) {
            sendMessage(message, Answers.UNKNOWN_COMMAND.getValue());
            return;
        }

        if (reply.equals(Answers.GEO_WELCOME)) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(Answers.ANSWER_YES.getValue());
            button.setCallbackData(Commands.GEO);
            rowInline.add(button);
            rowsInline.add(rowInline);
            markupInline.setKeyboard(rowsInline);

            sendMessage(message, botHandler.getLocation(userId, message.getText()));
            sendReplyMarkup(message, markupInline, Answers.REPLY.getValue());
            userMap.remove(userId);
        } else if (reply.equals(Answers.GPT_WELCOME)) {
            Message sendMessage = sendMessage(message, Answers.GPT_WAIT.getValue());
            editMessage(sendMessage, botHandler.getResponseGPT(userId, message));
        }
    }

    private Message sendMessage(Message message, String value) {
        SendMessage send = new SendMessage(message.getChatId().toString(), value);
        send.enableMarkdown(true);

        try {
            return this.execute(send);
        } catch (TelegramApiException e) {
            log.error("Error sending a message to chat", e);
            return null;
        }
    }

    private void editMessage(Message message, String value) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(message.getChatId());
        editMessage.setMessageId(message.getMessageId());
        editMessage.setText(value);
        editMessage.enableMarkdown(true);

        try {
            this.execute(editMessage);
        } catch (TelegramApiException e) {
            log.error("Error sending a message to chat", e);
        }
    }

    private void sendReplyMarkup(Message message, InlineKeyboardMarkup markupInline, String text) {
        SendMessage reply = new SendMessage(message.getChatId().toString(), text);
        reply.setReplyMarkup(markupInline);
        reply.enableMarkdown(true);

        try {
            execute(reply);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}