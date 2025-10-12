package ac.za.postapp.Pages;

import java.sql.*;
import java.time.LocalDateTime;
import javax.swing.JOptionPane;

public class UserDAO {

    // Enhanced method to create the users table with all required columns
    public static void createUsersTable() {
        System.out.println("Creating users table...");

        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "surname VARCHAR(100) NOT NULL, " +
                "student_number VARCHAR(20) UNIQUE NOT NULL, " +
                "age INT NOT NULL, " +
                "gender VARCHAR(20) NOT NULL, " +
                "email VARCHAR(255) UNIQUE NOT NULL, " +
                "password_hash VARCHAR(255) NOT NULL, " +
                "device_type VARCHAR(50) DEFAULT 'None', " +
                "avoid_stairs BOOLEAN DEFAULT FALSE, " +
                "prefer_ramps BOOLEAN DEFAULT FALSE, " +
                "min_path_width_cm INT DEFAULT 90, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "updated_at TIMESTAMP NULL)";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("✓ Users table created/verified successfully");

        } catch (SQLException e) {
            System.err.println("✗ Error creating users table: " + e.getMessage());
            // If table exists but missing columns, try to alter it
            alterUsersTable();
        }
    }

    // Method to add missing columns if table already exists
    private static void alterUsersTable() {
        System.out.println("Attempting to alter users table...");

        String[] alterStatements = {
                "ALTER TABLE users ADD COLUMN device_type VARCHAR(50) DEFAULT 'None'",
                "ALTER TABLE users ADD COLUMN avoid_stairs BOOLEAN DEFAULT FALSE",
                "ALTER TABLE users ADD COLUMN prefer_ramps BOOLEAN DEFAULT FALSE",
                "ALTER TABLE users ADD COLUMN min_path_width_cm INT DEFAULT 90",
                "ALTER TABLE users ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
                "ALTER TABLE users ADD COLUMN updated_at TIMESTAMP NULL"
        };

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            for (String alterSql : alterStatements) {
                try {
                    stmt.execute(alterSql);
                    System.out.println("✓ Executed: " + alterSql);
                } catch (SQLException e) {
                    System.err.println("✗ Column may already exist: " + alterSql);
                }
            }

        } catch (SQLException e) {
            System.err.println("✗ Error altering users table: " + e.getMessage());
        }
    }

    // Method to drop and recreate the table (handles foreign key constraints)
    public static void recreateUsersTable() {
        System.out.println("Recreating users table...");

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // First, disable foreign key checks
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");
            System.out.println("✓ Disabled foreign key checks");

            // Drop sessions table first if it exists
            try {
                stmt.execute("DROP TABLE IF EXISTS sessions");
                System.out.println("✓ Dropped sessions table");
            } catch (SQLException e) {
                System.err.println("✗ Error dropping sessions table: " + e.getMessage());
            }

            // Drop users table
            stmt.execute("DROP TABLE IF EXISTS users");
            System.out.println("✓ Dropped users table");

            // Re-enable foreign key checks
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
            System.out.println("✓ Re-enabled foreign key checks");

            // Create new table
            createUsersTable();

            // Recreate sessions table
            createSessionsTable();

        } catch (SQLException e) {
            System.err.println("✗ Error recreating users table: " + e.getMessage());
            // Make sure to re-enable foreign key checks even if error occurs
            try (Connection conn = DatabaseConnection.getConnection();
                 Statement stmt = conn.createStatement()) {
                stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
            } catch (SQLException e2) {
                System.err.println("✗ Error re-enabling foreign key checks: " + e2.getMessage());
            }
        }
    }

    // Method to create sessions table
    public static void createSessionsTable() {
        System.out.println("Creating sessions table...");

        String sql = "CREATE TABLE IF NOT EXISTS sessions (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "user_id INT NOT NULL, " +
                "session_token VARCHAR(255) UNIQUE NOT NULL, " +
                "expires_at TIMESTAMP NOT NULL, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("✓ Sessions table created/verified successfully");

        } catch (SQLException e) {
            System.err.println("✗ Error creating sessions table: " + e.getMessage());
        }
    }

    public static boolean registerUser(User user, String password) {
        // First, make sure the table has all required columns
        createUsersTable();

        String sql = "INSERT INTO users (name, surname, student_number, age, gender, email, " +
                "password_hash, device_type, avoid_stairs, prefer_ramps, min_path_width_cm) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Simple password hash for demo
            String hashedPassword = simpleHash(password);

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

            if (affectedRows > 0) {
                System.out.println("✓ User registered successfully: " + user.getEmail());
                return true;
            } else {
                System.out.println("✗ No rows affected when registering user");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("✗ Error registering user: " + e.getMessage());
            JOptionPane.showMessageDialog(null,
                    "Error registering user: " + e.getMessage() +
                            "\n\nTrying to fix table structure...",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);

            // Try to fix the table and retry
            recreateUsersTable();
            return registerUser(user, password); // Retry registration
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
                    // Simple password check for demo
                    if (simpleHash(password).equals(hashedPassword)) {
                        // Create user object with all fields including accessibility preferences
                        User user = new User(
                                rs.getString("name"),
                                rs.getString("surname"),
                                rs.getString("student_number"),
                                rs.getInt("age"),
                                rs.getString("gender"),
                                rs.getString("email")
                        );

                        // Set accessibility preferences if they exist in the database
                        try {
                            user.setDeviceType(rs.getString("device_type"));
                            user.setAvoidStairs(rs.getBoolean("avoid_stairs"));
                            user.setPreferRamps(rs.getBoolean("prefer_ramps"));
                            user.setMinPathWidthCm(rs.getInt("min_path_width_cm"));
                        } catch (SQLException e) {
                            System.out.println("Note: Some accessibility columns not found, using defaults");
                        }

                        return user;
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
            JOptionPane.showMessageDialog(null, "Error updating user preferences: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
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

    // Simple hash function for demonstration
    private static String simpleHash(String input) {
        // This is a simple hash for demonstration purposes only
        // In a real application, use a proper hashing algorithm like BCrypt
        return Integer.toString(input.hashCode());
    }
}