package ac.za.postapp.Pages;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Register extends JFrame {
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField studentNumberField;
    private JTextField ageField;
    private JComboBox<String> genderComboBox;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<String> deviceTypeComboBox;
    private JCheckBox avoidStairsCheckBox;
    private JCheckBox preferRampsCheckBox;
    private JTextField minWidthField;
    private JButton registerButton;
    private JButton backButton;

    public Register() {
        setTitle("Register - Campus Accessibility");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 700);
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

        // ====== Form Panel with Tabs ======
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));

        tabbedPane.addTab("Personal Info", createPersonalInfoPanel());
        tabbedPane.addTab("Accessibility", createAccessibilityPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // ====== Buttons ======
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(245, 247, 250));

        registerButton = new JButton("Register");
        styleButton(registerButton, new Color(40, 167, 69));

        backButton = new JButton("Back to Login");
        styleButton(backButton, new Color(108, 117, 125));

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

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createPersonalInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);

        // Name
        addLabelAndField(panel, gbc, 0, "Name:", nameField = new JTextField(20));

        // Surname
        addLabelAndField(panel, gbc, 1, "Surname:", surnameField = new JTextField(20));

        // Student Number
        addLabelAndField(panel, gbc, 2, "Student Number:", studentNumberField = new JTextField(20));

        // Age
        addLabelAndField(panel, gbc, 3, "Age:", ageField = new JTextField(20));

        // Gender
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(labelFont);
        genderLabel.setForeground(new Color(52, 73, 94));
        panel.add(genderLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        String[] genders = {"Male", "Female", "Other", "Prefer not to say"};
        genderComboBox = new JComboBox<>(genders);
        genderComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(genderComboBox, gbc);

        // Email
        addLabelAndField(panel, gbc, 5, "Email:", emailField = new JTextField(20));

        // Password
        gbc.gridx = 0; gbc.gridy = 6;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(labelFont);
        passwordLabel.setForeground(new Color(52, 73, 94));
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 6;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        panel.add(passwordField, gbc);

        // Confirm Password
        gbc.gridx = 0; gbc.gridy = 7;
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(labelFont);
        confirmPasswordLabel.setForeground(new Color(52, 73, 94));
        panel.add(confirmPasswordLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 7;
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        panel.add(confirmPasswordField, gbc);

        return panel;
    }

    private JPanel createAccessibilityPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);

        // Device Type
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel deviceLabel = new JLabel("Mobility Device:");
        deviceLabel.setFont(labelFont);
        deviceLabel.setForeground(new Color(52, 73, 94));
        panel.add(deviceLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        String[] devices = {"None", "Wheelchair", "Walker", "Crutches", "Cane", "Service Animal"};
        deviceTypeComboBox = new JComboBox<>(devices);
        deviceTypeComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(deviceTypeComboBox, gbc);

        // Accessibility Preferences
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        JLabel prefLabel = new JLabel("Accessibility Preferences:");
        prefLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        prefLabel.setForeground(new Color(52, 73, 94));
        panel.add(prefLabel, gbc);

        gbc.gridy = 2; gbc.gridwidth = 2;
        avoidStairsCheckBox = new JCheckBox("Avoid Stairs (I need elevator access)");
        avoidStairsCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        avoidStairsCheckBox.setBackground(Color.WHITE);
        panel.add(avoidStairsCheckBox, gbc);

        gbc.gridy = 3; gbc.gridwidth = 2;
        preferRampsCheckBox = new JCheckBox("Prefer Ramps (Instead of stairs when available)");
        preferRampsCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        preferRampsCheckBox.setBackground(Color.WHITE);
        panel.add(preferRampsCheckBox, gbc);

        // Minimum Path Width
        gbc.gridy = 4; gbc.gridwidth = 1;
        gbc.gridx = 0;
        JLabel widthLabel = new JLabel("Minimum Path Width (cm):");
        widthLabel.setFont(labelFont);
        widthLabel.setForeground(new Color(52, 73, 94));
        panel.add(widthLabel, gbc);

        gbc.gridx = 1;
        minWidthField = new JTextField("90");
        minWidthField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        minWidthField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        panel.add(minWidthField, gbc);

        // Info text
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        JLabel infoLabel = new JLabel("<html><i>These preferences will help us find the most accessible routes for you.</i></html>");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        infoLabel.setForeground(Color.GRAY);
        panel.add(infoLabel, gbc);

        return panel;
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        label.setForeground(new Color(52, 73, 94));

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(label, gbc);

        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        gbc.gridx = 1;
        gbc.gridy = row;
        panel.add(field, gbc);
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(Color.BLACK); // Explicitly set text to white
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 40));
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
                button.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
                button.setForeground(Color.WHITE);
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
        String deviceType = (String) deviceTypeComboBox.getSelectedItem();
        boolean avoidStairs = avoidStairsCheckBox.isSelected();
        boolean preferRamps = preferRampsCheckBox.isSelected();
        String minWidthStr = minWidthField.getText().trim();

        // Validation
        if (name.isEmpty() || surname.isEmpty() || studentNumber.isEmpty() ||
                ageStr.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields", "Error", JOptionPane.ERROR_MESSAGE);
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

        int age;
        try {
            age = Integer.parseInt(ageStr);
            if (age <= 0 || age > 120) {
                JOptionPane.showMessageDialog(this, "Please enter a valid age (1-120)", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age must be a number", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int minWidth;
        try {
            minWidth = Integer.parseInt(minWidthStr);
            if (minWidth < 60 || minWidth > 200) {
                JOptionPane.showMessageDialog(this, "Please enter a valid path width (60-200 cm)", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Path width must be a number", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if email or student number already exists
        if (UserDAO.emailExists(email)) {
            JOptionPane.showMessageDialog(this, "Email already registered", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (UserDAO.studentNumberExists(studentNumber)) {
            JOptionPane.showMessageDialog(this, "Student number already registered", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create user with accessibility preferences
        User user = new User(name, surname, studentNumber, age, gender, email,
                deviceType, avoidStairs, preferRamps, minWidth);

        // Register user
        if (UserDAO.registerUser(user, password)) {
            JOptionPane.showMessageDialog(this,
                    "Registration successful!\n\n" +
                            "Welcome " + name + " " + surname + "!\n" +
                            "Student Number: " + studentNumber + "\n" +
                            "Accessibility: " + deviceType +
                            (avoidStairs ? ", Avoids Stairs" : "") +
                            (preferRamps ? ", Prefers Ramps" : ""),
                    "Registration Successful",
                    JOptionPane.INFORMATION_MESSAGE);
            openLogin();
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openLogin() {
        this.dispose();
        Login login = new Login();
        login.setVisible(true);
    }

    public static void main(String[] args) {
        // Initialize database
        DatabaseConnection.initializeDatabase();
        UserDAO.createUsersTable();

        SwingUtilities.invokeLater(() -> {
            Register register = new Register();
            register.setVisible(true);
        });
    }
}