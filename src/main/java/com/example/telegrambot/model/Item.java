package com.example.telegrambot.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Item {
    String name;
    int lastBet;
    String currencyType;
    String condition;
    String imgUrl;
    int itemDeadlineAmount;
    String itemDeadlineName;
    String itemUrl;
    int peopleBetAmount;
    int authorRating;
    String authorName;
    String authorUrl;
    String authorRegisterDate;
}
