import main.java.bot.*;

public class Main {
    public static void main(String[] args) {
<<<<<<< HEAD
        ConsoleBot bot = new ConsoleBot();
        //TelegramBot bot = new TelegramBot();
=======
       /* String envProxyHost = System.getenv("PROXY_HOST");
        String envProxyPort = System.getenv("PROXY_PORT");

        if (envProxyHost != null && !envProxyHost.isEmpty()) {
            System.setProperty("socksProxyHost", envProxyHost);
            System.out.println("Прокси хост установлен: " + envProxyHost);
        }
        if (envProxyPort != null && !envProxyPort.isEmpty()) {
            System.setProperty("socksProxyPort", envProxyPort);
            System.out.println("Прокси порт установлен: " + envProxyPort);
        }

        String botToken = System.getenv("TELEGRAM_BOT_TOKEN");
        if (botToken == null || botToken.isEmpty()) {
            System.out.println("Ошибка: не задан TELEGRAM_BOT_TOKEN!");
            return;
        }*/

        Bot bot = new ConsoleBot();
>>>>>>> 430adb94074c86d51eb024fe5d191ee0859946ba
        bot.run();
    }
}