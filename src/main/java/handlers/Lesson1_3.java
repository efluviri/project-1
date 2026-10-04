package main.java.handlers;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Lesson1_3 implements Task {
    public void start(Scanner scanner) {
        System.out.println("""
                УРОКИ ПО ТЕМАМ
                [1] Настоящее время
                [2] Будущее время
                [3] Прошедшее время
                """);
        Map<String, Lesson> lessons = new HashMap<>();
        createLessons(lessons);
        String choice = scanner.nextLine().trim();
        Lesson lesson = lessons.get(choice);
        if (lesson == null) {
            System.out.println("Неверный выбор. Возврат в меню.");
            return;
        }
        System.out.println(lesson.rule);
        scanner.nextLine();
        System.out.println("\n[ОТРАБОТКА]");
        int score = 0;
        int tasksSize = lesson.tasks.size();
        int i = 1;

        for (Map.Entry<String, String> entry : lesson.tasks.entrySet()) {
            String taskSentence = entry.getKey();
            String trueAnswer = entry.getValue();
            System.out.println("\nЗадание " + i++ + " из " + tasksSize + ":");
            System.out.print(taskSentence + " ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase(trueAnswer)) {
                System.out.println("Верно");
                score++;
            } else {
                System.out.println("Неверно. Правильный ответ: " + trueAnswer);
            }
        }
        printResult(score, tasksSize);
    }

    private void createLessons(Map<String, Lesson> lessons) {
        Map<String, String> t1 = new HashMap<>();
        t1.put("I ___ (to go) to school every day.", "go");
        t1.put("She ___ (to work) in a bank.", "works");
        t1.put("They ___ (to play) football on Sundays.", "play");
        lessons.put("1", new Lesson("""
             Правило:
                Present Simple — для регулярных действий, привычек и фактов.
                Формула:
                  I / You / We / They + V1
                  He / She / It + V1 + s
                Примеры:
                  I go to school every day.
                  She works in a bank.
             """, t1));

        Map<String, String> t2 = new HashMap<>();
        t2.put("I ___ (to call) you tomorrow.", "will call");
        t2.put("She ___ (to travel) to Japan.", "will travel");
        t2.put("They ___ (to come) back soon.", "will come");
        lessons.put("2", new Lesson("""
             Правило:
                Future Simple — для действий в будущем.
                Формула:
                    Subject + will + V1
                Примеры:
                     I will call you tomorrow.
                     She will travel to Japan.
             """, t2));

        Map<String, String> t3 = new HashMap<>();
        t3.put("I ___ (to go) to the park yesterday.", "went");
        t3.put("She ___ (to work) all night.", "worked");
        t3.put("They ___ (to visit) London last year.", "visited");
        lessons.put("3", new Lesson("""
             Правило:
                 Past Simple — для действий в прошлом.
                 Формула:
                   Subject + V2 (V-ed для правильных глаголов)
                 Примеры:
                   I went to the park yesterday.
                   She worked all night.
             """, t3));
    }
    private static class Lesson {
        String rule;
        Map<String, String> tasks;
        Lesson(String rule, Map<String, String> tasks) {
            this.rule = rule;
            this.tasks = tasks;
        }
    }
}