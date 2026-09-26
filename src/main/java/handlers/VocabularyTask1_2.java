package main.java.handlers;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class VocabularyTask1_2 implements Task{
    @Override
    public void start(Scanner scanner){
        System.out.println("\n=== ЗАДАНИЕ: ПЕРЕВЕДИ СЛОВО ===");
        printTopicMenu();
        String choice = scanner.nextLine().trim();
        Map<String, String> vocabulary = new HashMap<>();
        switch (choice.toLowerCase()) {
            case "1":
            case "food":
                fillFood(vocabulary);
                break;
        case "2":
        case "travel":
            fillTravel(vocabulary);
            break;
        default:
            System.out.println("Неверный ввод. Выходим из режима словаря.");
            return;
        }
        if (vocabulary.isEmpty()) return;
        int score = 0;
        int totalQuestions = vocabulary.size();
        System.out.println("\n--- НАЧАЛО ТЕСТА ---");
        for (Map.Entry<String, String> entry : vocabulary.entrySet()) {
            System.out.print("Перевод слова '" + entry.getKey() + "': ");
            String userInput = scanner.nextLine().trim().toLowerCase();
            if (userInput.equalsIgnoreCase(entry.getValue())) {
                System.out.println("Верно!");
                score++;
            }
            else {
                System.out.printf("Ошибка. Правильный ответ: %s%n%n", entry.getValue());
            }
        }
        printResult(score, totalQuestions);
    }
    private void printTopicMenu() {
        System.out.println("Выберите тему:");
        System.out.println("[1] Food (Еда)");
        System.out.println("[2] Travel (Путешествия)");
        System.out.print("Ваш выбор (номер или название): ");
    }
    private void fillFood(Map<String, String> vocab) {
        vocab.put("apple", "яблоко");
        vocab.put("bread", "хлеб");
        vocab.put("milk", "молоко");
        vocab.put("egg", "яйцо");
    }
    private void fillTravel(Map<String, String> vocab) {
        vocab.put("airport", "аэропорт");
        vocab.put("ticket", "билет");
        vocab.put("hotel", "отель");
        vocab.put("train", "поезд");
    }
    private void printResult(int score, int total) {
        double percentage = ((double) score / total) * 100;
        System.out.println("==============================");
        System.out.println("ТЕСТ ЗАВЕРШЕН");
        System.out.println("Правильных ответов: " + score + "/" + total);
        if (percentage == 100) {
            System.out.println("Идеально!");
        }
        else if (percentage > 70) {
            System.out.println("Хорошо!");
        }
        else {
            System.out.println("Нужно повторить слова.");
        }
        System.out.println("==============================\n");
    }
}


