package main.java.Tasks;

import main.java.inout.InOut;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WordGuessTask implements Task {

    private static final Map<String, String> EASY_WORDS = createEasyWords();
    private static final Map<String, String> MEDIUM_WORDS = createMediumWords();
    private static final Map<String, String> HARD_WORDS = createHardWords();

    @Override
    public void start(InOut io, long chatId) {
        io.print("\n=== ИГРА: УГАДАЙ СЛОВО ===");
        io.print("Выбери уровень сложности:");
        io.print("[1] Легкий");
        io.print("[2] Средний");
        io.print("[3] Сложный");

        String choice = io.readline().trim();
        Map<String, String> selectedMap;

        switch (choice) {
            case "1":
                selectedMap = EASY_WORDS;
                break;
            case "2":
                selectedMap = MEDIUM_WORDS;
                break;
            case "3":
                selectedMap = HARD_WORDS;
                break;
            default:
                io.print("Неверный выбор.");
                return;
        }

        Game(io, chatId, selectedMap);
    }

    private void Game(InOut io, long chatId, Map<String, String> wordsMap) {
        Random random = new Random();
        int score = 0;
        int totalQuestions = 10;

        io.print("\nУгадай слово по описанию! (10 вопросов)\n");

        Object[] descriptions = wordsMap.keySet().toArray();
        boolean[] used = new boolean[descriptions.length];

        for (int i = 0; i < totalQuestions; i++) {
            int randomIndex;
            do {
                randomIndex = random.nextInt(descriptions.length);
            } while (used[randomIndex]);

            used[randomIndex] = true;
            String currentDescription = (String) descriptions[randomIndex];
            String correctAnswer = wordsMap.get(currentDescription);

            io.print("Вопрос " + (i + 1) + "/" + totalQuestions);
            io.print(currentDescription);
            io.print("Твой ответ: ");

            String userAnswer = io.readline().trim();
            if (userAnswer.equalsIgnoreCase(correctAnswer)) {
                io.print("Верно! ");
                score++;
                CoreUtils.logAnswer(chatId, "guess_word", true, io);
            } else {
                io.print("Неверно.  Правильный ответ: " + correctAnswer);
                CoreUtils.logAnswer(chatId, "guess_word", false, io);
            }
            io.print("");
        }

        io.print("Игра окончена! Твой результат: " + score + " из " + totalQuestions);
    }

    private static Map<String, String> createEasyWords() {
        Map<String, String> map = new HashMap<>();
        map.put("Домашнее животное, которое мяукает", "cat");
        map.put("Желтый фрукт, который любят обезьяны", "banana");
        map.put("Светит ночью на небе", "moon");
        map.put("Столица России", "Moscow");
        map.put("Используется для письма", "pen");
        map.put("Большое животное с хоботом", "elephant");
        map.put("Время суток, когда мы спим", "night");
        map.put("Холодное время года со снегом", "winter");
        map.put("Транспорт на двух колесах", "bicycle");
        map.put("Место, где учатся дети", "school");
        return map;
    }

    private static Map<String, String> createMediumWords() {
        Map<String, String> map = new HashMap<>();
        map.put("Место, где живут книги", "library");
        map.put("Человек, который лечит людей", "doctor");
        map.put("Устройство для полета в космос", "rocket");
        map.put("Время года, когда распускаются цветы", "spring");
        map.put("Человек, который готовит еду в ресторане", "chef");
        map.put("Наука о звездах и планетах", "astronomy");
        map.put("Место, где можно купить лекарства", "pharmacy");
        map.put("Человек, который изучает древности", "archaeologist");
        map.put("Прибор для измерения температуры", "thermometer");
        map.put("Искусство красивого письма", "calligraphy");
        return map;
    }

    private static Map<String, String> createHardWords() {
        Map<String, String> map = new HashMap<>();
        map.put("Наука о небесных телах и вселенной", "astrophysics");
        map.put("Красноречие, умение красиво говорить", "eloquence");
        map.put("Неизбежный, тот, которого нельзя избежать", "inevitable");
        map.put("Человек, который пишет музыку", "composer");
        map.put("Явление, когда вода превращается в пар", "evaporation");
        map.put("Небольшая часть чего-то целого", "fragment");
        map.put("Чувство сильного страха, паника", "panic");
        map.put("Тот, кто управляет оркестром", "conductor");
        map.put("Сложная ситуация, запутанное дело", "dilemma");
        map.put("Человек, который предсказывает будущее", "prophet");
        return map;
    }
}