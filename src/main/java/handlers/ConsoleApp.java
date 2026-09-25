package main.java.handlers;
import java.util.Scanner;
public class ConsoleApp {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        TextHandler textHandler;
        System.out.println("Выберите режим обработки текста:");
        System.out.println("[1] UPPERCASE (ВСЕ БУКВЫ ЗАГЛАВНЫЕ)");
        System.out.println("[2] camelCase");
        String choice = scanner.nextLine();
        if ("1".equals(choice)){
            textHandler = new UpperCaseHandler();
        }
        else if ("2".equals(choice)) {
            textHandler = new CamelCaseHandler();
            System.out.println("Выбран режим: camelCase");
        }
        else {
            System.out.println("Ошибка: введен неверный номер. Переключено на UPPERCASE по умолчанию.");
            textHandler = new UpperCaseHandler();
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