package main.java.handlers;
import java.util.Scanner;
public class ConsoleApp {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        TextHandler textHandler;
        System.out.println("[0.1] UPPERCASE (ВСЕ БУКВЫ ЗАГЛАВНЫЕ)");
        System.out.println("[0.2] camelCase");
        System.out.println("[1.1] Заполнение пропущенных слов в предложении");
        System.out.println("[1.2] Проверка знания слов по темам");
        System.out.println("[1.3] Уроки по темам (правило и отработка)");
        String choice = scanner.nextLine();
        switch (choice) {
            case "0.1":
                textHandler = new UpperCaseHandler();
                break;

            case "0.2":
                textHandler = new CamelCaseHandler();
                System.out.println("Выбран режим: camelCase");
                break;

            case "1.1":
                new FillBlankTask1_1().start(scanner);
                scanner.close();
                return;

            case "1.2":
                new VocabularyTask1_2().start(scanner);
                scanner.close();
                return;

            case "1.3":
                new lesson1_3().start(scanner);
                scanner.close();
                return;

            default:
                System.out.println("Ошибка: введен неверный номер. Переключено на UPPERCASE по умолчанию.");
                textHandler = new UpperCaseHandler();
                break;
        }

        System.out.println("Введите текст для обработки ");

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine();
            if ("exit".equalsIgnoreCase(line)) {
                break;
            }
            String result = textHandler.handle(line);
            System.out.println("Результат: " + result);
        }
        scanner.close();
    }
}