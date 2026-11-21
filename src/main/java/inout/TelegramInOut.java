package main.java.inout;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.ArrayList;
import java.util.List;
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
    public String readline() {
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
    public void receiveCallback(String data) {
        try {
            messageQueue.put(data);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    public void printWithInlineButtons(String text, String[] buttons, String[] callbacks) {
        try {
            SendMessage msg = new SendMessage(chatId.toString(), text);
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup(new ArrayList<>());
            List<InlineKeyboardRow> rows = new ArrayList<>();
            for (int i = 0; i < buttons.length; i++) {
                InlineKeyboardButton button = new InlineKeyboardButton(buttons[i]);
                button.setCallbackData(callbacks[i]);

                InlineKeyboardRow row = new InlineKeyboardRow();
                row.add(button);
                rows.add(row);
            }
            markup.setKeyboard(rows);
            msg.setReplyMarkup(markup);
            sender.execute(msg);
        } catch (TelegramApiException e) {
              e.printStackTrace();
        }
    }


}
