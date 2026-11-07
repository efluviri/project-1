package main.java.bot;
import java.util.Map;
import main.java.inout.*;
import main.java.Tasks.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private final Map<Long, TelegramInOut> activeDialogs = new ConcurrentHashMap<>();
    private final ExecutorService taskExecutor = Executors.newCachedThreadPool();

    public TelegramBot(String token) {
        this.BOT_TOKEN = token;
        this.telegramClient = new OkHttpTelegramClient(BOT_TOKEN);
    }

    @Override
    public void run() {
        System.out.println("Запуск бота...");
        try (TelegramBotsLongPollingApplication app = new TelegramBotsLongPollingApplication()) {
            app.registerBot(BOT_TOKEN, this);
            System.out.println("Бот успешно запущен и ждет сообщений!");
            Thread.currentThread().join();
        } catch (Exception e) {
            System.err.println("Ошибка при запуске бота:");
            e.printStackTrace();
        }
    }
    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage(String.valueOf(chatId), text);
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            System.err.println("Ошибка отправки: " + e.getMessage());
        }
    }

    @Override
    public void consume(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();
        if (activeDialogs.containsKey(chatId)) {
            InOut currentIo = activeDialogs.get(chatId);
            if (currentIo instanceof TelegramInOut) {
                ((TelegramInOut) currentIo).receiveMessage(messageText);
            }
            return;
        }
        InOut io = new TelegramInOut(chatId, telegramClient);
        switch (messageText.toLowerCase()) {
            case "/start":
                sendMessage(chatId, "Привет! Добро пожаловать в бота для изучения английского!");
                break;
            case "/progress":
                CoreUtils.printProgress(chatId, io);
                break;
            case "/alltime":
                CoreUtils.printAllTimeStats(chatId, io);
                break;
            case "/fillblank":
                startDialog(chatId, (TelegramInOut) io, new FillBlankTask());
                break;
            case "/rules":
                startDialog(chatId, (TelegramInOut) io, new RuleTask());
                break;
            case "/vocabulary":
                startDialog(chatId, (TelegramInOut) io, new VocabularyTask());
                break;
            case "/guess":
                startDialog(chatId, (TelegramInOut) io, new WordGuessTask());
                break;

            default:
                sendMessage(chatId, "Неизвестная команда. Введите /start");
                break;
        }
    }
    private void  startDialog(long chatId, TelegramInOut io, Task task) {
        activeDialogs.put(chatId, io);
        taskExecutor.submit(() -> {
            try {
                task.start(io,chatId);
            } finally {
                activeDialogs.remove(chatId);
            }
        });
    }
}