package main.java.Tasks;
import main.java.inout.InOut;
import java.util.List;
import java.util.ArrayList;

import java.util.Map;
import java.sql.*;
public class CoreUtils {
    public static final String DB_PATH = "data/statistics.db";
    public static void initDatabase(InOut io)throws SQLException {
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
            io.print(" Структура базы данных создана.");
        }
        catch (SQLException e) {
            io.print(" Не удалось создать таблицу базы данных:");
        }
    }
    public static void logAnswer(long userId, String taskId, boolean isCorrect,InOut io) {
        try (
                Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
                PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO user_answers(user_id, task_id, is_correct) VALUES(?, ?, ?)"
                )
        ) {
            pstmt.setString(1, Long.toString(userId));
            pstmt.setString(2, taskId);
            pstmt.setBoolean(3, isCorrect);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            io.print(" Ошибка записи статистики:");
        }
    }
    public static void printAllTimeStats(long userId,InOut io) {
        String sqlCount = """ 
                    SELECT COUNT(*) AS total,
                     SUM(is_correct) AS correct
                    FROM user_answers WHERE user_id=?
                    """;
        try (
                Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
                PreparedStatement pstmt = conn.prepareStatement(sqlCount)

        ) {
            pstmt.setLong(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                io.print("\nУ тебя пока нет результатов.");
                return;
            }
            int total = rs.getInt("total");
            int correct = rs.getInt("correct");
            if (total == 0) {
                io.print("\nТы ещё не ответил ни на один вопрос!");
            }
            else {
                double accuracy = (double) correct / total;
                io.print("\n=== ТВОЯ СТАТИСТИКА С НАЧАЛА ОБУЧЕНИЯ ===");
                io.print("\nТы решил " + String.format("%.2f%%", accuracy * 100) + "\n");
                io.print("Правильные ответы:" + correct + " из "+ total+ "\n");
            }
        } catch (SQLException e) {
            io.print(" Не удалось получить статистику:");
        }
    }
    public static void printProgress(long userId, InOut io) {
        String sql = """ 
                SELECT
                     CASE
                       WHEN INSTR(task_id, '_') > 0
                       THEN SUBSTR(task_id, 1, INSTR(task_id, '_') - 1)
                       ELSE task_id
                     END AS topic_group,
                     COUNT(*) AS total_attempts,
                     SUM(is_correct) AS correct_answers
                FROM user_answers
                WHERE user_id = ?
                GROUP BY topic_group
                ORDER BY topic_group ASC;
                """;
        try (
             Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
             PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setLong(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                io.print("\nУ тебя пока нет результатов.");
                return;
            }
            io.print("\n ТВОЙ ПРОГРЕСС ПО ЗАДАНИЯМ ");
            do {
                String topic = rs.getString("topic_group");
                int total = rs.getInt("total_attempts");
                int correct = rs.getInt("correct_answers");
                double accuracy = (double) correct / total;
                io.print(String.format("%-20s | %.2f%%%n", topic, accuracy * 100));
            } while (rs.next());
        } catch (SQLException e) {
            io.print(" ОШИБКА: Не удалось получить статистику:");
        }
    }
    public static void printResult(int score, int total,InOut io) {
        double percentage = ((double) score / total) * 100;
        io.print("==============================");
        io.print("ТЕСТ ЗАВЕРШЕН");
        io.print("Правильных ответов: " + score + "/" + total);
        if (percentage == 100.0) {
            io.print("Идеально!");
        }
        else if (percentage > 70.0) {
            io.print("Хорошо!");
        }
        else {
            io.print("Нужно повторить.");
        }
        io.print("==============================\n");
    }

    public static void processTasks(InOut io,long userId, Map<String, String> tasksMap,String taskType,String mapName){
        int score=0;
        int total=tasksMap.size();

        for (Map.Entry<String, String> entry : tasksMap.entrySet()) {
            String taskSentence = entry.getKey();
            String correctAnswer = entry.getValue();
            io.print(taskSentence+"\n");
            String userInput = io.readline().toLowerCase();
            boolean isCorrect = userInput.equalsIgnoreCase(correctAnswer);
            if (isCorrect) {
                io.print("Верно! \n");
                score++;
            }
            else {
                io.print(String.format("Ошибка. Правильный ответ: %s%n", correctAnswer));
            }
            int questionHash = Math.abs(taskSentence.hashCode());
            String key = Long.toHexString(questionHash).substring(0, 4);
            String uniqueTaskId;
            if (!mapName.isEmpty()) {
                uniqueTaskId = taskType + "_" + mapName + "_" + key;
            }
            else {
                uniqueTaskId = taskType + "_" + key;
            }
            logAnswer(userId,uniqueTaskId,isCorrect,io);
        }
        printResult(score,total,io);
    }

}
