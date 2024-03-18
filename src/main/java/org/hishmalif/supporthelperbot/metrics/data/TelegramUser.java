package org.hishmalif.supporthelperbot.metrics.data;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class TelegramUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "telegram_id")
    private Long telegramId;
    private String name;
    private String login;
    private String region;
    @Column(columnDefinition = "is_bot")
    private Boolean isBot;
    @Column(columnDefinition = "is_premium")
    private Boolean isPremium;
}