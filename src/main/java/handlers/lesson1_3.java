package main.java.handlers;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class lesson1_3 {
    private static class Lesson {
        String rule;
        Map<String, String> tasks;
        Lesson(String rule, Map<String, String> tasks) {
            this.rule = rule;
            this.tasks = tasks;
        }
    }
    private static final Map<String, Lesson> LESSONS = new HashMap<>();

    static {
        Map<String, String> t1 = new HashMap<>();
        t1.put("I ___ (to go) to school every day.", "go");
        t1.put("She ___ (to work) in a bank.", "works");
        t1.put("They ___ (to play) football on Sundays.", "play");
        LESSONS.put("1", new Lesson("""
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
        LESSONS.put("2", new Lesson("""
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
        LESSONS.put("3", new Lesson("""
             Правило:
                 Past Simple — для действий в прошлом.
                 Формула:
                   Subject + V2 (V-ed для правильных глаголов)
                 Примеры:
                   I went to the park yesterday.
                   She worked all night.
             """, t3));
    }

    public void start(Scanner scanner) {
        System.out.println("""
                УРОКИ ПО ТЕМАМ
                [1] Настоящее время
                [2] Будущее время
                [3] Прошедшее время
                """);

        Lesson lesson = LESSONS.get(scanner.nextLine().trim());
        if (lesson == null) {
            System.out.println("Неверный выбор. Возврат в меню.");
            return;
        }

        System.out.println(lesson.rule);
        scanner.nextLine();

        System.out.println("\n[ОТРАБОТКА]");
        int score = 0, total = lesson.tasks.size(), i = 1;
        for (Map.Entry<String, String> e : lesson.tasks.entrySet()) {
            System.out.println("\nЗадание " + i++ + " из " + total + ":");
            System.out.println(e.getKey());
            System.out.print("Ваш ответ: ");
            if (scanner.nextLine().trim().equalsIgnoreCase(e.getValue())) {
                System.out.println("Верно!");
                score++;
            } else {
                System.out.println("Неверно. Правильный ответ: " + e.getValue());
            }
        }

        System.out.println("\n=== РЕЗУЛЬТАТ ===");
        System.out.println("Правильных ответов: " + score + " из " + total);
        scanner.nextLine();
    }
}