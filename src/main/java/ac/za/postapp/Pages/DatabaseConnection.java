package ac.za.postapp.Pages;

import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/campus_access_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Elona@2004";
    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Load MySQL JDBC Driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
        return connection;
    }

    public static void initializeDatabase() {
        // For MySQL, we assume the database and tables are created manually
        try (Connection conn = getConnection()) {
            System.out.println("Database connection established successfully");

            // Clean up expired sessions on startup
            SessionManager.cleanupExpiredSessions();

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }

    // Proper password hashing with BCrypt
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Check if password matches the hash
    public static boolean checkPassword(String password, String hashedPassword) {
        try {
            return BCrypt.checkpw(password, hashedPassword);
        } catch (Exception e) {
            System.err.println("Error checking password: " + e.getMessage());
            return false;
        }
    }
}