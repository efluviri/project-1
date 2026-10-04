package main.java.Tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
public class FillBlankTask implements Task {
    private static final Map<String, String> SENTENCES = createDatabase();

    @Override
    public void start(Scanner scanner) {
        System.out.println("\n=== ЗАДАНИЕ: ЗАПОЛНИ ПРОПУСК ===");
        int score = 0;
        int totalQuestions = SENTENCES.size();
        System.out.println("Вставь пропущенное слово:");
        for (Map.Entry<String, String> entry : SENTENCES.entrySet()) {
            String taskSentence = entry.getKey();
            String correctAnswer = entry.getValue();
            System.out.print(taskSentence + " ");
            String userInput = scanner.nextLine().trim().toLowerCase();
           if (userInput.equalsIgnoreCase(correctAnswer)) {
               System.out.println("Верно! \n");
               score++;
           }
           else {
            System.out.printf("Ошибка. Правильный ответ: %s%n%n", correctAnswer);
           }
        }
        CoreUtils.printResult(score, totalQuestions);
    }
    private static Map<String, String> createDatabase() {
        Map<String, String> db = new HashMap<>();
        db.put("I ___ to the cinema yesterday.", "went");
        db.put("She ___ coffee every morning.", "drinks");
        db.put("Moscow is a very ___ city.", "big");
        db.put("This is my favorite ___ .", "book");
        db.put("Look! It ___ outside right now. (snow)", "is snowing");
        db.put("She usually ___ to work by bus. (go)", "goes");
        db.put("I ___ coffee every morning before school. (drink)", "drink");
        db.put("Be quiet! The baby ___ . (sleep)", "is sleeping");
        db.put("Look at those dark clouds! It ___ rain soon. (is going to)", "is going to");
        db.put("My brother ___ twenty years old next Monday. (be)", "will be");
        db.put("I think she ___ pass the exam easily. (will)", "will");
        db.put("We ___ visit our grandparents this weekend. (are going to)", "are going to");
        return db;
    }
}
