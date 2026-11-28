package main.java.bot;

import java.util.Map;
import main.java.inout.*;
import main.java.Tasks.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBot implements LongPollingSingleThreadUpdateConsumer, Bot {

    private String BOT_TOKEN;
    private final TelegramClient telegramClient;
    private final Map<Long, TelegramInOut> activeDialogs = new ConcurrentHashMap<>();
    private final ExecutorService taskExecutor = Executors.newCachedThreadPool();

    private final TimerService timerService;

    public TelegramBot() {
        String envProxyHost = System.getenv("PROXY_HOST");
        String envProxyPort = System.getenv("PROXY_PORT");

        if (envProxyHost != null && !envProxyHost.isEmpty()) {
            System.setProperty("socksProxyHost", envProxyHost);
            System.out.println("Прокси хост установлен");
        }
        if (envProxyPort != null && !envProxyPort.isEmpty()) {
            System.setProperty("socksProxyPort", envProxyPort);
            System.out.println("Прокси порт установлен");
        }

        this.BOT_TOKEN = System.getenv("TELEGRAM_BOT_TOKEN");
        if (this.BOT_TOKEN == null || this.BOT_TOKEN.isEmpty()) {
            throw new IllegalArgumentException("Ошибка: не задан TELEGRAM_BOT_TOKEN!");
        }

        this.telegramClient = new OkHttpTelegramClient(BOT_TOKEN);

        this.timerService = new TimerService(telegramClient);

        try {
            CoreUtils.initDatabase(new TelegramInOut(0L, telegramClient));
        } catch (Exception e) {
            System.err.println("Ошибка инициализации БД: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        System.out.println("Запуск бота...");
        timerService.start();

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

    public void consume(Update update) {
        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            try {
                telegramClient.execute(new AnswerCallbackQuery(update.getCallbackQuery().getId()));
            } catch (TelegramApiException ignored) {}

            if (data.startsWith("/")) {
                TelegramInOut io = new TelegramInOut(chatId, telegramClient);
                switch (data) {
                    case "/fillblank":
                        startDialog(chatId, io, new FillBlankTask());
                        break;
                    case "/rules":
                        startDialog(chatId, io, new RuleTask());
                        break;
                    case "/vocabulary":
                        startDialog(chatId, io, new VocabularyTask());
                        break;
                    case "/guess":
                        startDialog(chatId, io, new WordGuessTask());
                        break;
                    case "/stats":
                        CoreUtils.printAllTimeStats(chatId, io);
                        io.sendBuffer();
                        io.printWithInlineButtons(
                                "Выбери задание:",
                                new String[]{"Заполнить пропуск", "Уроки по темам", "Переведи слово", "Игра: Угадай слово", "Статистика", "Прогресс"},
                                new String[]{"/fillblank", "/rules", "/vocabulary", "/guess", "/stats", "/progress"}
                        );
                        break;
                    case "/progress":
                        CoreUtils.printProgress(chatId, io);
                        io.sendBuffer();
                        io.printWithInlineButtons(
                                "Выбери задание:",
                                new String[]{"Заполнить пропуск", "Уроки по темам", "Переведи слово", "Игра: Угадай слово", "Статистика", "Прогресс"},
                                new String[]{"/fillblank", "/rules", "/vocabulary", "/guess", "/stats", "/progress"}
                        );
                        break;
                    default:
                        sendMessage(chatId, "Неизвестная команда.");
                        break;
                }
                return;
            }

            if (data.startsWith("vocab_")) {
                String topic = data.substring(6);
                TelegramInOut io = new TelegramInOut(chatId, telegramClient);
                activeDialogs.put(chatId, io);
                VocabularyTask task = new VocabularyTask();
                taskExecutor.submit(() -> {
                    try {
                        if ("food".equals(topic)) {
                            task.startTopic(io, chatId, VocabularyTask.getFoodVocab(), "food");
                        } else if ("travel".equals(topic)) {
                            task.startTopic(io, chatId, VocabularyTask.getTravelVocab(), "travel");
                        }
                    } finally {
                        activeDialogs.remove(chatId);
                    }
                });
                return;
            }
            if (data.startsWith("rule")) {
                String topic = data.substring(5);
                TelegramInOut io = new TelegramInOut(chatId, telegramClient);
                activeDialogs.put(chatId, io);
                RuleTask task = new RuleTask();
                taskExecutor.submit(() -> {
                    try {
                        task.startTopic(io, chatId, topic);
                    } finally {
                        activeDialogs.remove(chatId);
                    }
                });
                return;
            }
            if (data.startsWith("level_")) {
                int level = Integer.parseInt(data.substring(6));
                TelegramInOut gameIo = new TelegramInOut(chatId, telegramClient);
                activeDialogs.put(chatId, gameIo);

                taskExecutor.submit(() -> {
                    try {
                        WordGuessTask game = new WordGuessTask();
                        game.startGame(gameIo, chatId, level);
                    } catch (Exception e) {
                        e.printStackTrace();
                        gameIo.print("Произошла ошибка во время игры.");
                        gameIo.sendBuffer();
                    } finally {
                        activeDialogs.remove(chatId);
                    }
                });
                return;
            }

            if (activeDialogs.containsKey(chatId)) {
                InOut currentIo = activeDialogs.get(chatId);
                if (currentIo instanceof TelegramInOut) {
                    ((TelegramInOut) currentIo).receiveCallback(data);
                }
                return;
            }
        }

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
        if (!activeDialogs.containsKey(chatId)) {
            TelegramInOut io = new TelegramInOut(chatId, telegramClient);
            String normalizedText = messageText.trim().toLowerCase();
            switch (normalizedText) {
                case "/start":
                    io.printWithInlineButtons(
                            "Привет! Выбери задание:",
                            new String[]{"Заполнить пропуск", "Уроки по темам", "Переведи слово", "Игра: Угадай слово", "Статистика", "Прогресс"},
                            new String[]{"/fillblank", "/rules", "/vocabulary", "/guess", "/stats", "/progress"}
                    );
                    break;
                default:
                    sendMessage(chatId, "Неизвестная команда. Введите /start");
                    break;
            }
        }
    }

    private void startDialog(long chatId, TelegramInOut io, Task task) {
        activeDialogs.put(chatId, io);
        taskExecutor.submit(() -> {
            try {
                task.start(io, chatId);
            } finally {
                activeDialogs.remove(chatId);
            }
        });
    }
}