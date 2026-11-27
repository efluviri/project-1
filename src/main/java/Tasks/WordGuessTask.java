package main.java.Tasks;

import main.java.inout.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WordGuessTask implements Task {

    private static final Map<String, String> EASY_WORDS = createEasyWords();
    private static final Map<String, String> MEDIUM_WORDS = createMediumWords();
    private static final Map<String, String> HARD_WORDS = createHardWords();

    @Override
    public void start(InOut io, long chatId) {
        io.print("\n ИГРА: УГАДАЙ СЛОВО");
        io.print("Выбери уровень сложности: Легкий (3 жизни, 60 сек.), Средний (2 жизни, 45 сек.), Сложный (1 жизнь, 30 сек.)");
        String[] levels = {"1. Легкий", "2. Средний", "3. Сложный"};
        String[] callbacks = {"level_1", "level_2", "level_3"};
        io.printWithInlineButtons("Выбери уровень:", levels, callbacks);
    }
    public void startGame(InOut io,long chatId,int level){
        Map<String, String> selectedMap;

        switch (level) {
            case 1:
                selectedMap = EASY_WORDS;
                break;
            case 2:
                selectedMap = MEDIUM_WORDS;
                break;
            case 3:
                selectedMap = HARD_WORDS;
                break;
            default:
                selectedMap = EASY_WORDS;
        }
        runGame(io, chatId, selectedMap,level);
    }

    private void runGame(InOut io, long chatId, Map<String, String> wordsMap, int level) {
        int timeLimit;
        if (level == 1) {
            timeLimit = 60000;
        } else if (level == 2) {
            timeLimit = 45000;
        } else {
            timeLimit = 30000;
        }
        int currentLife = 4 - level;
        int skipTokens = 2;
        int helpTokens = 3;
        Random random = new Random();
        int score = 0;
        int totalQuestions = 10;
        Map<String, Boolean> usedHelps = new HashMap<>();

        io.print("\nУгадай слово по описанию! (10 вопросов)\n");

        Object[] descriptions = wordsMap.keySet().toArray();
        boolean[] used = new boolean[descriptions.length];

        for (int i = 0; i < totalQuestions && currentLife > 0; i++) {
            int randomIndex;
            do {
                randomIndex = random.nextInt(descriptions.length);
            } while (used[randomIndex]);
            used[randomIndex] = true;

            String currentDescription = (String) descriptions[randomIndex];
            String correctAnswer = wordsMap.get(currentDescription);

            boolean questionAnswered = false;
            while (!questionAnswered && currentLife > 0) {

                io.print("\nТвои жизни " + currentLife);
                io.print("Осталось пропусков: " + skipTokens + " | Осталось подсказок: " + helpTokens);
                io.print("Вопрос " + (i + 1) + "/" + totalQuestions);
                io.print(currentDescription);
                io.sendBuffer();

                String[] actionButtons = {" Подсказка", " Пропустить"};
                String[] actionData = {"HELP", "SKIP"};
                io.printWithInlineButtons("Твой ответ:", actionButtons, actionData);

                int messageId = io.printWithInlineButtons("Твой ответ:", actionButtons, actionData);

                boolean[] timeIsOut = {false};
                final int[] secondsLeft = {timeLimit / 1000};

                java.util.Timer timer = new java.util.Timer();
                timer.scheduleAtFixedRate(new java.util.TimerTask() {
                    @Override
                    public void run() {
                        if (secondsLeft[0] > 0) {
                            if (secondsLeft[0] % 5 == 0) {
                                ((TelegramInOut) io).editMessage(messageId,
                                        "Вопрос: " + currentDescription + "\n\nУ тебя осталось " + secondsLeft[0] + " секунд!",
                                        new String[]{" Подсказка", " Пропустить"},
                                        new String[]{"HELP", "SKIP"});
                            }
                            secondsLeft[0]--;
                        } else {
                            timeIsOut[0] = true;
                            ((TelegramInOut) io).editMessage(messageId,
                                    " Время вышло! Правильный ответ: " + correctAnswer,
                                    new String[]{" Подсказка", " Пропустить"},
                                    new String[]{"HELP", "SKIP"});
                            timer.cancel();
                        }
                    }
                }, 0, 1000);

                String userInput = io.readline();
                timer.cancel();

                if (timeIsOut[0]) {
                    io.print("Время вышло! Ты не успел ответить.");
                    currentLife--;
                    questionAnswered = true;
                    continue;
                }

                if (userInput == null) {
                    io.print("Ошибка получения ответа. Игра прервана.");
                    return;
                }

                String userAnswer = userInput.trim().toLowerCase();


                if ("/skip".equals(userAnswer) || "SKIP".equalsIgnoreCase(userInput)) {
                    if (handleSkip(io, skipTokens)) skipTokens--;
                    else currentLife--;
                    questionAnswered = true;
                    continue;
                }


                if ("/help".equals(userAnswer) || "HELP".equalsIgnoreCase(userInput)) {
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
                } else {
                    io.print("Неверно. Правильный ответ: " + correctAnswer);
                    CoreUtils.logAnswer(chatId, "guess_word", false, io);
                    currentLife--;
                }
                questionAnswered = true;
                io.print("");
            }
        }
        io.print("Игра окончена! Твой результат: " + score + " из " + totalQuestions);
        io.sendBuffer();
        io.printWithInlineButtons(
                "Выбери задание:",
                new String[]{"Заполнить пропуск", "Уроки по темам", "Переведи слово", "Игра: Угадай слово", "Статистика", "Прогресс"},
                new String[]{"/fillblank", "/rules", "/vocabulary", "/guess", "/stats", "/progress"}
        );
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

