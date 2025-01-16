package com.example.telegrambot.service;

import com.example.telegrambot.TelegramBot;
import com.example.telegrambot.model.Categories;
import com.example.telegrambot.model.Item;
import com.example.telegrambot.model.ItemCategoryUrl;
import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class Parser {
    private final Categories categories;
    private volatile int page = 1;
    private int allPages;
    private ItemCategoryUrl itemCategoryUrl;
    private TelegramBot bot;
//    String proxyHost = "148.72.169.225";
//    int proxyPort = 10501	;
//    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));

    public Parser(Categories categories) {
        this.categories = categories;
    }

    private void parseOnePage(String userInput, Long chatId) {
        itemCategoryUrl = categories.getItemCategories().get(userInput);
        var urlToFind = String.format("https://www.limundo.com/%s/aukcije/%s?page=%d", itemCategoryUrl.getCategory(),
                itemCategoryUrl.getNumber(), page);

        var userPreferenceRating = Categories.userSettingsMap.get(chatId).getRatingPreference();
        var userPreferencePrice = Categories.userSettingsMap.get(chatId).getPricePreference();

        try {
            var docItems = Jsoup.connect(urlToFind).get();
            var itemsDivs = docItems.getElementsByClass("col-12 list-view-listing-item  ");
            var paginationDivParsed = docItems.getElementsByClass("pagination justify-content-center").getFirst();
            allPages = Integer.parseInt(paginationDivParsed.select("li").get(paginationDivParsed.childrenSize() - 2).text());

            for (Element itemDiv : itemsDivs) {
                if (!Categories.userSettingsMap.get(chatId).isItemsSending()) return;
 
                int itemAuthorRating = Integer.parseInt(itemDiv.getElementsByClass("truncate").select("span").select("span").text()
                        .replace("(", "").replace(")", "").replace(".", "")
                );

                int itemPrice = Integer.parseInt(itemDiv.select("strong").text().split(" ")[0].replace(".", ""));

                if (itemAuthorRating > userPreferenceRating || itemPrice < userPreferencePrice) {
                    continue;
                }

                var authorUrl = itemDiv.getElementsByClass("content-bottom").select("a").attr("href");
                var docAuthor = Jsoup.connect(authorUrl).get();
                var authorRegisterDate = docAuthor.getElementsByClass("media-body").select("p").get(1)
                        .text().replace("Član od", "").replace(" ", "");

                var item = new Item(
                        itemDiv.select("h3").select("a").text(),
                        itemPrice,
                        itemDiv.select("strong").text().split(" ")[1],
                        itemDiv.getElementsByClass("item-options").select("li").select("span").get(1).text(),
                        itemDiv.select("img").attr("src"),
                        Integer.parseInt(itemDiv.getElementsByClass("item-data").get(2).select("span").text()),
                        itemDiv.getElementsByClass("item-data").get(2).select("small").text(),
                        "https://www.limundo.com" + itemDiv.getElementsByClass("overlay-container overlay-visible").select("a").attr("href"),
                        Integer.parseInt(itemDiv.getElementsByClass("item-data").get(3).select("span").text().split(" ")[0]),
                        itemAuthorRating,
                        itemDiv.getElementsByClass("truncate").select("p").text(),
                        authorUrl,
                        authorRegisterDate
                );
                sendItemToUser(item, chatId);
            }
        } catch (Exception e) {
            var answer = "Сайт не отвечает, попробуйте заново.";
            var sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(chatId));
            sendMessage.setText(answer);
            bot.send(sendMessage);
            Categories.userSettingsMap.get(chatId).setItemsSending(false);
        }
    }

    public void parseAllPages(String userInput, Long chatId) {
        new Thread(() -> {
            parseOnePage(userInput, chatId);
            for (int i = 2; i <= allPages; i++) {
                if (!Categories.userSettingsMap.get(chatId).isItemsSending()) return;
                page = i;
                parseOnePage(userInput, chatId);
            }
        }).start();
    }

    private void sendItemToUser(Item item, Long chatId) {
        var answer = String.format("""
                        Название: %s
                        Последняя ставка/цена: %s %s
                        Состояние: %s
                        Товар истекает через: %d %s
                        Кол-во ставок: %d
                        Рейтинг продавца: %d
                        Имя продавца: %s
                        Ссылка на продавца: %s
                        Ссылка на товар: %s
                        Дата регистрации продавца: %s""",
                item.getName(), item.getLastBet(), item.getCurrencyType(), item.getCondition(), item.getItemDeadlineAmount(),
                item.getItemDeadlineName(), item.getPeopleBetAmount(), item.getAuthorRating(), item.getAuthorName(),
                item.getAuthorUrl(), item.getItemUrl(), item.getAuthorRegisterDate());
        var sendMessage = new SendPhoto();
        var inputFile = new InputFile(item.getImgUrl());
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setPhoto(inputFile);
        sendMessage.setCaption(answer);
        bot.send(sendMessage);
    }
}
