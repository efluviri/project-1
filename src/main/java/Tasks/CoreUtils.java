package main.java.Tasks;
import java.util.Map;
import java.util.Scanner;
import java.sql.*;
public class CoreUtils {
    public static final String DB_PATH = "data/statistics.db";
    private static long globalQuestionCounter = 1L;
    public static void initDatabase()throws SQLException {
        try (
                Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
                Statement stmt = conn.createStatement()
        ) {
            String sql = """ 
                    CREATE TABLE IF NOT EXISTS user_answers (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id TEXT NOT NULL,
                    task_id TEXT NOT NULL,
                    is_correct BOOLEAN NOT NULL,
                    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
                    );
                    """;
            stmt.execute(sql);
            System.out.println("[INFO] Структура базы данных создана.");
        }
        catch (SQLException e) {
            System.err.println("[ERROR] Не удалось создать таблицу базы данных:");
        }
    }
    public static void logAnswer(String userId, String taskId, boolean isCorrect) {
        try (
                Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
                PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO user_answers(user_id, task_id, is_correct) VALUES(?, ?, ?)"
                )
        ) {
            pstmt.setString(1, userId);
            pstmt.setString(2, taskId);
            pstmt.setBoolean(3, isCorrect);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[ERROR] Ошибка записи статистики:");
        }
    }
    public static void printAllTimeStats(String userId) {
        String sqlCount = """ 
                    SELECT COUNT(*) AS total,
                     SUM(is_correct) AS correct
                    FROM user_answers WHERE user_id=?
                    """;
        try (
                Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
                PreparedStatement pstmt = conn.prepareStatement(sqlCount)
        ) {

            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                System.out.println("\nУ тебя пока нет результатов.");
                return;
            }
            int total = rs.getInt("total");
            int correct = rs.getInt("correct");
            if (total == 0) {
                System.out.println("\nТы ещё не ответил ни на один вопрос!");
            }
            else {
            double accuracy = (double) correct / total;
            System.out.println("\n=== ТВОЯ СТАТИСТИКА С НАЧАЛА ОБУЧЕНИЯ ===");
            System.out.printf("Ты решил %.2f%%%n", accuracy * 100);
            System.out.printf("Правильные ответы: %d из %d.%n%n", correct, total);
            }
        } catch (SQLException e) {
            System.err.println("[ERROR] Не удалось получить статистику:");
        }
    }
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
            boolean isCorrect = userInput.equalsIgnoreCase(correctAnswer);
            if (isCorrect) {
                System.out.println("Верно! \n");
                score++;
            }
            else {
                System.out.printf("Ошибка. Правильный ответ: %s%n%n", correctAnswer);
            }
            String lessonPrefix = "lesson_test";
            String uniqueTaskId = lessonPrefix + "_" +globalQuestionCounter++;
            CoreUtils.logAnswer("test_user",uniqueTaskId,isCorrect);
        }
        printResult(score,total);
    }
}
