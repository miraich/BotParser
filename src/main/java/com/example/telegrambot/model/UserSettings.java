package com.example.telegrambot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class UserSettings {
     int ratingPreference;
     int pricePreference;
     boolean ItemsSending;
}
