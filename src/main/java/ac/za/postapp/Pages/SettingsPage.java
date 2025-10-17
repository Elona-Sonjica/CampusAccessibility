package ac.za.postapp.Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class SettingsPage extends JDialog {
    private JComboBox<String> themeComboBox;
    private JComboBox<String> fontSizeComboBox;
    private JCheckBox notificationsCheckBox;
    private JCheckBox soundCheckBox;
    private JCheckBox autoLoginCheckBox;
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<String> languageComboBox;
    private JSlider brightnessSlider;
    private JSlider volumeSlider;
    private JCheckBox locationServicesCheckBox;
    private JCheckBox dataCollectionCheckBox;
    private JFrame parentFrame;

    public SettingsPage(JFrame parent) {
        super(parent, "⚙️ Settings - Campus Access Guide", true);
        this.parentFrame = parent;
        setSize(850, 750);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

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

        // Content panel with enhanced tabs
        JTabbedPane tabbedPane = createEnhancedTabbedPane();
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadCurrentSettings();
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
        JButton backButton = createModernButton("← Back to Dashboard", new Color(108, 117, 125));
        backButton.addActionListener(e -> dispose());

        // Title with icon
        JLabel titleLabel = new JLabel("⚙️ Application Settings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);

        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(titlePanel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JTabbedPane createEnhancedTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(new Color(245, 247, 250));

        // Create enhanced tab panels
        tabbedPane.addTab("🎨 Appearance", createAppearancePanel());
        tabbedPane.addTab("🔔 Notifications", createNotificationsPanel());
        tabbedPane.addTab("🔒 Security", createSecurityPanel());
        tabbedPane.addTab("🛡️ Privacy", createPrivacyPanel());
        tabbedPane.addTab("🔊 Audio", createAudioPanel());
        tabbedPane.addTab("🌍 Accessibility", createAccessibilityPanel());

        return tabbedPane;
    }

    private JPanel createAppearancePanel() {
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
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Color labelColor = new Color(52, 73, 94);

        int row = 0;

        // Theme selection
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("🎨 Theme:", labelFont, labelColor), gbc);
        gbc.gridx = 1;
        String[] themes = {"Light", "Dark", "Blue Ocean", "Forest Green", "Sunset Orange", "High Contrast"};
        themeComboBox = createStyledComboBox(themes);
        panel.add(themeComboBox, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("🔤 Font Size:", labelFont, labelColor), gbc);
        gbc.gridx = 1;
        String[] fontSizes = {"Small", "Medium", "Large", "Extra Large"};
        fontSizeComboBox = createStyledComboBox(fontSizes);
        panel.add(fontSizeComboBox, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("🌐 Language:", labelFont, labelColor), gbc);
        gbc.gridx = 1;
        String[] languages = {"English", "Afrikaans", "Zulu", "Xhosa", "Sotho", "French", "Spanish"};
        languageComboBox = createStyledComboBox(languages);
        panel.add(languageComboBox, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("💡 Brightness:", labelFont, labelColor), gbc);
        gbc.gridx = 1;
        JPanel brightnessPanel = new JPanel(new BorderLayout());
        brightnessPanel.setOpaque(false);
        brightnessSlider = createStyledSlider();
        brightnessSlider.setValue(80);
        JLabel brightnessValue = new JLabel("80%");
        brightnessValue.setFont(new Font("Segoe UI", Font.BOLD, 12));

        brightnessSlider.addChangeListener(e -> {
            brightnessValue.setText(brightnessSlider.getValue() + "%");
        });

        brightnessPanel.add(brightnessSlider, BorderLayout.CENTER);
        brightnessPanel.add(brightnessValue, BorderLayout.EAST);
        panel.add(brightnessPanel, gbc);

        // Preview section
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        panel.add(createSectionLabel("🎭 Live Preview"), gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JPanel previewPanel = createPreviewPanel();
        panel.add(previewPanel, gbc);

        return panel;
    }

    private JPanel createNotificationsPanel() {
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
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Notification settings
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        notificationsCheckBox = createStyledCheckBox("🔔 Enable Notifications", true);
        panel.add(notificationsCheckBox, gbc);

        gbc.gridy = 1;
        soundCheckBox = createStyledCheckBox("🔊 Play Sound for Notifications", true);
        panel.add(soundCheckBox, gbc);

        gbc.gridy = 2;
        JCheckBox vibrationCheckBox = createStyledCheckBox("📳 Vibration for Notifications", true);
        panel.add(vibrationCheckBox, gbc);

        gbc.gridy = 3;
        JCheckBox popupCheckBox = createStyledCheckBox("💬 Show Popup Notifications", true);
        panel.add(popupCheckBox, gbc);

        gbc.gridy = 4;
        JCheckBox badgeCheckBox = createStyledCheckBox("🎯 Show Badge Count", true);
        panel.add(badgeCheckBox, gbc);

        // Notification types
        gbc.gridy = 5; gbc.gridwidth = 2;
        panel.add(createSectionLabel("📬 Notification Types"), gbc);

        gbc.gridy = 6;
        JCheckBox academicCheckBox = createStyledCheckBox("🎓 Academic Alerts", true);
        panel.add(academicCheckBox, gbc);

        gbc.gridy = 7;
        JCheckBox eventCheckBox = createStyledCheckBox("📅 Event Reminders", true);
        panel.add(eventCheckBox, gbc);

        gbc.gridy = 8;
        JCheckBox emergencyCheckBox = createStyledCheckBox("🚨 Emergency Alerts", true);
        emergencyCheckBox.setForeground(Color.RED);
        panel.add(emergencyCheckBox, gbc);

        // Test notification button
        gbc.gridy = 9;
        JButton testButton = createModernButton("🧪 Test Notification", new Color(52, 152, 219));
        testButton.addActionListener(e -> testNotification());
        panel.add(testButton, gbc);

        return panel;
    }

    private JPanel createSecurityPanel() {
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
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Auto login
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        autoLoginCheckBox = createStyledCheckBox("🔑 Remember Me (Auto Login)", true);
        panel.add(autoLoginCheckBox, gbc);

        // Biometric authentication
        gbc.gridy = 1;
        JCheckBox biometricCheckBox = createStyledCheckBox("👆 Enable Biometric Login", false);
        panel.add(biometricCheckBox, gbc);

        // Password change section
        gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(createSectionLabel("🔐 Change Password"), gbc);

        gbc.gridy = 3; gbc.gridwidth = 1;
        panel.add(createStyledLabel("🔒 Current Password:", new Font("Segoe UI", Font.BOLD, 13), new Color(52, 73, 94)), gbc);
        gbc.gridx = 1;
        currentPasswordField = createStyledPasswordField();
        panel.add(currentPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(createStyledLabel("🆕 New Password:", new Font("Segoe UI", Font.BOLD, 13), new Color(52, 73, 94)), gbc);
        gbc.gridx = 1;
        newPasswordField = createStyledPasswordField();
        panel.add(newPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(createStyledLabel("✅ Confirm Password:", new Font("Segoe UI", Font.BOLD, 13), new Color(52, 73, 94)), gbc);
        gbc.gridx = 1;
        confirmPasswordField = createStyledPasswordField();
        panel.add(confirmPasswordField, gbc);

        // Password strength indicator
        gbc.gridx = 1; gbc.gridy = 6;
        JLabel strengthLabel = new JLabel("Password Strength: Weak");
        strengthLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        strengthLabel.setForeground(Color.RED);
        panel.add(strengthLabel, gbc);

        // Session timeout
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        panel.add(createSectionLabel("⏰ Session Settings"), gbc);

        gbc.gridy = 8; gbc.gridwidth = 1;
        panel.add(createStyledLabel("🚪 Auto Logout After:", new Font("Segoe UI", Font.BOLD, 13), new Color(52, 73, 94)), gbc);
        gbc.gridx = 1;
        String[] timeouts = {"15 minutes", "30 minutes", "1 hour", "2 hours", "4 hours", "Never"};
        JComboBox<String> timeoutComboBox = createStyledComboBox(timeouts);
        panel.add(timeoutComboBox, gbc);

        return panel;
    }

    private JPanel createPrivacyPanel() {
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
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Location services
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        locationServicesCheckBox = createStyledCheckBox("📍 Enable Location Services", true);
        panel.add(locationServicesCheckBox, gbc);

        gbc.gridy = 1;
        JLabel locationDesc = new JLabel("Allows the app to access your location for navigation and campus services");
        locationDesc.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        locationDesc.setForeground(Color.GRAY);
        panel.add(locationDesc, gbc);

        // Data collection
        gbc.gridy = 2;
        dataCollectionCheckBox = createStyledCheckBox("📊 Allow Anonymous Data Collection", false);
        panel.add(dataCollectionCheckBox, gbc);

        gbc.gridy = 3;
        JLabel dataDesc = new JLabel("Help improve the app by sharing anonymous usage data");
        dataDesc.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        dataDesc.setForeground(Color.GRAY);
        panel.add(dataDesc, gbc);

        // Privacy options
        gbc.gridy = 4;
        JCheckBox analyticsCheckBox = createStyledCheckBox("📈 Enable Analytics", true);
        panel.add(analyticsCheckBox, gbc);

        gbc.gridy = 5;
        JCheckBox crashReportsCheckBox = createStyledCheckBox("🐛 Send Crash Reports", true);
        panel.add(crashReportsCheckBox, gbc);

        gbc.gridy = 6;
        JCheckBox personalizedAdsCheckBox = createStyledCheckBox("🎯 Personalized Ads", false);
        panel.add(personalizedAdsCheckBox, gbc);

        // Clear data button
        gbc.gridy = 7;
        JButton clearDataButton = createModernButton("🗑️ Clear App Data", new Color(220, 53, 69));
        clearDataButton.addActionListener(e -> clearAppData());
        panel.add(clearDataButton, gbc);

        // Export data button
        gbc.gridy = 8;
        JButton exportDataButton = createModernButton("💾 Export My Data", new Color(40, 167, 69));
        exportDataButton.addActionListener(e -> exportData());
        panel.add(exportDataButton, gbc);

        return panel;
    }

    private JPanel createAudioPanel() {
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
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        // Volume control
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("🔊 Master Volume:", new Font("Segoe UI", Font.BOLD, 13), new Color(52, 73, 94)), gbc);
        gbc.gridx = 1;
        volumeSlider = createStyledSlider();
        volumeSlider.setValue(75);
        panel.add(volumeSlider, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("🎵 Notification Volume:", new Font("Segoe UI", Font.BOLD, 13), new Color(52, 73, 94)), gbc);
        gbc.gridx = 1;
        JSlider notificationVolume = createStyledSlider();
        notificationVolume.setValue(80);
        panel.add(notificationVolume, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel("🔔 Ringtone:", new Font("Segoe UI", Font.BOLD, 13), new Color(52, 73, 94)), gbc);
        gbc.gridx = 1;
        String[] ringtones = {"Default", "Chime", "Bell", "Digital", "Melody"};
        JComboBox<String> ringtoneCombo = createStyledComboBox(ringtones);
        panel.add(ringtoneCombo, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JButton testSoundButton = createModernButton("🎵 Test Sound", new Color(155, 89, 182));
        testSoundButton.addActionListener(e -> testSound());
        panel.add(testSoundButton, gbc);

        return panel;
    }

    private JPanel createAccessibilityPanel() {
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
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JCheckBox highContrast = createStyledCheckBox("🎨 High Contrast Mode", false);
        panel.add(highContrast, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JCheckBox screenReader = createStyledCheckBox("📖 Screen Reader Support", false);
        panel.add(screenReader, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JCheckBox largeText = createStyledCheckBox("🔍 Large Text Mode", false);
        panel.add(largeText, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JCheckBox reduceMotion = createStyledCheckBox("🌀 Reduce Animations", false);
        panel.add(reduceMotion, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JCheckBox voiceControl = createStyledCheckBox("🎤 Voice Control", false);
        panel.add(voiceControl, gbc);

        return panel;
    }

    private JPanel createPreviewPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        panel.setBackground(new Color(250, 250, 250));

        JLabel previewLabel = new JLabel("Sample Text - AaBbCc");
        previewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        previewLabel.setForeground(new Color(52, 73, 94));

        JButton previewButton = new JButton("Preview Button");
        previewButton.setBackground(new Color(52, 152, 219));
        previewButton.setForeground(Color.WHITE);
        previewButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        panel.add(previewLabel);
        panel.add(previewButton);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton resetButton = createModernButton("🔄 Reset to Default", new Color(255, 193, 7));
        resetButton.setForeground(Color.BLACK);
        resetButton.addActionListener(e -> resetToDefaults());

        JButton cancelButton = createModernButton("❌ Cancel", new Color(108, 117, 125));
        cancelButton.addActionListener(e -> dispose());

        JButton saveButton = createModernButton("💾 Save Changes", new Color(40, 167, 69));
        saveButton.addActionListener(e -> saveSettings());

        JButton applyButton = createModernButton("✅ Apply", new Color(52, 152, 219));
        applyButton.addActionListener(e -> applySettings());

        buttonPanel.add(resetButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(applyButton);
        buttonPanel.add(saveButton);

        return buttonPanel;
    }

    // Enhanced UI Component Creation Methods
    private JLabel createStyledLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(new Color(52, 73, 94));
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        return label;
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

    private JSlider createStyledSlider() {
        JSlider slider = new JSlider(0, 100, 80);
        slider.setMajorTickSpacing(25);
        slider.setMinorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setBackground(new Color(255, 255, 255, 0));
        return slider;
    }

    private JCheckBox createStyledCheckBox(String text, boolean selected) {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setSelected(selected);
        checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        checkBox.setBackground(new Color(255, 255, 255, 0));
        checkBox.setFocusPainted(false);
        checkBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return checkBox;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField(20) {
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
                g2d.setColor(getForeground());
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(getText(), x, y);

                g2d.dispose();
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(text.contains("Reset") ? Color.BLACK : Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(160, 45));

        return button;
    }

    // Enhanced Feature Methods
    private void testNotification() {
        JOptionPane.showMessageDialog(this,
                "<html><div style='text-align: center;'>" +
                        "🔔 Test Notification Sent!<br><br>" +
                        "You should see a sample notification appear.<br>" +
                        "Check your notification settings if it doesn't appear." +
                        "</div></html>",
                "Test Notification",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void testSound() {
        JOptionPane.showMessageDialog(this,
                "<html><div style='text-align: center;'>" +
                        "🔊 Playing Test Sound<br><br>" +
                        "You should hear a sample notification sound.<br>" +
                        "Adjust the volume if it's too loud or quiet." +
                        "</div></html>",
                "Test Sound",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void exportData() {
        JOptionPane.showMessageDialog(this,
                "<html><div style='text-align: center;'>" +
                        "💾 Data Export Started<br><br>" +
                        "Your data is being prepared for export.<br>" +
                        "This may take a few moments..." +
                        "</div></html>",
                "Export Data",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void applySettings() {
        // Apply settings without closing
        saveSettingsLogic();
        JOptionPane.showMessageDialog(this,
                "<html><div style='text-align: center;'>" +
                        "✅ Settings Applied!<br><br>" +
                        "Your changes have been applied successfully.<br>" +
                        "Some changes may require restarting the app." +
                        "</div></html>",
                "Settings Applied",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadCurrentSettings() {
        themeComboBox.setSelectedItem("Light");
        fontSizeComboBox.setSelectedItem("Medium");
        languageComboBox.setSelectedItem("English");
        brightnessSlider.setValue(80);
        volumeSlider.setValue(75);
        notificationsCheckBox.setSelected(true);
        soundCheckBox.setSelected(true);
        autoLoginCheckBox.setSelected(true);
        locationServicesCheckBox.setSelected(true);
        dataCollectionCheckBox.setSelected(false);
    }

    private void saveSettings() {
        if (saveSettingsLogic()) {
            dispose();
        }
    }

    private boolean saveSettingsLogic() {
        // Validate password change if fields are filled
        String currentPass = new String(currentPasswordField.getPassword());
        String newPass = new String(newPasswordField.getPassword());
        String confirmPass = new String(confirmPasswordField.getPassword());

        if (!currentPass.isEmpty() || !newPass.isEmpty() || !confirmPass.isEmpty()) {
            if (newPass.length() < 6) {
                showErrorDialog("New password must be at least 6 characters long");
                return false;
            }
            if (!newPass.equals(confirmPass)) {
                showErrorDialog("New passwords do not match");
                return false;
            }
            // In a real app, you would verify current password and update it
            showSuccessDialog("Password updated successfully!");

            // Clear password fields
            currentPasswordField.setText("");
            newPasswordField.setText("");
            confirmPasswordField.setText("");
        }

        // Save other settings
        String theme = (String) themeComboBox.getSelectedItem();
        String fontSize = (String) fontSizeComboBox.getSelectedItem();
        String language = (String) languageComboBox.getSelectedItem();
        boolean notifications = notificationsCheckBox.isSelected();
        boolean autoLogin = autoLoginCheckBox.isSelected();
        boolean locationServices = locationServicesCheckBox.isSelected();
        boolean dataCollection = dataCollectionCheckBox.isSelected();

        // Show success message
        showSuccessDialog(
                "Settings saved successfully!<br><br>" +
                        "🎨 Theme: " + theme + "<br>" +
                        "🔤 Font Size: " + fontSize + "<br>" +
                        "🌐 Language: " + language + "<br>" +
                        "🔔 Notifications: " + (notifications ? "Enabled" : "Disabled") + "<br>" +
                        "🔑 Auto Login: " + (autoLogin ? "Enabled" : "Disabled") + "<br>" +
                        "📍 Location Services: " + (locationServices ? "Enabled" : "Disabled")
        );

        return true;
    }

    private void resetToDefaults() {
        int result = JOptionPane.showConfirmDialog(this,
                "<html><div style='text-align: center; width: 300px;'>" +
                        "<b>Reset to Default Settings</b><br><br>" +
                        "Are you sure you want to reset all settings to default values?<br>" +
                        "This action cannot be undone." +
                        "</div></html>",
                "Confirm Reset",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            themeComboBox.setSelectedItem("Light");
            fontSizeComboBox.setSelectedItem("Medium");
            languageComboBox.setSelectedItem("English");
            brightnessSlider.setValue(100);
            volumeSlider.setValue(75);
            notificationsCheckBox.setSelected(true);
            soundCheckBox.setSelected(true);
            autoLoginCheckBox.setSelected(true);
            locationServicesCheckBox.setSelected(true);
            dataCollectionCheckBox.setSelected(false);

            currentPasswordField.setText("");
            newPasswordField.setText("");
            confirmPasswordField.setText("");

            showSuccessDialog("All settings have been reset to default values!");
        }
    }

    private void clearAppData() {
        int result = JOptionPane.showConfirmDialog(this,
                "<html><div style='text-align: center; width: 350px;'>" +
                        "<b>Clear App Data</b><br><br>" +
                        "This will clear all app data including:<br>" +
                        "• Preferences and settings<br>" +
                        "• Cache and temporary files<br>" +
                        "• Login information<br><br>" +
                        "The app will restart after this operation.<br>" +
                        "Continue?" +
                        "</div></html>",
                "Clear App Data",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            // In a real app, you would clear the data here
            showSuccessDialog(
                    "App data cleared successfully!<br><br>" +
                            "The application will now restart to apply changes.<br>" +
                            "Please log in again when the app reopens."
            );

            // Simulate app restart
            dispose();
            // In a real app, you would restart the application here
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
            SettingsPage settings = new SettingsPage(frame);
            settings.setVisible(true);
        });
    }
}