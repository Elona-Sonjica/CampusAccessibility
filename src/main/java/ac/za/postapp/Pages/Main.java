package ac.za.postapp.Pages;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Campus Accessibility Application...");

        // Initialize database only - tables will be created when needed
        DatabaseConnection.initializeDatabase();
        UserDAO.createUsersTable();

        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error setting look and feel: " + e.getMessage());
        }

        // Start the application
        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.setVisible(true);
        });
    }
}