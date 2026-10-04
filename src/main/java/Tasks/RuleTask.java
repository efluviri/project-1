package main.java.Tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RuleTask implements Task {
    private static final Map<String, String> PRESENT_MAP = createPresentSimpleTasks();
    private static final Map<String, String> FUTURE_MAP = createFutureSimpleTasks();
    private static final Map<String, String> PAST_MAP = createPastSimpleTasks();

    private  String rule;
    private  Map<String, String> tasks;

    public RuleTask(){}

    @Override
    public void start(Scanner scanner) {
        System.out.println("\n=== ИЗУЧЕНИЕ ПРАВИЛА И ОТРАБОТКА ===");
        System.out.println("""
                УРОКИ ПО ТЕМАМ
                [1] Настоящее время
                [2] Будущее время
                [3] Прошедшее время
                """);
        String choice = scanner.nextLine().trim();
        //Map<String, String> lessonContent;
        RuleTask lesson = null;
        switch (choice) {
            case "1":
                this.rule = getPresentRule();
                this.tasks = PRESENT_MAP;
                break;
            case "2":
                this.rule = getFutureRule();
                this.tasks = FUTURE_MAP;
                break;
            case "3":
                this.rule = getPastRule();
                this.tasks = PAST_MAP;
                break;
            default:
                System.out.println("Неверный ввод. Возврат в меню.");
                return;
        }

        System.out.println(this.rule);
        scanner.nextLine();
        System.out.println("\n[ОТРАБОТКА]");
        int score = 0;
        int tasksSize = tasks.size();
        int i = 1;

        for (Map.Entry<String, String> entry : tasks.entrySet()) {
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
        CoreUtils.printResult(score, tasksSize);
    }

    public String getFutureRule() {
        return """
             Правило:
                Future Simple — для действий в будущем.
                Формула:
                    Subject + will + V1
                Примеры:
                     I will call you tomorrow.
                     She will travel to Japan.
             """;

    }
    public String getPastRule() {
        return """
             Правило:
                 Past Simple — для действий в прошлом.
                 Формула:
                   Subject + V2 (V-ed для правильных глаголов)
                 Примеры:
                   I went to the park yesterday.
                   She worked all night.
             """;

    }
    public String getPresentRule() {
        return """
             Правило:
                Present Simple — для регулярных действий, привычек и фактов.
                Формула:
                  I / You / We / They + V1
                  He / She / It + V1 + s
                Примеры:
                  I go to school every day.
                  She works in a bank.
             """;
    }
    private static Map<String, String> createFutureSimpleTasks() {
        Map<String, String> map = new HashMap<>();
        map.put("I ___ (to call) you tomorrow.", "will call");
        map.put("She ___ (to travel) to Japan.", "will travel");
        map.put("They ___ (to come) back soon.", "will come");
        return map;
    }
    private static Map<String, String> createPastSimpleTasks() {
        Map<String, String> map = new HashMap<>();
        map.put("I ___ (to go) to the park yesterday.", "went");
        map.put("She ___ (to work) all night.", "worked");
        map.put("They ___ (to visit) London last year.", "visited");
        return map;
    }
    private static Map<String, String> createPresentSimpleTasks() {
        Map<String, String> map = new HashMap<>();
        map.put("I ___ (to go) to school every day.", "go");
        map.put("She ___ (to work) in a bank.", "works");
        map.put("They ___ (to play) football on Sundays.", "play");
        return map;
    }
}