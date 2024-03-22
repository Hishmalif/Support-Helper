package org.hishmalif.supporthelperbot.controller;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface BotHandler {
    String getStart(Long id);

    String getGeo(Long id, Message message);
}