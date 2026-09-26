package main.java.handlers;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
public class FillBlankTask1_1 implements Task{
    @Override
    public void start(Scanner scanner) {
        System.out.println("\n=== ЗАДАНИЕ: ЗАПОЛНИ ПРОПУСК ===");
        Map<String, String> sentences = new HashMap<>();
        fillDatabase(sentences);
        int score = 0;
        int totalQuestions = sentences.size();
        System.out.println("Вставь пропущенное слово:");
        for (Map.Entry<String, String> entry : sentences.entrySet()) {
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
        printResult(score, totalQuestions);
    }
    private void fillDatabase(Map<String, String> db) {
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
    }
    private void printResult(int score, int total) {
        double percentage = ((double) score / total) * 100;
        System.out.println("==============================");
        System.out.println("ТЕСТ ПО ПРЕДЛОЖЕНИЯМ ЗАВЕРШЕН");
        System.out.println("Правильных ответов: " + score + "/" + total);
        if (percentage == 100) {
            System.out.println("Идеально! Вы мастер грамматики!");
        }
        else if (percentage > 70) {
            System.out.println("Хорошо! Но есть что повторить.");
        }
        else {
            System.out.println("Нужно подучить правила.");
        }
        System.out.println("==============================\n");
    }

}
