package org.hishmalif.supporthelperbot.controller;

import org.telegram.telegrambots.meta.api.objects.User;
import org.hishmalif.supporthelperbot.data.TelegramUser;

public interface BotDataHandler {
    void insertNewUser(User user);

    Boolean checkActivityUser(User user);

    TelegramUser getUser(Long id);
}