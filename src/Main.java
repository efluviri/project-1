import main.java.bot.*;
public class Main {
    public static void main(String[] args){
        System.setProperty("https.proxyHost", System.getenv("PROXY_HOST"));
        System.setProperty("https.proxyPort", System.getenv("PROXY_PORT"));
        String BOT_TOKEN = System.getenv("TELEGRAM_BOT_TOKEN");
        TelegramBot bot = new TelegramBot(BOT_TOKEN);
        bot.run();
    }
}