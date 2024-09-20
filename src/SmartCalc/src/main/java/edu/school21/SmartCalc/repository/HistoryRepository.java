package edu.school21.SmartCalc.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoryRepository {
    private static final String DB_URL = "jdbc:sqlite:calc_history.db";

    public HistoryRepository() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS history (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "expression TEXT NOT NULL )";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveHistoryItem(String expression) {
        String insertSQL = "INSERT INTO history (expression) VALUES (?)";


        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, expression);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<String> loadHistory() {
        String selectSQL = "SELECT expression FROM history";
        List<String> historyItems = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(selectSQL);
            while (rs.next()) {
                String expression = rs.getString("expression");
                historyItems.add(expression);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return historyItems;
    }

    public void clearHistory() {
        String deleteSQL = "DELETE FROM history";
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(deleteSQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
