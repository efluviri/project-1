import main.java.bot.*;

public class Main {
    public static void main(String[] args) {

        //Bot bot = new ConsoleBot();
        Bot bot = new TelegramBot();
        bot.run();
    }
}