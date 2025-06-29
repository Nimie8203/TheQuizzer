package pack1;

import java.sql.*;
import java.util.*;

public class DatabaseManager {
    private Connection connection;

    public DatabaseManager() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/quizzer", "root", "82138");
    }

    public boolean isUsernameTaken(String username) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM users WHERE username = ?");
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    public boolean insertUser(String username, String fullName, String password) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (username, fullname, password, score) VALUES (?, ?, ?, 0)");
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, fullName);
        preparedStatement.setString(3, password);
        return preparedStatement.executeUpdate() > 0;
    }

    public User login(String username, String password) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return new User(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("fullname"),
                resultSet.getString("password"),
                resultSet.getInt("score")
            );
        }
        return null;
    }

    public List<String> getTopScores() throws SQLException {
        List<String> list = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT username, score FROM users ORDER BY score DESC");
        while (resultSet.next()) {
            list.add(resultSet.getString("username") + " - Score: " + resultSet.getInt("score"));
        }
        return list;
    }

    public List<Map<String, Object>> getRandomQuestions(int count) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM questions ORDER BY RAND() LIMIT " + count);
        while (resultSet.next()) {
            Map<String, Object> question = new HashMap<>();
            question.put("id", resultSet.getInt("id"));
            question.put("question", resultSet.getString("question"));
            question.put("correct", resultSet.getString("correct_answer"));
            question.put("wrong1", resultSet.getString("wrong_answer1"));
            question.put("wrong2", resultSet.getString("wrong_answer2"));
            question.put("wrong3", resultSet.getString("wrong_answer3"));
            list.add(question);
        }
        return list;
    }

    public void updateScore(int userId, int score) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET score = ? WHERE id = ?");
        preparedStatement.setInt(1, score);
        preparedStatement.setInt(2, userId);
        preparedStatement.executeUpdate();
    }
}