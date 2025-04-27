package com.example.telegrambot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@Data
@ComponentScan(basePackages = "com.example.telegrambot")
@PropertySource("classpath:application.properties")
public class BotConfig {
    String botName = System.getenv("TG_BOT_NAME");
    String token = System.getenv("TG_BOT_TOKEN");
}