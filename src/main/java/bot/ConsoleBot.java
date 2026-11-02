package main.java.bot;

import main.java.Tasks.*;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class ConsoleBot implements Bot {
    private volatile boolean InTask = false;
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        try {
            CoreUtils.initDatabase();
        } catch (SQLException e) {
            System.err.println("[ERROR] Не удалось создать базу данных.");
            return;
        }

        MessageService messageService = new MessageService();
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!InTask) {
                    System.out.println("\n");
                    messageService.printMessage();
                }
            }
        }, 60000, 60000);

        while (true) {
            System.out.println("[1] Заполнение пропущенных слов в предложении");
            System.out.println("[2] Проверка знания слов по темам");
            System.out.println("[3] Уроки по темам (правило и отработка)");
            System.out.println("[0] узнать статистику за всё время");
            System.out.println("[0.1] узнать статистику по темам");
            String choice = scanner.nextLine();


            switch (choice) {
                case "1":
                    InTask = true;
                    new FillBlankTask().start(scanner);
                    InTask = false;
                    break;

                case "2":
                    InTask = true;
                    new VocabularyTask().start(scanner);
                    InTask = false;
                    break;

                case "3":
                    InTask = true;
                    new RuleTask().start(scanner);
                    InTask = false;
                    break;
                case "0":
                    InTask = true;
                    CoreUtils.printAllTimeStats("test_user");
                    InTask = false;
                    break;
                case "0.1":
                    InTask = true;
                    CoreUtils.printProgress("test_user");
                    InTask = false;
                    break;

                default:
                    System.out.println("Ошибка: введен неверный номер.");
                    continue;
            }
            System.out.print("> ");
            String line = scanner.nextLine();
            if ("exit".equalsIgnoreCase(line)) {
                break;
            }
        }
        scanner.close();
    }
}
