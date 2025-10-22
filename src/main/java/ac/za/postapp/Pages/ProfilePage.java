package ac.za.postapp.Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

/**
 * Elona Sonjica
 */

public class ProfilePage extends JDialog {
    private User currentUser;
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField studentNumberField;
    private JTextField ageField;
    private JComboBox<String> genderComboBox;
    private JTextField emailField;
    private JTextField phoneField;
    private JComboBox<String> facultyComboBox;
    private JTextField courseField;
    private JTextArea bioArea;
    private JLabel avatarLabel;
    private JFrame parentFrame;

    public ProfilePage(JFrame parent, User user) {
        super(parent, "👤 User Profile - Campus Access Guide", true);
        this.currentUser = user;
        this.parentFrame = parent;
        setSize(900, 750); // Increased size to accommodate all fields
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        setResizable(true); // Changed to true to allow resizing

        // Main panel with gradient background
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(245, 247, 250),
                        getWidth(), getHeight(), new Color(220, 230, 240)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Content panel with tabs
        JTabbedPane tabbedPane = createTabbedPane();
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadUserData();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(74, 107, 136),
                        getWidth(), getHeight(), new Color(33, 64, 98)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Back button
        JButton backButton = createModernButton("← Back", new Color(108, 117, 125));
        backButton.addActionListener(e -> dispose());

        // Title
        JLabel titleLabel = new JLabel("👤 User Profile", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);

        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titlePanel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JTabbedPane createTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(new Color(245, 247, 250));

        // Create tab panels with glass morphism effect
        tabbedPane.addTab("📝 Personal Info", createPersonalInfoPanel());
        tabbedPane.addTab("🎓 Academic Info", createAcademicInfoPanel());
        tabbedPane.addTab("⚙️ Preferences", createPreferencesPanel());

        return tabbedPane;
    }

    private JPanel createPersonalInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setOpaque(false);

        // Avatar section
        JPanel avatarPanel = createAvatarPanel();
        panel.add(avatarPanel, BorderLayout.NORTH);

