
import main.java.Tasks.CoreUtils;
import main.java.Tasks.FillBlankTask;
import main.java.Tasks.RuleTask;
import main.java.Tasks.VocabularyTask;
import java.util.Scanner;
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
        while (true) {
            System.out.println("[1] Заполнение пропущенных слов в предложении");
            System.out.println("[2] Проверка знания слов по темам");
            System.out.println("[3] Уроки по темам (правило и отработка)");
            System.out.println("[0] узнать статистику за всё время");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    new FillBlankTask().start(scanner);
                    break;

                case "2":
                    new VocabularyTask().start(scanner);
                    break;

                case "3":
                    new RuleTask().start(scanner);
                    break;
                case "0":
                    CoreUtils.printAllTimeStats("test_user");
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