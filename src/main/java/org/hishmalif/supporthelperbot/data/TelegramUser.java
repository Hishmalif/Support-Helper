package org.hishmalif.supporthelperbot.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.text.SimpleDateFormat;

@Data
@NoArgsConstructor
public class TelegramUser {
    private Long id;
    private Long telegram_id;
    private String name;
    private String login;
    private Boolean active;
    private Date first_login;
    private String language_code;
    private Boolean is_bot;
    private Boolean is_premium;
    private Boolean is_admin;
    private Date unblock;

    public String getUnblockDate() {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        return format.format(unblock);
    }
}