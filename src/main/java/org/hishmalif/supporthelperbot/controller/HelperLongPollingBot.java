package org.hishmalif.supporthelperbot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.facilities.filedownloader.TelegramFileDownloader;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodBoolean;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.hishmalif.supporthelperbot.data.TelegramUser;
import org.hishmalif.supporthelperbot.data.enums.Answers;
import org.hishmalif.supporthelperbot.data.enums.Commands;
import org.hishmalif.supporthelperbot.data.exception.BotException;
import org.hishmalif.supporthelperbot.configuration.bot.BotProperties;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Controller
public class HelperLongPollingBot extends TelegramLongPollingBot {
    private final String name;
    private final BotHandler botHandler;
    private final TelegramFileDownloader downloader;
    private final Map<Long, Answers> userMap; // Переделать на сессии

    public HelperLongPollingBot(BotProperties properties, BotHandler botHandler) {
        super(properties.getToken());
        this.name = properties.getName();
        this.botHandler = botHandler;
        this.downloader = new TelegramFileDownloader(properties::getToken);
        userMap = new ConcurrentHashMap<>();
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public void onUpdateReceived(Update update) {
        final Message message = getMessage(update);
        final TelegramUser user;
        final String chatId;
        final Long userId;

        if (message == null) {
            log.warn("message is null | data: {}", update);
            return;
        }
        user = botHandler.getUser(message.getFrom());
        chatId = message.getChatId().toString();
        userId = user.getId();

        if (!user.getActive()) {
            sendMessage(chatId, Answers.USER_IS_BLOCK.getValue() + user.getUnblockDate());
            log.info("userId: {} | profile is blocked", userId);
            return;
        }
        switch (message.getText()) {
            case Commands.START:
                sendMessage(chatId, botHandler.getStart(userId));
                break;
            case Commands.GEO:
                userMap.put(userId, Answers.GEO_WELCOME);
                sendMessage(chatId, Answers.GEO_WELCOME.getValue());
                log.info("userId: {} | send first geo message", userId);
                break;
            case Commands.CHAT:
                userMap.put(userId, Answers.GPT_WELCOME);
                log.info("userId: {} | send first chatGPT message", userId);
                sendMessage(chatId, Answers.GPT_WELCOME.getValue());
                break;
            case Commands.ART:
                sendMessage(chatId, Map.of("App", "Send"), "Укажите данные:", "https://hishmalif.github.io/PagesForHelperBot/art/");
                sendMessage(chatId, botHandler.drawSVG(userId, message)); // Сделать норм логику
                break;
            case Commands.SQL:
                sendMessage(chatId, botHandler.buildSQL(userId, getFile(message)));
                break;
            case Commands.ABOUT:
                sendMessage(chatId, botHandler.getAbout(userId));
                break;
            default:
                handleBotOperation(userId, message);
                break;
        }
    }

    private void handleBotOperation(Long userId, Message message) {
        final Answers reply = userMap.get(userId);
        final String chatId = message.getChatId().toString();

        if (reply == null) {
            sendMessage(chatId, Answers.UNKNOWN_COMMAND.getValue());
            return;
        }

        if (reply.equals(Answers.GEO_WELCOME)) {
            sendMessage(chatId, botHandler.getLocation(userId, message.getText()));
            sendMessage(chatId, Map.of(Answers.ANSWER_YES.getValue(), Commands.GEO), Answers.REPLY.getValue(), null);
            userMap.remove(userId);
        } else if (reply.equals(Answers.GPT_WELCOME)) {
            Message sendMessage = sendMessage(chatId, Answers.GPT_WAIT.getValue());
            changeMessage(chatId, sendMessage, botHandler.getResponseGPT(userId, message));
        }
    }

    private Message getMessage(Update update) {
        if (update.hasMessage()) {
            return update.getMessage();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage();
        } else if (update.hasEditedMessage()) {
            return update.getEditedMessage();
        } else if (update.hasChannelPost()) {
            return update.getChannelPost();
        } else if (update.hasEditedChannelPost()) {
            return update.getEditedChannelPost();
        }
        return null;
    }

    private java.io.File getFile(Message message) {
        try {
            final Document document = message.getDocument();
            final File telegramFile = (File) send(new GetFile(document.getFileId()));
            final java.io.File file = downloader.downloadFile(telegramFile);
            file.renameTo(new java.io.File(document.getFileName()));
            return file;
        } catch (TelegramApiException e) {
            log.error("error download file | message: {} | error: {} ", e.getMessage(), e.getStackTrace());
            throw new BotException(e);
        }
    }

    private void changeMessage(String chatId, Message message, String text) {
        final DeleteMessage deleteMessage = new DeleteMessage(chatId, message.getMessageId());
        send(deleteMessage);
        sendMessage(chatId, text);
    }

    private Message sendMessage(String chatId, Map<String, String> callback, String text, @Nullable String url) {
        final SendMessage message = new SendMessage(chatId, text);
        final InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        final List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        final List<InlineKeyboardButton> rowInline = new ArrayList<>();
        callback.forEach((key, value) -> {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(key);
            button.setCallbackData(value);
            rowInline.add(button);
        });

        if (url != null) {
            rowInline.get(0).setWebApp(new WebAppInfo(url));
            message.disableWebPagePreview();
        }

        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);
        message.enableMarkdown(true);
        return (Message) send(message);
    }

    private Message sendMessage(String chatId, String text) {
        final SendMessage message = new SendMessage(chatId, text);
        message.enableMarkdown(true);
        return (Message) send(message);
    }

    private <T extends BotApiMethod<?>> BotApiObject send(T message) {
        try {
            if (message instanceof BotApiMethodMessage) {
                return this.execute((BotApiMethodMessage) message);
            } else if (message instanceof BotApiMethodBoolean) {
                this.execute((BotApiMethodBoolean) message);
                return new Message();
            }
        } catch (TelegramApiException e) {
            log.error("error sending a message to chat | message: {} | error: {} ", e.getMessage(), e.getStackTrace());
            throw new BotException(e);
        }
        return null;
    }
}