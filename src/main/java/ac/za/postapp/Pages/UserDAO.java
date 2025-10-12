package ac.za.postapp.Pages;

import ac.za.postapp.connection.DatabaseConnection;
import ac.za.postapp.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.time.LocalDateTime;

public class UserDAO {

    public static boolean registerUser(User user, String password) {
        String sql = "INSERT INTO users (name, surname, student_number, age, gender, email, " +
                "password_hash, device_type, avoid_stairs, prefer_ramps, min_path_width_cm) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getSurname());
            pstmt.setString(3, user.getStudentNumber());
            pstmt.setInt(4, user.getAge());
            pstmt.setString(5, user.getGender());
            pstmt.setString(6, user.getEmail());
            pstmt.setString(7, hashedPassword);
            pstmt.setString(8, user.getDeviceType());
            pstmt.setBoolean(9, user.isAvoidStairs());
            pstmt.setBoolean(10, user.isPreferRamps());
            pstmt.setInt(11, user.getMinPathWidthCm());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
            return false;
        }
    }

    public static User loginUser(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password_hash");
                    if (BCrypt.checkpw(password, hashedPassword)) {
                        return new User(
                                rs.getString("name"),
                                rs.getString("surname"),
                                rs.getString("student_number"),
                                rs.getInt("age"),
                                rs.getString("gender"),
                                rs.getString("email"),
                                rs.getString("device_type"),
                                rs.getBoolean("avoid_stairs"),
                                rs.getBoolean("prefer_ramps"),
                                rs.getInt("min_path_width_cm")
                        );
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error during login: " + e.getMessage());
        }

        return null;
    }

    public static boolean updateUserPreferences(User user) {
        String sql = "UPDATE users SET device_type = ?, avoid_stairs = ?, " +
                "prefer_ramps = ?, min_path_width_cm = ?, updated_at = ? " +
                "WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getDeviceType());
            pstmt.setBoolean(2, user.isAvoidStairs());
            pstmt.setBoolean(3, user.isPreferRamps());
            pstmt.setInt(4, user.getMinPathWidthCm());
            pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(6, user.getEmail());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating user preferences: " + e.getMessage());
            return false;
        }
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
}