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
        io.print("[1] Легкий (3 жизни, 60 сек.)");
        io.print("[2] Средний (2 жизни, 45 сек.)");
        io.print("[3] Сложный (1 жизнь, 30 сек.)");

        String choice = io.readline().trim();
        Map<String, String> selectedMap;
        int level;

        switch (choice) {
            case "1":
                selectedMap = EASY_WORDS;
                level=1;
                break;
            case "2":
                selectedMap = MEDIUM_WORDS;
                level=2;
                break;
            case "3":
                selectedMap = HARD_WORDS;
                level=3;
                break;
            default:
                io.print("Неверный выбор.");
                return;
        }

        Game(io, chatId, selectedMap,level);
    }

    private void Game(InOut io, long chatId, Map<String, String> wordsMap,int level) {
        int currentLife = switch(level) {
            case 1 -> 3;
            case 2 -> 2;
            default -> 1;
        };
        int skipTokens = 2;
        int helpTokens = 3;
        Random random = new Random();
        int score = 0;
        int totalQuestions = 10;
        Map<String, Boolean> usedHelps = new HashMap<>();

        io.print("\nУгадай слово по описанию! (10 вопросов)\n");

        Object[] descriptions = wordsMap.keySet().toArray();
        boolean[] used = new boolean[descriptions.length];

        for (int i = 0; i < totalQuestions; i++) {
            if (currentLife <= 0 || skipTokens < 0) break;
            int randomIndex;
            do {
                randomIndex = random.nextInt(descriptions.length);
            } while (used[randomIndex]);
            used[randomIndex] = true;

            String currentDescription = (String) descriptions[randomIndex];
            String correctAnswer = wordsMap.get(currentDescription);

            boolean questionAnswered = false;
            while (!questionAnswered) {

                io.print("\nТвои жизни "+ currentLife );
                io.print("Осталось пропусков: " + skipTokens + " | Осталось подсказок: "+ helpTokens);
                io.print("Вопрос " + (i + 1) + "/" + totalQuestions);
                io.print(currentDescription);
                io.print("Твой ответ: ");

                long startTime = System.currentTimeMillis();
                String userInput = io.readline();
                long spentTime = System.currentTimeMillis() - startTime;

                double timeLimit;
                if (level == 1) timeLimit = 60000;
                else if (level == 2) timeLimit = 45000;
                else timeLimit = 30000;

                if (spentTime >= timeLimit) {
                    io.print("\n⏰ Время вышло!");
                    io.print("Правильный ответ: " + correctAnswer);
                    currentLife--;
                    break;
                }

                String userAnswer = userInput.trim().toLowerCase();

                if ("/skip".equals(userAnswer)) {
                    if (handleSkip(io, skipTokens)) skipTokens--;
                    else currentLife--;
                    questionAnswered = true;
                    continue;
                }
                if ("/help".equals(userAnswer)) {
                    if (usedHelps.containsKey(currentDescription)) {
                        io.print("Подсказка по этому слову уже использована.");
                        continue;
                    }
                    if (giveHelp(correctAnswer, helpTokens, io)) {
                        helpTokens--;
                        usedHelps.put(currentDescription, true);
                    }
                    continue;
                }

                if (userAnswer.equalsIgnoreCase(correctAnswer)) {
                    io.print("Верно! ");
                    score++;
                    CoreUtils.logAnswer(chatId, "guess_word", true, io);
                    questionAnswered = true;
                } else {
                    io.print("Неверно. Правильный ответ: " + correctAnswer + ". Оставшиеся жизни: " + currentLife);
                    CoreUtils.logAnswer(chatId, "guess_word", false, io);
                    currentLife--;
                    if (currentLife <= 0) { questionAnswered = true; }
                }
                io.print("");
            }
        }
        io.print("Игра окончена! Твой результат: " + score + " из " + totalQuestions);
    }
    private boolean handleSkip(InOut io, int skips) {
        if (skips > 0) {
            io.print("Ты пропустил слово.");
            return true;
        }
        else {
            io.print("У тебя закончились пропуска!");
            return false;
        }
    }
    private boolean giveHelp(String answer, int helpTokens, InOut io) {
        if (helpTokens <= 0) {
            io.print("У тебя нет доступных подсказок!");
            return false;
        }
        char firstLetter = Character.toUpperCase(answer.charAt(0));
        int length = answer.length();
        io.print("\n ПОДСКАЗКА: \n"+
                "- Первая буква: " + firstLetter +"\n"+
                "- Количество букв: " + length+"\n"+
                "  Осталось подсказок: " + (helpTokens-1));
        return true;

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