package ac.za.postapp.Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Register extends JFrame {
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField studentNumberField;
    private JTextField ageField;
    private JComboBox<String> genderComboBox;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton backButton;

    public Register() {
        setTitle("Register - Campus Accessibility");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // ====== Main Panel ======
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        mainPanel.setBackground(new Color(245, 247, 250));

        // ====== Title ======
        JLabel title = new JLabel("📝 Campus Registration", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(52, 73, 94));
        mainPanel.add(title, BorderLayout.NORTH);

        // ====== Form Panel ======
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);

        // ====== Name ======
        addLabelAndField(formPanel, gbc, 0, "Name:", nameField = new JTextField(20));

        // ====== Surname ======
        addLabelAndField(formPanel, gbc, 1, "Surname:", surnameField = new JTextField(20));

        // ====== Student Number ======
        addLabelAndField(formPanel, gbc, 2, "Student Number:", studentNumberField = new JTextField(20));

        // ====== Age ======
        addLabelAndField(formPanel, gbc, 3, "Age:", ageField = new JTextField(20));

        // ====== Gender ======
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(labelFont);
        genderLabel.setForeground(new Color(52, 73, 94));
        formPanel.add(genderLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        String[] genders = {"Male", "Female", "Other"};
        genderComboBox = new JComboBox<>(genders);
        genderComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(genderComboBox, gbc);

        // ====== Email ======
        addLabelAndField(formPanel, gbc, 5, "Email:", emailField = new JTextField(20));

        // ====== Password ======
        gbc.gridx = 0; gbc.gridy = 6;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(labelFont);
        passwordLabel.setForeground(new Color(52, 73, 94));
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 6;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(passwordField, gbc);

        // ====== Confirm Password ======
        gbc.gridx = 0; gbc.gridy = 7;
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(labelFont);
        confirmPasswordLabel.setForeground(new Color(52, 73, 94));
        formPanel.add(confirmPasswordLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 7;
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formPanel.add(confirmPasswordField, gbc);

        // ====== Buttons ======
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(245, 247, 250));

        registerButton = new JButton("Register");
        styleButton(registerButton, new Color(40, 167, 69)); // Green

        backButton = new JButton("Back");
        styleButton(backButton, new Color(108, 117, 125)); // Grey

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openLogin();
            }
        });

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        // ====== Add panels to frame ======
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // Helper: Adds label and styled field to form panel
    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        label.setForeground(new Color(52, 73, 94));

        gbc.gridx = 0; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(label, gbc);

        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        gbc.gridx = 1; gbc.gridy = row;
        panel.add(field, gbc);
    }

    // Helper: Styles buttons with hover effect
    private void styleButton(JButton button, Color baseColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 40));
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor);
            }
        });
    }

    private void register() {
        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String studentNumber = studentNumberField.getText().trim();
        String ageStr = ageField.getText().trim();
        String gender = (String) genderComboBox.getSelectedItem();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

        // Validation
        if (name.isEmpty() || surname.isEmpty() || studentNumber.isEmpty() ||
                ageStr.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int age = Integer.parseInt(ageStr);
            if (age <= 0 || age > 120) {
                JOptionPane.showMessageDialog(this, "Please enter a valid age (1-120)", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age must be a number", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if email or student number already exists
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Check email
            String checkEmailSql = "SELECT COUNT(*) FROM users WHERE email = ?";
            PreparedStatement checkEmailStmt = conn.prepareStatement(checkEmailSql);
            checkEmailStmt.setString(1, email);
            ResultSet emailRs = checkEmailStmt.executeQuery();

            if (emailRs.next() && emailRs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Email already registered", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check student number
            String checkStudentSql = "SELECT COUNT(*) FROM users WHERE student_number = ?";
            PreparedStatement checkStudentStmt = conn.prepareStatement(checkStudentSql);
            checkStudentStmt.setString(1, studentNumber);
            ResultSet studentRs = checkStudentStmt.executeQuery();

            if (studentRs.next() && studentRs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Student number already registered", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Insert new user
            String insertSql = "INSERT INTO users (name, surname, student_number, age, gender, email, password_hash) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setString(1, name);
            insertStmt.setString(2, surname);
            insertStmt.setString(3, studentNumber);
            insertStmt.setInt(4, Integer.parseInt(ageStr));
            insertStmt.setString(5, gender);
            insertStmt.setString(6, email);
            insertStmt.setString(7, DatabaseConnection.hashPassword(password)); // Use proper hashing

            int rowsAffected = insertStmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                openLogin();
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // Simple password hashing (in a real application, use a proper hashing algorithm like BCrypt)
    private String hashPassword(String password) {
        // This is a simple hash for demonstration purposes
        return Integer.toString(password.hashCode());
    }

    private void openLogin() {
        this.dispose();
        Login login = new Login();
        login.setVisible(true);
    }

    public static void main(String[] args) {
        // Initialize database connection
        DatabaseConnection.initializeDatabase();

        SwingUtilities.invokeLater(() -> {
            Register register = new Register();
            register.setVisible(true);
        });
    }
}