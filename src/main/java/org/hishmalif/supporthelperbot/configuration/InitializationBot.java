package org.hishmalif.supporthelperbot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringBootConfiguration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.hishmalif.supporthelperbot.controller.HelperLongPollingBot;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@SpringBootConfiguration
public class InitializationBot {

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