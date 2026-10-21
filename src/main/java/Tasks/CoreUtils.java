package main.java.Tasks;
import java.util.Map;
import java.util.Scanner;
public class CoreUtils {
    public static void printResult(int score, int total) {
        double percentage = ((double) score / total) * 100;
        System.out.println("==============================");
        System.out.println("ТЕСТ ЗАВЕРШЕН");
        System.out.println("Правильных ответов: " + score + "/" + total);
        if (percentage == 100.0) {
            System.out.println("Идеально!");
        }
        else if (percentage > 70.0) {
            System.out.println("Хорошо!");
        }
        else {
            System.out.println("Нужно повторить.");
        }
        System.out.println("==============================\n");
    }
    public static void processTasks(Scanner scanner, Map<String, String> tasksMap){
        int score=0;
        int total=tasksMap.size();

        for (Map.Entry<String, String> entry : tasksMap.entrySet()) {
            String taskSentence = entry.getKey();
            String correctAnswer = entry.getValue();
            System.out.print(taskSentence+"\n");
            String userInput = scanner.nextLine().trim().toLowerCase();
            if (userInput.equalsIgnoreCase(correctAnswer)) {
                System.out.println("Верно! \n");
                score++;
            }
            else {
                System.out.printf("Ошибка. Правильный ответ: %s%n%n", correctAnswer);
            }
        }
        printResult(score,total);
    }
}
