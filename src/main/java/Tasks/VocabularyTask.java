package main.java.Tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class VocabularyTask implements Task {
    private static final Map<String, String> FOOD_MAP = createFoodVocab();
    private static final Map<String, String> TRAVEL_MAP = createTravelVocab();

    @Override
    public void start(Scanner scanner){
        System.out.println("\n=== ЗАДАНИЕ: ПЕРЕВЕДИ СЛОВО ===");
        printTopicMenu();
        String choice = scanner.nextLine().trim();
        Map<String, String> vocabulary;
        switch (choice.toLowerCase()) {
            case "1","food":
                vocabulary=FOOD_MAP;
                break;
        case "2","travel":
            vocabulary=TRAVEL_MAP;
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
            System.out.print("Перевод слова '" + entry.getValue() + "': ");
            String userInput = scanner.nextLine().trim().toLowerCase();
            if (userInput.equalsIgnoreCase(entry.getKey())) {
                System.out.println("Верно!");
                score++;
            }
            else {
                System.out.printf("Ошибка. Правильный ответ: %s%n%n", entry.getValue());
            }
        }
        CoreUtils.printResult(score, totalQuestions);
    }
    private void printTopicMenu() {
        System.out.println("Выберите тему:");
        System.out.println("[1] Food (Еда)");
        System.out.println("[2] Travel (Путешествия)");
        System.out.print("Ваш выбор (номер или название): ");
    }
    private static Map<String, String> createFoodVocab() {
        Map<String, String> vocab = new HashMap<>();
        vocab.put("apple", "яблоко");
        vocab.put("bread", "хлеб");
        vocab.put("milk", "молоко");
        vocab.put("egg", "яйцо");
        return vocab;
    }
    private static Map<String, String> createTravelVocab() {
        Map<String, String> vocab = new HashMap<>();
        vocab.put("airport", "аэропорт");
        vocab.put("ticket", "билет");
        vocab.put("hotel", "отель");
        vocab.put("train", "поезд");
        return vocab;
    }
}


