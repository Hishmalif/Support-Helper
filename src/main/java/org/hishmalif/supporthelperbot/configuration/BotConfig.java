package org.hishmalif.supporthelperbot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.hishmalif.supporthelperbot.controller.BotHandler;
import org.hishmalif.supporthelperbot.controller.HelperLongPollingBot;

@Configuration
public class BotConfig {
    @Bean
    public HelperLongPollingBot helperLongPollingBot(@Value("${telegram.bot.token}") String token,
                                                     @Value("${telegram.bot.name}") String name,
                                                     BotHandler botHandler) {
        return new HelperLongPollingBot(token, name, botHandler);
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