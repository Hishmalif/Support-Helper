package org.hishmalif.supporthelperbot.controller;

import org.telegram.telegrambots.meta.api.objects.User;
import org.hishmalif.supporthelperbot.data.TelegramUser;

import java.util.Date;

public interface BotDataHandler {
    TelegramUser insertNewUser(User user);
    TelegramUser getUser(Long id);
    void insertUsageOperation(Long id, String operation);
}