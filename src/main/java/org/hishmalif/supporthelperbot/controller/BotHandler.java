package org.hishmalif.supporthelperbot.controller;

import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.hishmalif.supporthelperbot.data.TelegramUser;

import java.io.File;

public interface BotHandler {
    String getStart(Long id);

    String getLocation(Long id, String geo);

    String getResponseGPT(Long id, Message message);

    String drawSVG(Long id, Message message);

    String buildSQL(Long id, File file);

    String getAbout(Long id);
    TelegramUser getUser(User telegramUser);
}