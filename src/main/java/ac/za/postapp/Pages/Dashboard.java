package ac.za.postapp.Pages;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard extends JFrame {
    private JLabel nameLabel;
    private JLabel surnameLabel;
    private JLabel studentNumberLabel;
    private JLabel ageLabel;
    private JLabel genderLabel;
    private JLabel emailLabel;
    private JLabel deviceTypeLabel;
    private JLabel accessibilityLabel;
    private JButton logoutButton;

    private JComboBox<String> facultyComboBox;
    private JTextField courseField;
    private JTextField destinationField;

    // Enhanced buttons
    private JButton mapButton;
    private JButton eventsButton;
    private JButton accessibleEventsButton;
    private JButton profileButton;

    // Store current user
    private User currentUser;

    public Dashboard() {
        setTitle("Dashboard - CPUT Campus D6");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 750);
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
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(new Color(52, 73, 94));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Enhanced menu options - FIXED: All buttons now have proper actions
        JButton scheduleButton = createMenuButton("Schedule");
        scheduleButton.addActionListener(e -> openSchedule());
        panel.add(scheduleButton);

        JButton notificationsButton = createMenuButton("Notifications");
        notificationsButton.addActionListener(e -> openNotifications());
        panel.add(notificationsButton);

        JButton settingsButton = createMenuButton("Settings");
        settingsButton.addActionListener(e -> openSettings());
        panel.add(settingsButton);

        JButton facilitiesButton = createMenuButton("Facilities");
        facilitiesButton.addActionListener(e -> openFacilities());
        panel.add(facilitiesButton);

        // Enhanced functionality buttons
        mapButton = createMenuButton("Campus Map");
        mapButton.addActionListener(e -> openInteractiveMap());
        panel.add(mapButton);

        eventsButton = createMenuButton("Manage Events");
        eventsButton.addActionListener(e -> openEventManager());
        panel.add(eventsButton);

        accessibleEventsButton = createMenuButton("Find Events");
        accessibleEventsButton.addActionListener(e -> openEventBrowser());
        panel.add(accessibleEventsButton);

        profileButton = createMenuButton("My Profile");
        profileButton.addActionListener(e -> openProfile());
        panel.add(profileButton);

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
        JLabel title = new JLabel("📋 Student Dashboard - Enhanced", SwingConstants.CENTER);
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

        // ==== Accessibility Info Section ====
        JPanel accessibilityPanel = createAccessibilityPanel(labelFont, valueFont);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(accessibilityPanel, gbc);
        gbc.gridwidth = 1;

        // ==== Navigation Section ====
        JPanel navigationPanel = createNavigationPanel(labelFont);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
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

    private JPanel createAccessibilityPanel(Font labelFont, Font valueFont) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Accessibility Preferences"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        // Device Type
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("Mobility Device:", labelFont), gbc);
        gbc.gridx = 1;
        deviceTypeLabel = createValueLabel(valueFont);
        panel.add(deviceTypeLabel, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("Accessibility Needs:", labelFont), gbc);
        gbc.gridx = 1;
        accessibilityLabel = createValueLabel(valueFont);
        panel.add(accessibilityLabel, gbc);

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
        String[] faculties = {"Engineering", "Health Sciences", "Business", "Education", "Applied Sciences", "Informatics and Design"};
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

        // Quick Navigation Buttons - FIXED STYLING
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JPanel quickNavPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        quickNavPanel.setBackground(Color.WHITE);

        String[] quickLocations = {"Entry/Exit", "Lab 1.19", "Lab 1.11", "Toilets", "Library", "Cafeteria"};
        for (String location : quickLocations) {
            JButton btn = new JButton(location);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btn.setBackground(new Color(70, 130, 180));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

            // Add hover effect
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(30, 144, 255));
                    btn.setForeground(Color.WHITE);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(70, 130, 180));
                    btn.setForeground(Color.WHITE);
                }
            });

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

        // Home button - FIXED STYLING
        JButton homeButton = new JButton("Home");
        homeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        homeButton.setBackground(new Color(52, 73, 94));
        homeButton.setForeground(Color.WHITE);
        homeButton.setFocusPainted(false);
        homeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        // Add hover effect to home button
        homeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                homeButton.setBackground(new Color(30, 50, 70));
                homeButton.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                homeButton.setBackground(new Color(52, 73, 94));
                homeButton.setForeground(Color.WHITE);
            }
        });

        homeButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Already on home page!"));

        // Logout button - FIXED STYLING
        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        // Add hover effect to logout button
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(200, 35, 51));
                logoutButton.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(220, 53, 69));
                logoutButton.setForeground(Color.WHITE);
            }
        });

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
                btn.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(70, 130, 180));
                btn.setForeground(Color.WHITE);
            }
        });

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

    // ===== INTEGRATED PAGES METHODS - FIXED =====

    private void openSchedule() {
        // Create and show SchedulePage if you have it, otherwise show message
        try {
            // If you have a SchedulePage class:
            // SchedulePage schedule = new SchedulePage(this);
            // schedule.setVisible(true);
            JOptionPane.showMessageDialog(this,
                    "Schedule feature coming soon!\n\n" +
                            "This will display your class timetable and academic schedule.",
                    "Schedule",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Schedule feature is under development", "Coming Soon", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void openNotifications() {
        try {
            // If you have a NotificationsPage class:
            // NotificationsPage notifications = new NotificationsPage(this);
            // notifications.setVisible(true);
            JOptionPane.showMessageDialog(this,
                    "Notifications Center\n\n" +
                            "• New assignment in AppDev\n" +
                            "• Class cancelled: Project Management\n" +
                            "• Campus event tomorrow",
                    "Notifications",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Notifications feature is under development", "Coming Soon", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void openSettings() {
        try {
            // If you have a SettingsPage class:
            // SettingsPage settings = new SettingsPage(this);
            // settings.setVisible(true);
            JOptionPane.showMessageDialog(this,
                    "Application Settings\n\n" +
                            "• Theme: Light\n" +
                            "• Notifications: Enabled\n" +
                            "• Accessibility: Custom\n" +
                            "• Language: English",
                    "Settings",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Settings feature is under development", "Coming Soon", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void openFacilities() {
        try {
            // If you have a FacultyPage class:
            // FacultyPage facilities = new FacultyPage();
            // facilities.setVisible(true);
            JOptionPane.showMessageDialog(this,
                    "Campus Facilities\n\n" +
                            "🏛️  Engineering Building\n" +
                            "🏥  Health Sciences\n" +
                            "💼  Business School\n" +
                            "🎓  Education Faculty\n" +
                            "🔬  Applied Sciences\n" +
                            "💻  Informatics & Design",
                    "Campus Facilities",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Facilities feature is under development", "Coming Soon", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void openProfile() {
        if (currentUser != null) {
            // Open the actual ProfilePage if available
            try {
                // If you have a ProfilePage class:
                // ProfilePage profile = new ProfilePage(this, currentUser);
                // profile.setVisible(true);

                // For now, show user info in dialog
                String profileInfo = String.format(
                        "👤 User Profile\n\n" +
                                "Name: %s %s\n" +
                                "Student Number: %s\n" +
                                "Email: %s\n" +
                                "Age: %d\n" +
                                "Gender: %s\n\n" +
                                "Accessibility:\n" +
                                "• Device: %s\n" +
                                "• Avoid Stairs: %s\n" +
                                "• Prefer Ramps: %s\n" +
                                "• Min Path Width: %d cm",
                        currentUser.getName(), currentUser.getSurname(),
                        currentUser.getStudentNumber(), currentUser.getEmail(),
                        currentUser.getAge(), currentUser.getGender(),
                        currentUser.getDeviceType(),
                        currentUser.isAvoidStairs() ? "Yes" : "No",
                        currentUser.isPreferRamps() ? "Yes" : "No",
                        currentUser.getMinPathWidthCm()
                );

                JOptionPane.showMessageDialog(this, profileInfo, "My Profile", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error opening profile: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "User data not available", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openInteractiveMap() {
        try {
            JOptionPane.showMessageDialog(this,
                    "🏫 Campus Map\n\n" +
                            "Opening interactive campus navigation...\n" +
                            "This will show:\n" +
                            "• Building locations\n" +
                            "• Accessible routes\n" +
                            "• Your current position\n" +
                            "• Navigation to destinations",
                    "Campus Map",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Map feature is under development", "Coming Soon", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void openEventManager() {
        try {
            // Open the EventGUI for staff/lecturers to manage events
            new EventGUI().setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error opening Event Manager: " + e.getMessage() +
                            "\n\nMake sure EventGUI class is available.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openEventBrowser() {
        try {
            // Open the AvailableEventsGUI for students to browse events
            new AvailableEventsGUI().setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error opening Event Browser: " + e.getMessage() +
                            "\n\nMake sure AvailableEventsGUI class is available.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openInteractiveMapWithDestination(String destination) {
        JOptionPane.showMessageDialog(this,
                "🗺️  Navigation Started\n\n" +
                        "Destination: " + destination + "\n" +
                        "Calculating most accessible route...\n" +
                        "Considering your preferences:\n" +
                        "• " + currentUser.getDeviceType() + "\n" +
                        "• " + (currentUser.isAvoidStairs() ? "Avoiding stairs" : "No stair restrictions") + "\n" +
                        "• Minimum width: " + currentUser.getMinPathWidthCm() + " cm",
                "Navigation to " + destination,
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ===== USER MANAGEMENT METHODS =====

    public void setUserData(User user) {
        this.currentUser = user;
        if (nameLabel != null) nameLabel.setText(user.getName());
        if (surnameLabel != null) surnameLabel.setText(user.getSurname());
        if (studentNumberLabel != null) studentNumberLabel.setText(user.getStudentNumber());
        if (ageLabel != null) ageLabel.setText(String.valueOf(user.getAge()));
        if (genderLabel != null) genderLabel.setText(user.getGender());
        if (emailLabel != null) emailLabel.setText(user.getEmail());

        // Set accessibility information
        if (deviceTypeLabel != null) deviceTypeLabel.setText(user.getDeviceType());

        // Build accessibility needs string
        if (accessibilityLabel != null) {
            StringBuilder accessibilityNeeds = new StringBuilder();
            if (user.isAvoidStairs()) accessibilityNeeds.append("Avoid Stairs");
            if (user.isPreferRamps()) {
                if (accessibilityNeeds.length() > 0) accessibilityNeeds.append(", ");
                accessibilityNeeds.append("Prefer Ramps");
            }
            if (user.getMinPathWidthCm() != null && user.getMinPathWidthCm() > 90) {
                if (accessibilityNeeds.length() > 0) accessibilityNeeds.append(", ");
                accessibilityNeeds.append("Min Width: ").append(user.getMinPathWidthCm()).append("cm");
            }

            if (accessibilityNeeds.length() == 0) {
                accessibilityNeeds.append("Standard Accessibility");
            }
            accessibilityLabel.setText(accessibilityNeeds.toString());
        }

        // Set default values for navigation fields
        if (courseField != null) {
            courseField.setText("Applications Development");
        }
        if (facultyComboBox != null) {
            facultyComboBox.setSelectedItem("Informatics and Design");
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new Login().setVisible(true);
        }
    }

    // ===== MAIN METHOD FOR TESTING =====

    public static void main(String[] args) {
        // Initialize database
        DatabaseConnection.initializeDatabase();
        UserDAO.createUsersTable();

        SwingUtilities.invokeLater(() -> {
            Dashboard dashboard = new Dashboard();

            // Test with sample user data including accessibility preferences
            User testUser = new User("John", "Doe", "ST123456", 21, "Male",
                    "john.doe@email.com", "Wheelchair", true, true, 120);
            dashboard.setUserData(testUser);

            dashboard.setVisible(true);
        });
    }
}