package ac.za.postapp.Pages;

import javax.swing.*;
import java.awt.*;

public class ProfilePage extends JDialog {
    private User currentUser;
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField studentNumberField;
    private JTextField ageField;
    private JComboBox<String> genderComboBox;
    private JTextField emailField;
    private JTextField phoneField;
    private JComboBox<String> facultyComboBox; // Changed from JTextField to JComboBox<String>
    private JTextField courseField;
    private JTextArea bioArea;
    private JLabel avatarLabel;

    public ProfilePage(JFrame parent, User user) {
        super(parent, "User Profile", true);
        this.currentUser = user;
        setSize(700, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 247, 250));

        // Title
        JLabel titleLabel = new JLabel("👤 User Profile", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(52, 73, 94));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Content panel with tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));

        tabbedPane.addTab("Personal Info", createPersonalInfoPanel());
        tabbedPane.addTab("Academic Info", createAcademicInfoPanel());
        tabbedPane.addTab("Preferences", createPreferencesPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(245, 247, 250));

        JButton saveButton = new JButton("Save Profile");
        styleButton(saveButton, new Color(40, 167, 69));
        saveButton.addActionListener(e -> saveProfile());

        JButton cancelButton = new JButton("Cancel");
        styleButton(cancelButton, new Color(108, 117, 125));
        cancelButton.addActionListener(e -> dispose());

        JButton changeAvatarButton = new JButton("Change Avatar");
        styleButton(changeAvatarButton, new Color(70, 130, 180));
        changeAvatarButton.addActionListener(e -> changeAvatar());

        buttonPanel.add(changeAvatarButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadUserData();
    }

    private JPanel createPersonalInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Avatar section
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        avatarLabel = new JLabel("", SwingConstants.CENTER);
        avatarLabel.setPreferredSize(new Dimension(100, 100));
        avatarLabel.setOpaque(true);
        avatarLabel.setBackground(new Color(70, 130, 180));
        avatarLabel.setForeground(Color.WHITE);
        avatarLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        avatarLabel.setText(String.valueOf(currentUser.getName().charAt(0)) +
                String.valueOf(currentUser.getSurname().charAt(0)));
        avatarLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        panel.add(avatarLabel, gbc);

        // Personal information fields
        gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Surname:"), gbc);
        gbc.gridx = 1;
        surnameField = new JTextField(20);
        panel.add(surnameField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Student Number:"), gbc);
        gbc.gridx = 1;
        studentNumberField = new JTextField(20);
        studentNumberField.setEditable(false); // Cannot change student number
        panel.add(studentNumberField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        ageField = new JTextField(20);
        panel.add(ageField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        String[] genders = {"Male", "Female", "Other", "Prefer not to say"};
        genderComboBox = new JComboBox<>(genders);
        panel.add(genderComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(20);
        panel.add(phoneField, gbc);

        return panel;
    }

    private JPanel createAcademicInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Faculty:"), gbc);
        gbc.gridx = 1;
        String[] faculties = {"Engineering", "Health Sciences", "Business", "Education", "Applied Sciences", "Informatics & Design"};
        facultyComboBox = new JComboBox<>(faculties); // Now correctly declared as JComboBox<String>
        panel.add(facultyComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Course:"), gbc);
        gbc.gridx = 1;
        courseField = new JTextField(20);
        panel.add(courseField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Year of Study:"), gbc);
        gbc.gridx = 1;
        String[] years = {"1st Year", "2nd Year", "3rd Year", "4th Year", "Postgraduate"};
        JComboBox<String> yearComboBox = new JComboBox<>(years);
        panel.add(yearComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Student Type:"), gbc);
        gbc.gridx = 1;
        String[] types = {"Full-time", "Part-time", "Distance Learning", "Exchange"};
        JComboBox<String> typeComboBox = new JComboBox<>(types);
        panel.add(typeComboBox, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Expected Graduation:"), gbc);
        gbc.gridx = 1;
        String[] graduationYears = new String[5];
        int currentYear = java.time.Year.now().getValue();
        for (int i = 0; i < 5; i++) {
            graduationYears[i] = String.valueOf(currentYear + i);
        }
        JComboBox<String> graduationComboBox = new JComboBox<>(graduationYears);
        panel.add(graduationComboBox, gbc);

        return panel;
    }

    private JPanel createPreferencesPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Bio:"), gbc);
        gbc.gridx = 1;
        bioArea = new JTextArea(4, 20);
        bioArea.setLineWrap(true);
        bioArea.setWrapStyleWord(true);
        bioArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        JScrollPane bioScroll = new JScrollPane(bioArea);
        panel.add(bioScroll, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Notification Preferences:"), gbc);
        gbc.gridx = 1;
        JCheckBox emailNotifications = new JCheckBox("Email Notifications");
        emailNotifications.setSelected(true);
        panel.add(emailNotifications, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel(""), gbc);
        gbc.gridx = 1;
        JCheckBox smsNotifications = new JCheckBox("SMS Notifications");
        smsNotifications.setSelected(false);
        panel.add(smsNotifications, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Privacy Settings:"), gbc);
        gbc.gridx = 1;
        String[] privacyOptions = {"Public", "Friends Only", "Private"};
        JComboBox<String> privacyComboBox = new JComboBox<>(privacyOptions);
        panel.add(privacyComboBox, gbc);

        return panel;
    }

    private void loadUserData() {
        nameField.setText(currentUser.getName());
        surnameField.setText(currentUser.getSurname());
        studentNumberField.setText(currentUser.getStudentNumber());
        ageField.setText(String.valueOf(currentUser.getAge()));
        genderComboBox.setSelectedItem(currentUser.getGender());
        emailField.setText(currentUser.getEmail());

        // Set default values for other fields
        phoneField.setText("+27 123 456 789");
        facultyComboBox.setSelectedItem("Informatics & Design");
        courseField.setText("Applications Development");
        bioArea.setText("Computer Science student passionate about software development and AI.");
    }

    private void saveProfile() {
        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String ageStr = ageField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String faculty = (String) facultyComboBox.getSelectedItem(); // Now works correctly
        String course = courseField.getText().trim();

        // Validation
        if (name.isEmpty() || surname.isEmpty() || ageStr.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int age = Integer.parseInt(ageStr);
            if (age < 16 || age > 100) {
                JOptionPane.showMessageDialog(this, "Please enter a valid age (16-100)", "Error", JOptionPane.ERROR_MESSAGE);
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

        // Update user object
        currentUser.setName(name);
        currentUser.setSurname(surname);
        currentUser.setAge(Integer.parseInt(ageStr));
        currentUser.setGender((String) genderComboBox.getSelectedItem());
        currentUser.setEmail(email);

        // In a real app, you would save to database here

        JOptionPane.showMessageDialog(this,
                "Profile updated successfully!\n" +
                        "Name: " + name + " " + surname + "\n" +
                        "Faculty: " + faculty + "\n" +
                        "Course: " + course,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        dispose();
    }

    private void changeAvatar() {
        JOptionPane.showMessageDialog(this,
                "Avatar change functionality would open a file chooser to select an image.\n" +
                        "This feature is coming in the next update!",
                "Feature Preview",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            User testUser = new User("John", "Doe", "ST123456", 21, "Male", "john.doe@email.com");
            ProfilePage profile = new ProfilePage(frame, testUser);
            profile.setVisible(true);
        });
    }
}