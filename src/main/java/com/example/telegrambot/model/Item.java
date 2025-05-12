package com.example.telegrambot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private final String name;
    private final int lastBet;
    private final String currencyType;
    private final String condition;
    private final String imgUrl;
    private final int itemDeadlineAmount;
    private final String itemDeadlineName;
    private final String itemUrl;
    private final int peopleBetAmount;
    private final int authorRating;
    private final String authorName;
    private final String authorUrl;
    private final String authorRegisterDate;
}
