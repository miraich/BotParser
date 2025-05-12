package com.example.telegrambot.config;

import lombok.Data;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@Data
@ComponentScan(basePackages = "com.example.telegrambot")
@PropertySource("classpath:application.properties")
public class BotConfig {
    private String botName = System.getenv("TG_BOT_NAME");
    private String token = System.getenv("TG_BOT_TOKEN");
}