package com.example.telegrambot;

import com.example.telegrambot.config.BotConfig;
import com.example.telegrambot.model.Categories;
import com.example.telegrambot.model.UserSettings;
import com.example.telegrambot.service.Parser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final Parser parser;

    public TelegramBot(BotConfig botConfig, Parser parser) {
        this.botConfig = botConfig;
        this.parser = parser;
        this.parser.setBot(this);
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            var messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId);
                    break;
                case "/main":
                    send(prepareButtons(chatId));
                    break;
                case "/stop":
                    stopCommandReceived(chatId);
                    break;
                default:
                    if (checkIsSending(chatId)) return;
                    try {
                        if (messageText.contains("/change_seller_rating")) { // хз как тут
                            int rating = Integer.parseInt(messageText.split(" ")[1]);
                            if (rating < 0) throw new NumberFormatException();
                            Categories.userSettingsMap.get(chatId).setRatingPreference(rating);
                        }
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        changeSellerRatingReceived(chatId);
                    }
                    try {
                        if (messageText.contains("/change_item_price")) { // и тут
                            int price = Integer.parseInt(messageText.split(" ")[1]);
                            if (price <= 0) throw new NumberFormatException();
                            Categories.userSettingsMap.get(chatId).setPricePreference(price);
                        }
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        changeItemPriceReceived(chatId);
                    }
            }
        }
        if (update.hasCallbackQuery()) {
            var data = update.getCallbackQuery().getData();
            var chatId = update.getCallbackQuery().getMessage().getChatId();
            if (checkIsSending(chatId)) return;
            Categories.userSettingsMap.get(chatId).setItemsSending(true);
            parser.parseAllPages(data, chatId);
        }
    }

    private boolean checkIsSending(Long chatId) {
        if (Categories.userSettingsMap.get(chatId).isItemsSending()) {
            var answer = "Дождитесь окончания парсинга.";
            var sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(chatId));
            sendMessage.setText(answer);
            send(sendMessage);
            return true;
        }
        return false;
    }

    private void stopCommandReceived(long chatId) {
        Categories.userSettingsMap.get(chatId).setItemsSending(false);
    }

    private void startCommandReceived(Long chatId) {
        Categories.userSettingsMap.putIfAbsent(chatId, new UserSettings(50, 10000, false));
        var answer = """
                Добро пожаловать. Используйте /main для просмотра списка доступных категорий.
                /change_seller_rating для смены фильтра рейтинга продавца, по умолчанию значение - 50.
                /change_item_price для смены фильтра цены товара, по умолчанию значение - 10000.
                """;
        var sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(answer);
        send(sendMessage);
    }

    private void changeSellerRatingReceived(Long chatId) {
        var answer = "Рейтинг вводится командой /change_seller_rating {число}";
        var sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(answer);
        send(sendMessage);
    }

    private void changeItemPriceReceived(Long chatId) {
        var answer = "Цена вводится командой /change_item_price {число}";
        var sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(answer);
        send(sendMessage);
    }

    public void send(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void send(SendPhoto message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private SendMessage prepareButtons(long chatId) {
        var message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Категории, доступные для парсинга:");
        var inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(List.of(createBtn("Аксессуары", "Аксессуары")));
        keyboard.add(List.of(createBtn("Авто/Мото запчасти", "Авто/Мото запчасти")));
        keyboard.add(List.of(createBtn("Журналы, комиксы, игры", "Журналы, комиксы, игры")));
        keyboard.add(List.of(createBtn("Филателия", "Филателия")));
        keyboard.add(List.of(createBtn("Книги", "Книги")));
        keyboard.add(List.of(createBtn("Коллекционирование", "Коллекционирование")));
        keyboard.add(List.of(createBtn("Инструменты", "Инструменты")));
        keyboard.add(List.of(createBtn("Мобильные телефоны", "Мобильные телефоны")));
        keyboard.add(List.of(createBtn("Музыка и фильмы", "Музыка и фильмы")));
        keyboard.add(List.of(createBtn("Парфюм/средства для ухода", "Парфюм/средства для ухода")));
        keyboard.add(List.of(createBtn("Нумизматика", "Нумизматика")));
        keyboard.add(List.of(createBtn("Обувь", "Обувь")));
        keyboard.add(List.of(createBtn("Одежда", "Одежда")));
        keyboard.add(List.of(createBtn("Компьютерное оборудование", "Компьютерное оборудование")));
        keyboard.add(List.of(createBtn("Спортивное оборудование", "Спортивное оборудование")));
        keyboard.add(List.of(createBtn("Техника", "Техника")));
        keyboard.add(List.of(createBtn("Искусство", "Искусство")));
        keyboard.add(List.of(createBtn("Товары для детей", "Товары для детей")));
        keyboard.add(List.of(createBtn("Товары для дома и двора", "Товары для дома и двора")));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }

    private InlineKeyboardButton createBtn(String name, String data) {
        var inline = new InlineKeyboardButton();
        inline.setText(name);
        inline.setCallbackData(data);
        return inline;
    }
}
