package main.java.bot;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import java.util.ArrayList;
import java.util.List;

public class ButtonUtils {
    public static ReplyKeyboardMarkup createPersistentKeyboard(String[] texts) {
        List<KeyboardRow> rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        for (String text : texts) {
            KeyboardButton button = new KeyboardButton(text);
            row.add(button);
        }

        rows.add(row);

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(rows);
        markup.setResizeKeyboard(true);
        markup.setIsPersistent(true);
        return markup;
    }
}