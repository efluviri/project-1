package main.java.Tasks;
import main.java.inout.InOut;

import java.util.HashMap;
import java.util.Map;

public class RuleTask implements Task {
    private static final Map<String, String> PRESENT_MAP = createPresentSimpleTasks();
    private static final Map<String, String> FUTURE_MAP = createFutureSimpleTasks();
    private static final Map<String, String> PAST_MAP = createPastSimpleTasks();

    private String rule;
    private Map<String, String> tasks;
    private String mapName;

    @Override
    public void start(InOut io,long chatId) {
        io.print("\n ИЗУЧЕНИЕ ПРАВИЛА И ОТРАБОТКА ");
        String[] topics = {"Настоящее", "Будущее", "Прошедшее"};
        String[] callbacks = {"rule_present", "rule_future", "rule_past"};
        io.printWithInlineButtons("Выбери тему:", topics, callbacks);
    }
    public void startTopic(InOut io,long chatId,String topic){
        String rule;
        Map<String, String> tasks;
        String mapName;
        switch (topic) {
            case "present":
                rule = getPresentRule();
                tasks = PRESENT_MAP;
                mapName="present_simple";
                break;
            case "future":
                rule = getFutureRule();
                tasks = FUTURE_MAP;
                mapName="future_simple";
                break;
            case "past":
                rule = getPastRule();
                tasks = PAST_MAP;
                mapName="past_simple";
                break;
            default:
                io.print("Неверный ввод. Возврат в меню.");
                return;
        }
        io.print("\n" + rule);
        io.print("\n[ОТРАБОТКА]");
        CoreUtils.processTasks(io,chatId,tasks,"rule",mapName);
        io.sendBuffer();
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