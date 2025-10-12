package ac.za.postapp.Pages;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class SessionManager {

    public static String createSession(int userId) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plus(7, ChronoUnit.DAYS);

        String sql = "INSERT INTO sessions (user_id, session_token, expires_at) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, token);
            pstmt.setTimestamp(3, Timestamp.valueOf(expiresAt));

            pstmt.executeUpdate();
            return token;

        } catch (SQLException e) {
            System.err.println("Error creating session: " + e.getMessage());
            return null;
        }
    }

    public static boolean validateSession(String token) {
        String sql = "SELECT * FROM sessions WHERE session_token = ? AND expires_at > NOW()";

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

    public static void invalidateSession(String token) {
        String sql = "DELETE FROM sessions WHERE session_token = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, token);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error invalidating session: " + e.getMessage());
        }
    }

    public static void cleanupExpiredSessions() {
        String sql = "DELETE FROM sessions WHERE expires_at <= NOW()";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            System.err.println("Error cleaning up expired sessions: " + e.getMessage());
        }
    }
}