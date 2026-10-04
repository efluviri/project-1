package main.java.Tasks;

public class CoreUtils {
    public static void printResult(int score, int total) {
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
