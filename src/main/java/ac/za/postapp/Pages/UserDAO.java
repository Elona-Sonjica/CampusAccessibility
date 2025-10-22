package ac.za.postapp.Pages;

import java.sql.*;
import java.time.LocalDateTime;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class UserDAO {

    private static final Logger logger = Logger.getLogger(UserDAO.class.getName());

    // SQL Constants (compatible with Java 11)
    private static final String CREATE_USERS_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS users (" +
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

    private static final String CREATE_SESSIONS_TABLE_SQL =
            "CREATE TABLE IF NOT EXISTS sessions (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT NOT NULL, " +
                    "session_token VARCHAR(255) UNIQUE NOT NULL, " +
                    "expires_at TIMESTAMP NOT NULL, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)";

    private static final String INSERT_USER_SQL =
            "INSERT INTO users (name, surname, student_number, age, gender, email, " +
                    "password_hash, device_type, avoid_stairs, prefer_ramps, min_path_width_cm) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_USER_BY_EMAIL_SQL = "SELECT * FROM users WHERE email = ?";
    private static final String UPDATE_USER_PREFERENCES_SQL =
            "UPDATE users SET device_type = ?, avoid_stairs = ?, " +
                    "prefer_ramps = ?, min_path_width_cm = ?, updated_at = ? " +
                    "WHERE email = ?";
    private static final String CHECK_EMAIL_EXISTS_SQL = "SELECT COUNT(*) FROM users WHERE email = ?";
    private static final String CHECK_STUDENT_NUMBER_EXISTS_SQL = "SELECT COUNT(*) FROM users WHERE student_number = ?";
    private static final String DELETE_USER_SQL = "DELETE FROM users WHERE email = ?";

    // Enhanced method to create the users table with all required columns
    public static void createUsersTable() {
        logger.info("Creating users table...");

        try (Connection conn = getValidConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(CREATE_USERS_TABLE_SQL);
            logger.info("✓ Users table created/verified successfully");

        } catch (SQLException e) {
            logger.severe("✗ Error creating users table: " + e.getMessage());
            // If table exists but missing columns, try to alter it
            alterUsersTable();
        }
    }

    // Method to add missing columns if table already exists
    private static void alterUsersTable() {
        logger.info("Attempting to alter users table...");

        String[] alterStatements = {
                "ALTER TABLE users ADD COLUMN device_type VARCHAR(50) DEFAULT 'None'",
                "ALTER TABLE users ADD COLUMN avoid_stairs BOOLEAN DEFAULT FALSE",
                "ALTER TABLE users ADD COLUMN prefer_ramps BOOLEAN DEFAULT FALSE",
                "ALTER TABLE users ADD COLUMN min_path_width_cm INT DEFAULT 90",
                "ALTER TABLE users ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
                "ALTER TABLE users ADD COLUMN updated_at TIMESTAMP NULL"
        };

        try (Connection conn = getValidConnection();
             Statement stmt = conn.createStatement()) {

            for (String alterSql : alterStatements) {
                try {
                    stmt.execute(alterSql);
                    logger.info("✓ Executed: " + alterSql);
                } catch (SQLException e) {
                    logger.warning("✗ Column may already exist: " + alterSql);
                }
            }

        } catch (SQLException e) {
            logger.severe("✗ Error altering users table: " + e.getMessage());
        }
    }

    // Method to drop and recreate the table (handles foreign key constraints)
    public static void recreateUsersTable() {
        logger.info("Recreating users table...");

        try (Connection conn = getValidConnection();
             Statement stmt = conn.createStatement()) {

            // First, disable foreign key checks
            stmt.execute("SET FOREIGN_KEY_CHECKS = 0");
            logger.info("✓ Disabled foreign key checks");

            // Drop sessions table first if it exists
            try {
                stmt.execute("DROP TABLE IF EXISTS sessions");
                logger.info("✓ Dropped sessions table");
            } catch (SQLException e) {
                logger.severe("✗ Error dropping sessions table: " + e.getMessage());
            }

            // Drop users table
            stmt.execute("DROP TABLE IF EXISTS users");
            logger.info("✓ Dropped users table");

            // Re-enable foreign key checks
            stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
            logger.info("✓ Re-enabled foreign key checks");

            // Create new table
            createUsersTable();

            // Recreate sessions table
            createSessionsTable();

        } catch (SQLException e) {
            logger.severe("✗ Error recreating users table: " + e.getMessage());
            // Make sure to re-enable foreign key checks even if error occurs
            try (Connection conn = getValidConnection();
                 Statement stmt = conn.createStatement()) {
                stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
            } catch (SQLException e2) {
                logger.severe("✗ Error re-enabling foreign key checks: " + e2.getMessage());
            }
        }
    }

    // Method to create sessions table
    public static void createSessionsTable() {
        logger.info("Creating sessions table...");

        try (Connection conn = getValidConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(CREATE_SESSIONS_TABLE_SQL);
            logger.info("✓ Sessions table created/verified successfully");

        } catch (SQLException e) {
            logger.severe("✗ Error creating sessions table: " + e.getMessage());
        }
    }

    public static boolean registerUser(User user, String password) {
        // Validate user data first
        if (!isValidUserData(user)) {
            JOptionPane.showMessageDialog(null,
                    "Invalid user data provided. Please check all fields.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // First, make sure the table has all required columns
        createUsersTable();

        try (Connection conn = getValidConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_USER_SQL)) {

            // Secure password hash
            String hashedPassword = hashPassword(password);

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
                logger.info("✓ User registered successfully: " + user.getEmail());
                return true;
            } else {
                logger.warning("✗ No rows affected when registering user");
                return false;
            }

        } catch (SQLException e) {
            logger.severe("✗ Error registering user: " + e.getMessage());
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
        try (Connection conn = getValidConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_USER_BY_EMAIL_SQL)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("password_hash");
                    // Secure password verification
                    if (verifyPassword(password, hashedPassword)) {
                        return extractUserFromResultSet(rs);
                    }
                }
            }

        } catch (SQLException e) {
            logger.severe("Error during login: " + e.getMessage());
        }

        return null;
    }

    public static boolean updateUserPreferences(User user) {
        try (Connection conn = getValidConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_USER_PREFERENCES_SQL)) {

            pstmt.setString(1, user.getDeviceType());
            pstmt.setBoolean(2, user.isAvoidStairs());
            pstmt.setBoolean(3, user.isPreferRamps());
            pstmt.setInt(4, user.getMinPathWidthCm());
            pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(6, user.getEmail());

            int affectedRows = pstmt.executeUpdate();
            boolean success = affectedRows > 0;

            if (success) {
                logger.info("✓ User preferences updated successfully for: " + user.getEmail());
            } else {
                logger.warning("✗ No user found with email: " + user.getEmail());
            }

            return success;

        } catch (SQLException e) {
            logger.severe("Error updating user preferences: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error updating user preferences: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static boolean emailExists(String email) {
        try (Connection conn = getValidConnection();
             PreparedStatement pstmt = conn.prepareStatement(CHECK_EMAIL_EXISTS_SQL)) {

            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            logger.severe("Error checking email: " + e.getMessage());
            return false;
        }
    }

    public static boolean studentNumberExists(String studentNumber) {
        try (Connection conn = getValidConnection();
             PreparedStatement pstmt = conn.prepareStatement(CHECK_STUDENT_NUMBER_EXISTS_SQL)) {

            pstmt.setString(1, studentNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            logger.severe("Error checking student number: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteUser(String email) {
        try (Connection conn = getValidConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_USER_SQL)) {

            pstmt.setString(1, email);
            int affectedRows = pstmt.executeUpdate();
            boolean success = affectedRows > 0;

            if (success) {
                logger.info("✓ User deleted successfully: " + email);
            } else {
                logger.warning("✗ No user found with email: " + email);
            }

            return success;

        } catch (SQLException e) {
            logger.severe("Error deleting user: " + e.getMessage());
            return false;
        }
    }

    public static User getUserByEmail(String email) {
        try (Connection conn = getValidConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_USER_BY_EMAIL_SQL)) {

            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error getting user: " + e.getMessage());
        }
        return null;
    }

    // Secure password hashing using SHA-256
    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    // Verify password against stored hash
    private static boolean verifyPassword(String password, String storedHash) {
        if (password == null || storedHash == null) return false;
        return hashPassword(password).equals(storedHash);
    }

    // Extract user from ResultSet
    private static User extractUserFromResultSet(ResultSet rs) throws SQLException {
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
            logger.warning("Note: Some accessibility columns not found, using defaults");
        }

        return user;
    }

    // Database connection with validation
    private static Connection getValidConnection() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null || conn.isClosed() || !conn.isValid(2)) {
            // Get a fresh connection if current one is invalid
            return DatabaseConnection.getConnection();
        }
        return conn;
    }

    // Input validation
    public static boolean isValidUserData(User user) {
        if (user == null) return false;

        return user.getName() != null && !user.getName().trim().isEmpty() &&
                user.getSurname() != null && !user.getSurname().trim().isEmpty() &&
                user.getStudentNumber() != null && !user.getStudentNumber().trim().isEmpty() &&
                user.getEmail() != null && !user.getEmail().trim().isEmpty() &&
                user.getGender() != null && !user.getGender().trim().isEmpty() &&
                user.getAge() > 0 && user.getAge() < 150;
    }

    // Batch operations for multiple users
    public static void batchUpdatePreferences(java.util.List<User> users) {
        try (Connection conn = getValidConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_USER_PREFERENCES_SQL)) {

            for (User user : users) {
                if (!isValidUserData(user)) {
                    logger.warning("Skipping invalid user data for: " + user.getEmail());
                    continue;
                }

                pstmt.setString(1, user.getDeviceType());
                pstmt.setBoolean(2, user.isAvoidStairs());
                pstmt.setBoolean(3, user.isPreferRamps());
                pstmt.setInt(4, user.getMinPathWidthCm());
                pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
                pstmt.setString(6, user.getEmail());
                pstmt.addBatch();
            }

            int[] results = pstmt.executeBatch();
            logger.info("✓ Batch update completed. " + results.length + " users processed.");

        } catch (SQLException e) {
            logger.severe("Error in batch update: " + e.getMessage());
        }
    }
}