package main.java.inout;

public interface InOut {
    public void print(String message);
    public String readline();
    public void sendBuffer();
    public void printWithPersistentButtons(String[] buttons);
    public void printWithInlineButtons(String text, String[] buttons, String[] callbacks);
}
