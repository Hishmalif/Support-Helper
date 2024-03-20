package org.hishmalif.supporthelperbot.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class TelegramUser {
    private Long id;
    private Long telegramId;
    private String name;
    private String login;
    private Boolean active;
    private Date firstLogin;
    private String languageCode;
    private Boolean isBot;
    private Boolean isPremium;
    private Boolean isAdmin;
}