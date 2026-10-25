import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import main.java.Tasks.MessageService;
import main.java.Tasks.CoreUtils;
import main.java.Tasks.FillBlankTask;
import main.java.Tasks.RuleTask;
import main.java.Tasks.VocabularyTask;
import java.sql.SQLException;
public class ConsoleApp {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        try {
            CoreUtils.initDatabase();
        } catch (SQLException e) {
            System.err.println("[ERROR] Не удалось создать базу данных.");
            return;
        }
        final boolean[] InTask = {false};
        MessageService messageService = new MessageService();
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!InTask[0]) {
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
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    InTask[0] = true;
                    new FillBlankTask().start(scanner);
                    InTask[0] = false;
                    break;

                case "2":
                    InTask[0] = true;
                    new VocabularyTask().start(scanner);
                    InTask[0] = false;
                    break;

                case "3":
                    InTask[0] = true;
                    new RuleTask().start(scanner);
                    InTask[0] = false;
                    break;
                case "0":
                    InTask[0] = true;
                    CoreUtils.printAllTimeStats("test_user");
                    InTask[0] = false;
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
