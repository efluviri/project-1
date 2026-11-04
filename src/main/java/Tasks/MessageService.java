package main.java.Tasks;

import main.java.inout.InOut;

public class MessageService {
    public void printMessage(InOut io) {
        String reminderText = "НАПОМИНАНИЕ: не забудь сегодня выучить новые слова!";
        io.print(reminderText);
    }
}
