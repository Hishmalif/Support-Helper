package org.hishmalif.supporthelperbot.repository;

import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.telegram.telegrambots.meta.api.objects.User;
import org.hishmalif.supporthelperbot.data.TelegramUser;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.hishmalif.supporthelperbot.controller.BotDataHandler;

@Repository
public class BotRepository implements BotDataHandler {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BotRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TelegramUser insertNewUser(User user) {
        String query = "with newUser as" +
                "(insert into users (telegram_id, name, login, language_code, is_bot, is_premium) " +
                "select ?, ?, ?, ?, ?, ? " +
                "where not exists (select 1 from users where telegram_id = ?) returning *) " +
                "select * from newUser"; // Какая то проблема с количеством столбцов если нет lastname

        Object[] params = {
                user.getId(),
                user.getFirstName() + (user.getLastName() != null ? " " + user.getLastName() : ""),
                user.getUserName() != null ? user.getUserName() : user.getId(),
                user.getLanguageCode() != null ? user.getLanguageCode() : "",
                user.getIsBot(),
                user.getIsPremium() != null ? user.getIsPremium() : Boolean.FALSE,
                user.getId()
        };

        return jdbcTemplate.queryForObject(query, TelegramUser.class, params);
    }

    @Override
    public TelegramUser getUser(Long telegramId) {
        return jdbcTemplate.query("with block as ( " +
                                "select b.user_id, max(b.unlock_date) unblock " +
                                "from blacklist b " +
                                "group by b.user_id) " +
                                "select u.*, " +
                                "case when u.active = false and b.unblock is null " +
                                "then now() + interval '5 year' " +
                                "else b.unblock end " +
                                "from users u " +
                                "left join block b on b.user_id = u.id " +
                                "where u.telegram_id = ?",
                        new BeanPropertyRowMapper<>(TelegramUser.class), telegramId)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public void insertUsageOperation(Long id, String operation) {
        jdbcTemplate.update("insert into user_usage(user_id, command) " +
                "values(?, ?)", id, operation);
    }
}