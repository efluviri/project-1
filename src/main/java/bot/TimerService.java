package main.java.bot;

import main.java.Tasks.CoreUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimerService {
    private final TelegramClient telegramClient;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public TimerService(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    public void start() {
        scheduler.scheduleAtFixedRate(this::checkInactiveUsers, 1, 12, TimeUnit.HOURS);
        System.out.println("TimerService запущен (проверка раз в 12 часов)");
    }

    private void checkInactiveUsers() {
        System.out.println("[TIMER] Проверка неактивных...");
        List<Long> inactiveUsers = CoreUtils.getInactiveUsers(12);

        if (inactiveUsers.isEmpty()) {
            System.out.println("[TIMER] Все активны");
            return;
        }
        for (Long chatId : inactiveUsers) {
            try {
                SendMessage msg = new SendMessage(String.valueOf(chatId), "Напоминание: вы не проходили задания 12 часов. Вернитесь!");
                telegramClient.execute(msg);
                System.out.println("[TIMER] Отправлено: " + chatId);
            } catch (TelegramApiException e) {
                System.err.println("[TIMER] Ошибка " + chatId + ": " + e.getMessage());
            }
        }
    }

}