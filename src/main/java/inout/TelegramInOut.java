package main.java.inout;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramInOut implements InOut {
    private final Long chatId;
    private final TelegramClient client;
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

    public TelegramInOut(Long chatId,TelegramClient client) {
        this.chatId = chatId;
        this.client=client;
    }
    @Override
    public void print(String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(message);
        try {
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            System.err.println("Не удалось отправить сообщение в чат " + chatId);
        }
    }
    public String readline() {
        try {
            return messageQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
    public void receiveMessage(String text) {
        try {
            messageQueue.put(text);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
