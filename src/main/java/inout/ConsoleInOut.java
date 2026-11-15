package main.java.inout;
import java.util.Scanner;

public class ConsoleInOut implements InOut {
    private final Scanner scanner = new Scanner(System.in);
    @Override
    public void print(String message){
        System.out.println(message);
    }
    public String readline(){
        return scanner.nextLine().trim();
    }
    public void sendBuffer() {}
    public void printWithPersistentButtons(String[] buttons) {
        print("\n ВЫБЕРИ ВАРИАНТ ");
        for (int i = 0; i < buttons.length; i++) {
            print("[" + (i + 1) + "] " + buttons[i]);
        }
    }
    public void printWithInlineButtons(String text, String[] buttons, String[] callbacks) {
        print(text);
        printWithPersistentButtons(buttons);
    }
}
