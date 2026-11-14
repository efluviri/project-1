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
        if (buffer.length() == 0) return;

        String text = buffer.toString().trim();

        while (text.length() > 0) {
            int splitIndex = Math.min(text.length(), 4000);
            if (text.length() > 4000) {
                int lastNewline = text.lastIndexOf('\n', 4000);
                if (lastNewline != -1) {
                    splitIndex = lastNewline;
                }
            }

            String chunk = text.substring(0, splitIndex).trim();
            text = text.substring(splitIndex).trim();

            if (!chunk.isEmpty()) {
                SendMessage sendMessage = new SendMessage(chatId.toString(), chunk);
                try {
                    sender.execute(sendMessage);
                } catch (TelegramApiException e) {
                    System.err.println("Не удалось отправить сообщение в чат " + chatId);
                }
            }
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