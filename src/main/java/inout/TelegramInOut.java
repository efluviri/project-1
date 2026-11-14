package main.java.inout;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramInOut implements InOut {
    private final Long chatId;
    private final TelegramClient sender;
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    private final StringBuilder buffer = new StringBuilder();

    public TelegramInOut(Long chatId, TelegramClient sender) {
        this.chatId = chatId;
        this.sender = sender;
    }

    @Override
    public void print(String message) {
        buffer.append(message).append("\n");
    }

    @Override
    public String readline() {
        sendBuffer();

        try {
            return messageQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public void sendBuffer() {
        if (buffer.length() > 0) {
            String text = buffer.toString().trim();
            SendMessage sendMessage = new SendMessage(chatId.toString(), text);
            try {
                sender.execute(sendMessage);
            } catch (TelegramApiException e) {
                System.err.println("Не удалось отправить сообщение в чат " + chatId);
            }
            buffer.setLength(0);
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