package ac.za.postapp.Pages;

import ac.za.postapp.Pages.User;
import java.sql.*;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class UserService {

    public static boolean registerUser(User user, String password) {
        String sql = "INSERT INTO users (name, surname, student_number, age, gender, email, password_hash) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Hash the password (in a real application, use a proper hashing algorithm like BCrypt)
            String hashedPassword = simpleHash(password);

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getSurname());
            pstmt.setString(3, user.getStudentNumber());
            pstmt.setInt(4, user.getAge());
            pstmt.setString(5, user.getGender());
            pstmt.setString(6, user.getEmail());
            pstmt.setString(7, hashedPassword);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
            return false;
        }
    }

    public static User loginUser(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password_hash = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String hashedPassword = simpleHash(password);
            pstmt.setString(1, email);
            pstmt.setString(2, hashedPassword);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("student_number"),
                            rs.getInt("age"),
                            rs.getString("gender"),
                            rs.getString("email")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error during login: " + e.getMessage());
        }

        return null;
    }

    public static boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error checking email: " + e.getMessage());
            return false;
        }
    }

    public static boolean studentNumberExists(String studentNumber) {
        String sql = "SELECT COUNT(*) FROM users WHERE student_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error checking student number: " + e.getMessage());
            return false;
        }
    }

    // Simple hash function for demonstration (use a proper hashing library in production)
    private static String simpleHash(String input) {
        return Integer.toString(input.hashCode());
    }

    // Session management methods
    public static String createSession(int userId) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plus(7, ChronoUnit.DAYS); // 7-day session

        String sql = "INSERT INTO sessions (user_id, session_token, expires_at) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, token);
            pstmt.setString(3, expiresAt.toString());

            pstmt.executeUpdate();
            return token;

        } catch (SQLException e) {
            System.err.println("Error creating session: " + e.getMessage());
            return null;
        }
    }

    public static boolean validateSession(String token) {
        String sql = "SELECT * FROM sessions WHERE session_token = ? AND expires_at > datetime('now')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, token);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("Error validating session: " + e.getMessage());
            return false;
        }
    }
}
