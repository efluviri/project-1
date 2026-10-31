import main.java.Tasks.*;

import bot.TelegramBot;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class ConsoleApp {
    public static void main(String[] args) {
        System.setProperty("https.proxyHost", "103.234.35.159");
        System.setProperty("https.proxyPort", "8080");
        String botToken = "9108924329:AAEVWx-cz07LcZM2deW6e-QcV-ImP08zbbs";
        System.out.println("Запуск бота...");

        try (TelegramBotsLongPollingApplication app = new TelegramBotsLongPollingApplication()) {
            app.registerBot(botToken, new TelegramBot());
            System.out.println("Бот успешно запущен и ждет сообщений!");
            Thread.currentThread().join();
        } catch (Exception e) {
            System.err.println("Ошибка при запуске бота:");
            e.printStackTrace();
        }
    }
}