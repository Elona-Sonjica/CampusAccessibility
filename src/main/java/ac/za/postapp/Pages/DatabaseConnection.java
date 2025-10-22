package ac.za.postapp.Pages;

import java.sql.*;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/campus_access_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Elona@2004";
    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("Database connected successfully!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            showErrorDialog("MySQL JDBC Driver not found. Please make sure:\n" +
                    "1. MySQL is installed and running\n" +
                    "2. MySQL Connector/J is in your classpath\n" +
                    "3. Database 'campus_access_db' exists");
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            showErrorDialog("Cannot connect to database. Please check:\n" +
                    "1. MySQL server is running\n" +
                    "2. Database credentials are correct\n" +
                    "3. Database 'campus_access_db' exists\n\n" +
                    "Error: " + e.getMessage());
        }
        return connection;
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("Database initialization successful");

                // Create events table if it doesn't exist
                String createEventsTableSQL = "CREATE TABLE IF NOT EXISTS events (" +
                        "event_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "title VARCHAR(255) NOT NULL, " +
                        "description TEXT, " +
                        "building VARCHAR(100) NOT NULL, " +
                        "room VARCHAR(50) NOT NULL, " +
                        "start_time TIMESTAMP NOT NULL, " +
                        "end_time TIMESTAMP NOT NULL, " +
                        "accessibility VARCHAR(255) NOT NULL, " +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(createEventsTableSQL);
                    System.out.println("Events table ready");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }

    // Helper method to show error dialogs without importing JOptionPane
    private static void showErrorDialog(String message) {
        System.err.println("ERROR: " + message);
    }
}