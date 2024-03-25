package org.hishmalif.supporthelperbot.configuration.telegram;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.hishmalif.supporthelperbot.controller.HelperLongPollingBot;

@Configuration
public class BotConfig {
    @Bean
    public HelperLongPollingBot helperLongPollingBot(BotProperty botProperty) {
        return new HelperLongPollingBot(botProperty);
    }

    /**
     * Register my bot in Telegram Bots API
     */
    @Bean
    public TelegramBotsApi telegramBotsApi(HelperLongPollingBot helperBot) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(helperBot);
        return api;
    }
}