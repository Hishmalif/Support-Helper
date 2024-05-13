package org.hishmalif.supporthelperbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SupportHelperBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(SupportHelperBotApplication.class, args);
    }
}