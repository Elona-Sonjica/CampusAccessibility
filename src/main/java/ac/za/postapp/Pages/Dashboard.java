package ac.za.postapp.Pages;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {
    private JLabel nameLabel;
    private JLabel surnameLabel;
    private JLabel studentNumberLabel;
    private JLabel ageLabel;
    private JLabel genderLabel;
    private JLabel emailLabel;
    private JButton logoutButton;

    private JComboBox<String> facultyComboBox;
    private JTextField courseField;
    private JTextField destinationField;

    // Map button
    private JButton mapButton;

    // Store current user
    private User currentUser;

    public Dashboard() {
        setTitle("Dashboard - CPUT Campus D6");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        // ===== Main Layout =====
        setLayout(new BorderLayout());

        // ===== Top Menu Panel =====
        JPanel topMenuPanel = createTopMenuPanel();
        add(topMenuPanel, BorderLayout.NORTH);

        // ===== Scrollable Main Content Panel =====
        JPanel contentPanel = createContentPanel();
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // ===== Bottom Panel with Logout =====
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createTopMenuPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setBackground(new Color(52, 73, 94));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        String[] topOptions = {"Schedule", "Notifications", "Settings", "Profile" , "Facilities"};
        for (String option : topOptions) {
            JButton btn = createMenuButton(option);
            panel.add(btn);
        }

        // Map button - ADDED TO TOP MENU
        mapButton = createMenuButton("Campus Map");
        mapButton.addActionListener(e -> openInteractiveMap());
        panel.add(mapButton);

        return panel;
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(245, 247, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font valueFont = new Font("Segoe UI", Font.PLAIN, 14);

        // ==== Title ====
        JLabel title = new JLabel("📋 Student Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(52, 73, 94));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);
        gbc.gridwidth = 1;

        // ==== User Info Section ====
        JPanel userInfoPanel = createUserInfoPanel(labelFont, valueFont);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        panel.add(userInfoPanel, gbc);
        gbc.gridwidth = 1;

        // ==== Navigation Section ====
        JPanel navigationPanel = createNavigationPanel(labelFont);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(navigationPanel, gbc);

        return panel;
    }

    private JPanel createUserInfoPanel(Font labelFont, Font valueFont) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Personal Information"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        // Name
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("Name:", labelFont), gbc);
        gbc.gridx = 1;
        nameLabel = createValueLabel(valueFont);
        panel.add(nameLabel, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("Surname:", labelFont), gbc);
        gbc.gridx = 1;
        surnameLabel = createValueLabel(valueFont);
        panel.add(surnameLabel, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("Student Number:", labelFont), gbc);
        gbc.gridx = 1;
        studentNumberLabel = createValueLabel(valueFont);
        panel.add(studentNumberLabel, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("Age:", labelFont), gbc);
        gbc.gridx = 1;
        ageLabel = createValueLabel(valueFont);
        panel.add(ageLabel, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("Gender:", labelFont), gbc);
        gbc.gridx = 1;
        genderLabel = createValueLabel(valueFont);
        panel.add(genderLabel, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("Email:", labelFont), gbc);
        gbc.gridx = 1;
        emailLabel = createValueLabel(valueFont);
        panel.add(emailLabel, gbc);

        return panel;
    }

    private JPanel createNavigationPanel(Font labelFont) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Campus Navigation"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        // Faculty Dropdown
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("Faculty:", labelFont), gbc);
        gbc.gridx = 1;
        String[] faculties = {"Engineering", "Health Sciences", "Business", "Education", "Applied Sciences" ,  "informatics and Design"};
        facultyComboBox = new JComboBox<>(faculties);
        facultyComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(facultyComboBox, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("Course:", labelFont), gbc);
        gbc.gridx = 1;
        courseField = new JTextField();
        courseField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        courseField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        panel.add(courseField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("Destination:", labelFont), gbc);
        gbc.gridx = 1;
        destinationField = new JTextField();
        destinationField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        destinationField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        panel.add(destinationField, gbc);

        // Quick Navigation Buttons
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JPanel quickNavPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        quickNavPanel.setBackground(Color.WHITE);

        String[] quickLocations = {"Entry/Exit", "Lab 1.19", "Lab 1.11", "Toilets"};
        for (String location : quickLocations) {
            JButton btn = new JButton(location);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btn.setBackground(new Color(70, 130, 180));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> {
                destinationField.setText(location);
                openInteractiveMapWithDestination(location);
            });
            quickNavPanel.add(btn);
        }
        panel.add(quickNavPanel, gbc);

        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Home button
        JButton homeButton = new JButton("Home");
        homeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        homeButton.setBackground(new Color(52, 73, 94));
        homeButton.setForeground(Color.WHITE);
        homeButton.setFocusPainted(false);
        homeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Already on home page!"));

        // Logout button
        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> logout());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setBackground(new Color(245, 247, 250));
        buttonPanel.add(homeButton);
        buttonPanel.add(logoutButton);

        panel.add(buttonPanel, BorderLayout.EAST);

        return panel;
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(new Color(70, 130, 180));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        // Add hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(30, 144, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(70, 130, 180));
            }
        });

        // Add appropriate actions
        switch (text) {
            case "Schedule":
                btn.addActionListener(e -> openSchedule());
                break;
            case "Notifications":
                btn.addActionListener(e -> openNotifications());
                break;
            case "Settings":
                btn.addActionListener(e -> openSettings());
                break;
            case "Profile":
                btn.addActionListener(e -> openProfile());
                break;
            case "Facilities":
                btn.addActionListener(e -> openFacilities());
                break;
            case "Campus Map":
                btn.addActionListener(e -> openInteractiveMap());
                break;
        }

        return btn;
    }

    private JLabel createStyledLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(new Color(52, 73, 94));
        return label;
    }

    private JLabel createValueLabel(Font font) {
        JLabel label = new JLabel("N/A");
        label.setFont(font);
        label.setForeground(new Color(33, 37, 41));
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return label;
    }

    // ===== INTEGRATED PAGES METHODS =====

    private void openSchedule() {
        SchedulePage schedule = new SchedulePage(this);
        schedule.setVisible(true);
    }

    private void openNotifications() {
        NotificationsPage notifications = new NotificationsPage(this);
        notifications.setVisible(true);
    }

    private void openSettings() {
        SettingsPage settings = new SettingsPage(this);
        settings.setVisible(true);
    }

    private void openProfile() {
        if (currentUser != null) {
            ProfilePage profile = new ProfilePage(this, currentUser);
            profile.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "User data not available", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void openFacilities() {
        FacultyPage facilities = new FacultyPage();
        facilities.setVisible(true);
    }

    private void openInteractiveMap() {
        JFrame mapFrame = new JFrame("Interactive Campus Navigation");
        mapFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mapFrame.setSize(900, 800);
        mapFrame.setLocationRelativeTo(this);

        MapPanel mapPanel = new MapPanel();
        mapFrame.add(mapPanel);

        mapFrame.setVisible(true);
    }

    private void openInteractiveMapWithDestination(String destination) {
        JFrame mapFrame = new JFrame("Navigation to: " + destination);
        mapFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mapFrame.setSize(900, 800);
        mapFrame.setLocationRelativeTo(this);

        MapPanel mapPanel = new MapPanel();

        // Pre-select the destination in the map panel
        try {
            mapPanel.setDestination(destination);
        } catch (Exception e) {
            System.out.println("Could not pre-select destination: " + e.getMessage());
        }

        mapFrame.add(mapPanel);
        mapFrame.setVisible(true);
    }

    // ===== USER MANAGEMENT METHODS =====

    public void setUserData(User user) {
        this.currentUser = user;
        nameLabel.setText(user.getName());
        surnameLabel.setText(user.getSurname());
        studentNumberLabel.setText(user.getStudentNumber());
        ageLabel.setText(String.valueOf(user.getAge()));
        genderLabel.setText(user.getGender());
        emailLabel.setText(user.getEmail());

        // Set default values for navigation fields
        if (courseField != null) {
            courseField.setText("Applications Development");
        }
        if (facultyComboBox != null) {
            facultyComboBox.setSelectedItem("informatics and Design");
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            Login login = new Login();
            login.setVisible(true);
        }
    }

    // ===== MAIN METHOD FOR TESTING =====

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Dashboard dashboard = new Dashboard();

            // Test with sample user data
            User testUser = new User("John", "Doe", "ST123456", 21, "Male", "john.doe@email.com");
            dashboard.setUserData(testUser);

            dashboard.setVisible(true);
        });
    }
}