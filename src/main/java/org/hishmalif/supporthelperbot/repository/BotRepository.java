package org.hishmalif.supporthelperbot.repository;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.telegram.telegrambots.meta.api.objects.User;
import org.hishmalif.supporthelperbot.data.TelegramUser;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.hishmalif.supporthelperbot.controller.BotDataHandler;

import java.util.NoSuchElementException;

@Repository
public class BotRepository implements BotDataHandler {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BotRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertNewUser(User user) {
        String query = "insert into users (telegram_id, name, login, language_code, is_bot, is_premium) " +
                "select ?, ?, ?, ?, ?, ? " +
                "where not exists (select 1 from users where telegram_id = ?)";

        Object[] params = {
                user.getId(),
                user.getFirstName() + (user.getLastName() != null ? " " + user.getLastName() : ""),
                user.getUserName() != null ? user.getUserName() : user.getId(),
                user.getLanguageCode() != null ? user.getLanguageCode() : "",
                user.getIsBot(),
                user.getIsPremium() != null ? user.getIsPremium() : Boolean.FALSE,
                user.getId()
        };

        jdbcTemplate.update(query, params);
    }

    @Override
    public Boolean checkActivityUser(User user) {
        return jdbcTemplate.query("select active from users where telegram_id = ?",
                        (resultSet, i) -> resultSet.getBoolean("active"), user.getId())
                .stream()
                .findFirst()
                .orElse(Boolean.FALSE);
    }

    @Override
    public TelegramUser getUser(Long telegramId) {
        return jdbcTemplate.query("select * from users where telegram_id = ?",
                        new BeanPropertyRowMapper<>(TelegramUser.class), telegramId)
                .stream()
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }
}