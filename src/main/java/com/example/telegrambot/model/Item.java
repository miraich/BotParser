package com.example.telegrambot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

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

    public Item(String name, int lastBet, String currencyType, String condition, String imgUrl,
                int itemDeadlineAmount, String itemDeadlineName, String itemUrl, int peopleBetAmount,
                int authorRating, String authorName, String authorUrl, String authorRegisterDate) {
        this.name = name;
        this.lastBet = lastBet;
        this.currencyType = currencyType;
        this.condition = condition;
        this.imgUrl = imgUrl;
        this.itemDeadlineAmount = itemDeadlineAmount;
        this.itemDeadlineName = itemDeadlineName;
        this.itemUrl = itemUrl;
        this.peopleBetAmount = peopleBetAmount;
        this.authorRating = authorRating;
        this.authorName = authorName;
        this.authorUrl = authorUrl;
        this.authorRegisterDate = authorRegisterDate;
    }

    public Item() {

    }
}