        // Form section with scroll pane to ensure all fields are visible
        JScrollPane formScrollPane = new JScrollPane(createPersonalInfoForm());
        formScrollPane.setBorder(BorderFactory.createEmptyBorder());
        formScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        formScrollPane.setOpaque(false);
        formScrollPane.getViewport().setOpaque(false);
        panel.add(formScrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAvatarPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        avatarLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw circular background
                g2d.setColor(new Color(70, 130, 180));
                g2d.fillOval(0, 0, getWidth(), getHeight());

                // Draw text
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 36));
                FontMetrics fm = g2d.getFontMetrics();
                String text = String.valueOf(currentUser.getName().charAt(0)) +
                        String.valueOf(currentUser.getSurname().charAt(0));
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(text, x, y);

                g2d.dispose();
            }
        };
        avatarLabel.setPreferredSize(new Dimension(120, 120));
        avatarLabel.setOpaque(false);

        panel.add(avatarLabel);

        return panel;
    }

    private JPanel createPersonalInfoForm() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(255, 255, 255, 230));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2d.setColor(new Color(255, 255, 255, 150));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 20, 20);

                g2d.dispose();
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12); // Reduced insets to fit more content
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Color labelColor = new Color(52, 73, 94);

        int row = 0;

        // Name
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("👤 Name:", labelFont, labelColor), gbc);
        gbc.gridx = 1;
        nameField = createStyledTextField();
        panel.add(nameField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("👥 Surname:", labelFont, labelColor), gbc);
        gbc.gridx = 1;
        surnameField = createStyledTextField();
        panel.add(surnameField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("🎫 Student Number:", labelFont, labelColor), gbc);
        gbc.gridx = 1;
        studentNumberField = createStyledTextField();
        studentNumberField.setEditable(false);
        studentNumberField.setBackground(new Color(240, 240, 240));
        panel.add(studentNumberField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("🎂 Age:", labelFont, labelColor), gbc);
        gbc.gridx = 1;
        ageField = createStyledTextField();
        panel.add(ageField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("⚧ Gender:", labelFont, labelColor), gbc);
        gbc.gridx = 1;
        String[] genders = {"Male", "Female", "Other", "Prefer not to say"};
        genderComboBox = createStyledComboBox(genders);
        panel.add(genderComboBox, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("📧 Email:", labelFont, labelColor), gbc);
        gbc.gridx = 1;
        emailField = createStyledTextField();
        panel.add(emailField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("📱 Phone:", labelFont, labelColor), gbc);
        gbc.gridx = 1;
        phoneField = createStyledTextField();
        panel.add(phoneField, gbc);

        // Add some empty space at the bottom
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(Box.createVerticalGlue(), gbc);

        return panel;
    }

    private JPanel createAcademicInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(255, 255, 255, 230));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2d.setColor(new Color(255, 255, 255, 150));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 20, 20);

                g2d.dispose();
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Color labelColor = new Color(52, 73, 94);

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("🏛️ Faculty:", labelFont, labelColor), gbc);
        gbc.gridx = 1;
        String[] faculties = {"Engineering", "Health Sciences", "Business", "Education", "Applied Sciences", "Informatics & Design"};
        facultyComboBox = createStyledComboBox(faculties);
        panel.add(facultyComboBox, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("📚 Course:", labelFont, labelColor), gbc);
        gbc.gridx = 1;
        courseField = createStyledTextField();
        panel.add(courseField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("🎓 Year of Study:", labelFont, labelColor), gbc);
        gbc.gridx = 1;
        String[] years = {"1st Year", "2nd Year", "3rd Year", "4th Year", "Postgraduate"};
        JComboBox<String> yearComboBox = createStyledComboBox(years);
        panel.add(yearComboBox, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("🎯 Student Type:", labelFont, labelColor), gbc);
        gbc.gridx = 1;
        String[] types = {"Full-time", "Part-time", "Distance Learning", "Exchange"};
        JComboBox<String> typeComboBox = createStyledComboBox(types);
        panel.add(typeComboBox, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("🎓 Expected Graduation:", labelFont, labelColor), gbc);
        gbc.gridx = 1;
        String[] graduationYears = new String[5];
        int currentYear = java.time.Year.now().getValue();
        for (int i = 0; i < 5; i++) {
            graduationYears[i] = String.valueOf(currentYear + i);
        }
        JComboBox<String> graduationComboBox = createStyledComboBox(graduationYears);
        panel.add(graduationComboBox, gbc);

        // Add empty space
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(Box.createVerticalGlue(), gbc);

        return panel;
    }

    private JPanel createPreferencesPanel() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(255, 255, 255, 230));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2d.setColor(new Color(255, 255, 255, 150));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 20, 20);

                g2d.dispose();
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Color labelColor = new Color(52, 73, 94);

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("📝 Bio:", labelFont, labelColor), gbc);
        gbc.gridx = 1;
        bioArea = new JTextArea(4, 20);
        bioArea.setLineWrap(true);
        bioArea.setWrapStyleWord(true);
        bioArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        bioArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        JScrollPane bioScroll = new JScrollPane(bioArea);
        bioScroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(bioScroll, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("🔔 Notification Preferences:", labelFont, labelColor), gbc);
        gbc.gridx = 1;
        JPanel notificationPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        notificationPanel.setOpaque(false);

        JCheckBox emailNotifications = createStyledCheckBox("📧 Email Notifications", true);
        JCheckBox smsNotifications = createStyledCheckBox("💬 SMS Notifications", false);

        notificationPanel.add(emailNotifications);
        notificationPanel.add(smsNotifications);
        panel.add(notificationPanel, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("🔒 Privacy Settings:", labelFont, labelColor), gbc);
        gbc.gridx = 1;
        String[] privacyOptions = {"Public", "Friends Only", "Private"};
        JComboBox<String> privacyComboBox = createStyledComboBox(privacyOptions);
        panel.add(privacyComboBox, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("🎨 Theme:", labelFont, labelColor), gbc);
        gbc.gridx = 1;
        String[] themes = {"Light", "Dark", "System Default"};
        JComboBox<String> themeComboBox = createStyledComboBox(themes);
        panel.add(themeComboBox, gbc);

        // Add empty space
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(Box.createVerticalGlue(), gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton changeAvatarButton = createModernButton("🖼️ Change Avatar", new Color(70, 130, 180));
        changeAvatarButton.addActionListener(e -> changeAvatar());

        JButton cancelButton = createModernButton("❌ Cancel", new Color(108, 117, 125));
        cancelButton.addActionListener(e -> dispose());

        JButton saveButton = createModernButton("💾 Save Profile", new Color(40, 167, 69));
        saveButton.addActionListener(e -> saveProfile());

        buttonPanel.add(changeAvatarButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        return buttonPanel;
    }

    private JLabel createStyledLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                // Border
                if (hasFocus()) {
                    g2d.setColor(new Color(52, 152, 219));
                } else {
                    g2d.setColor(new Color(200, 200, 200));
                }
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 10, 10);

                super.paintComponent(g);
                g2d.dispose();
            }
        };
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        field.setOpaque(false);
        return field;
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<String>(items) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                // Border
                if (hasFocus()) {
                    g2d.setColor(new Color(52, 152, 219));
                } else {
                    g2d.setColor(new Color(200, 200, 200));
                }
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 10, 10);

                super.paintComponent(g);
                g2d.dispose();
            }
        };
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        combo.setOpaque(false);
        return combo;
    }

    private JCheckBox createStyledCheckBox(String text, boolean selected) {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setSelected(selected);
        checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        checkBox.setBackground(new Color(255, 255, 255, 0));
        checkBox.setFocusPainted(false);
        checkBox.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Fix hover color issue
        checkBox.setContentAreaFilled(false);
        checkBox.setOpaque(false);

        return checkBox;
    }

    private JButton createModernButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(baseColor.darker().darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(baseColor.brighter());
                } else {
                    g2d.setColor(baseColor);
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Text
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(getText(), x, y);

                g2d.dispose();
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 45));

        return button;
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

        // Update avatar
        if (avatarLabel != null) {
            avatarLabel.repaint();
        }
    }

    private void saveProfile() {
        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String ageStr = ageField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String faculty = (String) facultyComboBox.getSelectedItem();
        String course = courseField.getText().trim();

        // Validation
        if (name.isEmpty() || surname.isEmpty() || ageStr.isEmpty() || email.isEmpty()) {
            showErrorDialog("Please fill in all required fields");
            return;
        }

        try {
            int age = Integer.parseInt(ageStr);
            if (age < 16 || age > 100) {
                showErrorDialog("Please enter a valid age (16-100)");
                return;
            }
        } catch (NumberFormatException e) {
            showErrorDialog("Age must be a number");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            showErrorDialog("Please enter a valid email address");
            return;
        }

        // Update user object
        currentUser.setName(name);
        currentUser.setSurname(surname);
        currentUser.setAge(Integer.parseInt(ageStr));
        currentUser.setGender((String) genderComboBox.getSelectedItem());
        currentUser.setEmail(email);

        // Show success message with modern styling
        showSuccessDialog(
                "Profile updated successfully!\n\n" +
                        "👤 Name: " + name + " " + surname + "\n" +
                        "🏛️ Faculty: " + faculty + "\n" +
                        "📚 Course: " + course + "\n\n" +
                        "Your changes have been saved."
        );

        dispose();
    }

    private void changeAvatar() {
        String[] options = {"🎨 Choose from Gallery", "📷 Take Photo", "🎭 Use Default"};
        int choice = JOptionPane.showOptionDialog(this,
                "<html><div style='text-align: center; width: 300px;'>" +
                        "<b>Change Profile Picture</b><br><br>" +
                        "Choose how you'd like to update your avatar:" +
                        "</div></html>",
                "Change Avatar",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 0) {
            JOptionPane.showMessageDialog(this,
                    "<html><div style='text-align: center;'>" +
                            "🖼️ Avatar Gallery<br><br>" +
                            "This would open your file browser to select an image." +
                            "</div></html>",
                    "Coming Soon",
                    JOptionPane.INFORMATION_MESSAGE);
        } else if (choice == 1) {
            JOptionPane.showMessageDialog(this,
                    "<html><div style='text-align: center;'>" +
                            "📸 Camera Access<br><br>" +
                            "This would access your camera to take a new profile picture." +
                            "</div></html>",
                    "Coming Soon",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this,
                "<html><div style='text-align: center;'>" +
                        "❌ " + message +
                        "</div></html>",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccessDialog(String message) {
        JOptionPane.showMessageDialog(this,
                "<html><div style='text-align: center;'>" +
                        "✅ " + message.replace("\n", "<br>") +
                        "</div></html>",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame();
            User testUser = new User("John", "Doe", "ST123456", 21, "Male", "john.doe@email.com");
            ProfilePage profile = new ProfilePage(frame, testUser);
            profile.setVisible(true);
        });
    }
}