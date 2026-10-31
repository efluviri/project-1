package bot;

import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBot implements LongPollingSingleThreadUpdateConsumer {

    private static final String BOT_TOKEN = "9108924329:AAEVWx-cz07LcZM2deW6e-QcV-ImP08zbbs";
    private final TelegramClient telegramClient;

    public TelegramBot() {
        this.telegramClient = new OkHttpTelegramClient(BOT_TOKEN);
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
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