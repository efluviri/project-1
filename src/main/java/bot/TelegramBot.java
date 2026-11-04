package main.java.bot;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBot implements LongPollingSingleThreadUpdateConsumer, Bot {
    private final String BOT_TOKEN;
    private final TelegramClient telegramClient;

    public TelegramBot(String token) {
        this.BOT_TOKEN = token;
        this.telegramClient = new OkHttpTelegramClient(BOT_TOKEN);
    }

    @Override
    public void run() {
        System.out.println("Запуск бота...");
        try (TelegramBotsLongPollingApplication app = new TelegramBotsLongPollingApplication()) {
            app.registerBot(BOT_TOKEN, this); // <-- Передаем "this", так как мы и есть бот
            System.out.println("Бот успешно запущен и ждет сообщений!");
            Thread.currentThread().join();
        } catch (Exception e) {
            System.err.println("Ошибка при запуске бота:");
            e.printStackTrace();
        }
    }


    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();

            SendMessage message = new SendMessage(String.valueOf(chatId), "Привет! Добро пожаловать в бота для изучения английского! ");

            try {
                telegramClient.execute(message);
            } catch (TelegramApiException e) {
                System.err.println("Ошибка отправки: " + e.getMessage());
            }
        }
    }
}