package com.example.telegrambot.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Data
@Component
public class Categories {
    ConcurrentHashMap<String, ItemCategoryUrl> itemCategories = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<Long, UserSettings> userSettingsMap = new ConcurrentHashMap<>();

    public Categories() {
        itemCategories.putIfAbsent("Аксессуары", new ItemCategoryUrl("Aksesoari", "g5"));
        itemCategories.putIfAbsent("Авто/Мото запчасти", new ItemCategoryUrl("Auto-i-moto", "g1"));
        itemCategories.putIfAbsent("Журналы, комиксы, игры", new ItemCategoryUrl("Casopisi-stripovi-i-igre", "g33"));
        itemCategories.putIfAbsent("Филателия", new ItemCategoryUrl("Filatelija", "g2"));
        itemCategories.putIfAbsent("Книги", new ItemCategoryUrl("Knjige", "g6"));
        itemCategories.putIfAbsent("Коллекционирование", new ItemCategoryUrl("Kolekcionarstvo", "g3"));
        itemCategories.putIfAbsent("Инструменты", new ItemCategoryUrl("Masine-i-alati", "g26"));
        itemCategories.putIfAbsent("Мобильные телефоны", new ItemCategoryUrl("Mobilni-telefoni", "g31"));
        itemCategories.putIfAbsent("Музыка и фильмы", new ItemCategoryUrl("Muzika-i-filmovi", "g35"));
        itemCategories.putIfAbsent("Парфюм/средства для ухода", new ItemCategoryUrl("Nega-tela", "g10"));
        itemCategories.putIfAbsent("Нумизматика", new ItemCategoryUrl("Numizmatika", "g27"));
        itemCategories.putIfAbsent("Обувь", new ItemCategoryUrl("Obuca", "g36"));
        itemCategories.putIfAbsent("Одежда", new ItemCategoryUrl("Odeca", "g11"));
        itemCategories.putIfAbsent("Компьютерное оборудование", new ItemCategoryUrl("Racunari-i-oprema", "g12"));
        itemCategories.putIfAbsent("Спортивное оборудование", new ItemCategoryUrl("Sportska-oprema", "g13"));
        itemCategories.putIfAbsent("Техника", new ItemCategoryUrl("Tehnika", "g14"));
        itemCategories.putIfAbsent("Искусство", new ItemCategoryUrl("Umetnost", "g21"));
        itemCategories.putIfAbsent("Товары для детей", new ItemCategoryUrl("Za-bebe-i-decu", "g29"));
        itemCategories.putIfAbsent("Товары для дома и двора", new ItemCategoryUrl("Za-kucu-i-dvoriste", "g30"));
    }
}
