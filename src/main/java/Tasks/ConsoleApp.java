package main.java.Tasks;

import java.util.Scanner;
public class ConsoleApp {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("[1] Заполнение пропущенных слов в предложении");
            System.out.println("[2] Проверка знания слов по темам");
            System.out.println("[3] Уроки по темам (правило и отработка)");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    new FillBlankTask().start(scanner);
                    return;

                case "2":
                    new VocabularyTask().start(scanner);
                    return;

                case "3":
                    new RuleTask().start(scanner);
                    return;

                default:
                    System.out.println("Ошибка: введен неверный номер.");
                    break;
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