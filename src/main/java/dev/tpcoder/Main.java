package dev.tpcoder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        var url = System.getenv("DB_URL");
        var user = System.getenv("DB_USER");
        var password = System.getenv("DB_PASSWORD");
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            if (connection != null) {
                // Example of Query
                var result = queryCategory(connection);
                for (var c : result) {
                    System.out.println(c);
                }
                // Insert
                insertCategory(connection, "Ramen");
                // Update
                updateCategory(connection, 4, "Sushi");
                // Delete
                deleteCategory(connection, 4);
            } else {
                System.err.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            System.err.println("Connection failure: " + e.getMessage());
        }
    }

    private static List<Category> queryCategory(Connection connection) throws SQLException {
        var sql = """
                SELECT *
                FROM category c;
                """;
        List<Category> result = new ArrayList<>();
        try (var statement = connection.createStatement();
             var resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                var id = resultSet.getInt("id");
                var name = resultSet.getString("name");
                var category = new Category(id, name);
                result.add(category);
            }
        }
        return result;
    }

    private static void insertCategory(Connection connection, String name) throws SQLException {
        var sql = """
                INSERT INTO category(name)
                VALUES(?);
                """;
        try (var prepareStatement = connection.prepareStatement(sql)) {
            prepareStatement.setString(1, name);
            prepareStatement.executeUpdate();
        }
    }

    private static void updateCategory(Connection connection, int id, String name) throws SQLException {
        var sql = """
                UPDATE category SET name = ?
                WHERE id = ?;
                """;
        try (var prepareStatement = connection.prepareStatement(sql)) {
            prepareStatement.setString(1, name);
            prepareStatement.setInt(2, id);
            prepareStatement.executeUpdate();
        }
    }

    private static void deleteCategory(Connection connection, int id) throws SQLException {
        var sql = """
                DELETE FROM category WHERE id = ?;
                """;
        try (var prepareStatement = connection.prepareStatement(sql)) {
            prepareStatement.setInt(1, id);
            prepareStatement.executeUpdate();
        }
    }

    private record Category(int id, String name) {

    }
}