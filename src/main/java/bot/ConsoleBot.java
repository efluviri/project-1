package main.java.bot;

import main.java.Tasks.*;
import main.java.inout.ConsoleInOnt;
import main.java.inout.InOut;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class ConsoleBot implements Bot {
    public final InOut io=new ConsoleInOnt();
    private volatile boolean InTask = false;
    @Override
    public void run() {

        try {
            CoreUtils.initDatabase(io);
        } catch (SQLException e) {
            io.print("Не удалось создать базу данных.");
            return;
        }

        MessageService messageService = new MessageService();
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!InTask) {
                    System.out.println("\n");
                    messageService.printMessage(io);
                }
            }
        }, 60000, 60000);

        while (true) {
            String command = io.readline();
            if ("exit".equalsIgnoreCase(command)) {
                break;
            }
            io.print("[1] Заполнение пропущенных слов в предложении");
            io.print("[2] Проверка знания слов по темам");
            io.print("[3] Уроки по темам (правило и отработка)");
            io.print("[0] узнать статистику за всё время");
            io.print("[0.1] узнать статистику по темам");

            switch (command) {
                case "1":
                    InTask = true;
                    new FillBlankTask().start(io);
                    InTask = false;
                    break;

                case "2":
                    InTask = true;
                    new VocabularyTask().start(io);
                    InTask = false;
                    break;

                case "3":
                    InTask = true;
                    new RuleTask().start(io);
                    InTask = false;
                    break;
                case "0":
                    InTask = true;
                    CoreUtils.printAllTimeStats("test_user",io);
                    InTask = false;
                    break;
                case "0.1":
                    InTask = true;
                    CoreUtils.printProgress("test_user",io);
                    InTask = false;

                    break;

                default:
                    io.print("Ошибка: введен неверный номер.");
                    continue;
            }
            io.print("> ");

        }
    }
}
