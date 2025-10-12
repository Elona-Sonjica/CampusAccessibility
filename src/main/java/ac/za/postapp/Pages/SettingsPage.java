package ac.za.postapp.Pages;

import javax.swing.*;
import java.awt.*;

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
    private JCheckBox locationServicesCheckBox;
    private JCheckBox dataCollectionCheckBox;

    public SettingsPage(JFrame parent) {
        super(parent, "Settings", true);
        setSize(700, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 247, 250));

        // Title
        JLabel titleLabel = new JLabel("⚙️ Settings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(52, 73, 94));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Tabbed pane for different settings categories
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Appearance tab
        tabbedPane.addTab("Appearance", createAppearancePanel());

        // Notifications tab
        tabbedPane.addTab("Notifications", createNotificationsPanel());

        // Security tab
        tabbedPane.addTab("Security", createSecurityPanel());

        // Privacy tab
        tabbedPane.addTab("Privacy", createPrivacyPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(245, 247, 250));

        JButton saveButton = new JButton("Save Changes");
        styleButton(saveButton, new Color(40, 167, 69));
        saveButton.addActionListener(e -> saveSettings());

        JButton cancelButton = new JButton("Cancel");
        styleButton(cancelButton, new Color(108, 117, 125));
        cancelButton.addActionListener(e -> dispose());

        JButton resetButton = new JButton("Reset to Default");
        styleButton(resetButton, new Color(255, 193, 7));
        resetButton.setForeground(Color.BLACK);
        resetButton.addActionListener(e -> resetToDefaults());

        buttonPanel.add(resetButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadCurrentSettings();
    }

    private JPanel createAppearancePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Theme selection
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Theme:"), gbc);
        gbc.gridx = 1;
        String[] themes = {"Light", "Dark", "System Default", "Blue", "Green", "High Contrast"};
        themeComboBox = new JComboBox<>(themes);
        panel.add(themeComboBox, gbc);

        // Font size
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Font Size:"), gbc);
        gbc.gridx = 1;
        String[] fontSizes = {"Small", "Medium", "Large", "Extra Large"};
        fontSizeComboBox = new JComboBox<>(fontSizes);
        panel.add(fontSizeComboBox, gbc);

        // Language
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Language:"), gbc);
        gbc.gridx = 1;
        String[] languages = {"English", "Afrikaans", "Zulu", "Xhosa", "Sotho"};
        languageComboBox = new JComboBox<>(languages);
        panel.add(languageComboBox, gbc);

        // Brightness
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Brightness:"), gbc);
        gbc.gridx = 1;
        JPanel brightnessPanel = new JPanel(new BorderLayout());
        brightnessSlider = new JSlider(0, 100, 80);
        brightnessSlider.setMajorTickSpacing(25);
        brightnessSlider.setMinorTickSpacing(5);
        brightnessSlider.setPaintTicks(true);
        brightnessSlider.setPaintLabels(true);
        brightnessPanel.add(brightnessSlider, BorderLayout.CENTER);
        panel.add(brightnessPanel, gbc);

        return panel;
    }

    private JPanel createNotificationsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Notification settings
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        notificationsCheckBox = new JCheckBox("Enable Notifications");
        notificationsCheckBox.setSelected(true);
        panel.add(notificationsCheckBox, gbc);

        gbc.gridy = 1;
        soundCheckBox = new JCheckBox("Play Sound for Notifications");
        soundCheckBox.setSelected(true);
        panel.add(soundCheckBox, gbc);

        gbc.gridy = 2;
        JCheckBox vibrationCheckBox = new JCheckBox("Vibration for Notifications");
        vibrationCheckBox.setSelected(true);
        panel.add(vibrationCheckBox, gbc);

        gbc.gridy = 3;
        JCheckBox popupCheckBox = new JCheckBox("Show Popup Notifications");
        popupCheckBox.setSelected(true);
        panel.add(popupCheckBox, gbc);

        gbc.gridy = 4;
        JLabel descLabel = new JLabel("Notification preferences will apply to all alert types");
        descLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        descLabel.setForeground(Color.GRAY);
        panel.add(descLabel, gbc);

        return panel;
    }

    private JPanel createSecurityPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Auto login
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        autoLoginCheckBox = new JCheckBox("Remember Me (Auto Login)");
        autoLoginCheckBox.setSelected(true);
        panel.add(autoLoginCheckBox, gbc);

        // Password change section
        gbc.gridy = 1; gbc.gridwidth = 2;
        panel.add(createSectionLabel("Change Password"), gbc);

        gbc.gridy = 2; gbc.gridwidth = 1;
        panel.add(new JLabel("Current Password:"), gbc);
        gbc.gridx = 1;
        currentPasswordField = new JPasswordField(20);
        currentPasswordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        panel.add(currentPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("New Password:"), gbc);
        gbc.gridx = 1;
        newPasswordField = new JPasswordField(20);
        newPasswordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        panel.add(newPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        panel.add(confirmPasswordField, gbc);

        // Session timeout
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        panel.add(createSectionLabel("Session Settings"), gbc);

        gbc.gridy = 6; gbc.gridwidth = 1;
        panel.add(new JLabel("Auto Logout After:"), gbc);
        gbc.gridx = 1;
        String[] timeouts = {"15 minutes", "30 minutes", "1 hour", "2 hours", "Never"};
        JComboBox<String> timeoutComboBox = new JComboBox<>(timeouts);
        panel.add(timeoutComboBox, gbc);

        return panel;
    }

    private JPanel createPrivacyPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Location services
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        locationServicesCheckBox = new JCheckBox("Enable Location Services");
        locationServicesCheckBox.setSelected(true);
        panel.add(locationServicesCheckBox, gbc);

        gbc.gridy = 1;
        JLabel locationDesc = new JLabel("Allows the app to access your location for navigation and campus services");
        locationDesc.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        locationDesc.setForeground(Color.GRAY);
        panel.add(locationDesc, gbc);

        // Data collection
        gbc.gridy = 2;
        dataCollectionCheckBox = new JCheckBox("Allow Anonymous Data Collection");
        dataCollectionCheckBox.setSelected(false);
        panel.add(dataCollectionCheckBox, gbc);

        gbc.gridy = 3;
        JLabel dataDesc = new JLabel("Help improve the app by sharing anonymous usage data");
        dataDesc.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        dataDesc.setForeground(Color.GRAY);
        panel.add(dataDesc, gbc);

        // Privacy options
        gbc.gridy = 4;
        JCheckBox analyticsCheckBox = new JCheckBox("Enable Analytics");
        analyticsCheckBox.setSelected(true);
        panel.add(analyticsCheckBox, gbc);

        gbc.gridy = 5;
        JCheckBox crashReportsCheckBox = new JCheckBox("Send Crash Reports");
        crashReportsCheckBox.setSelected(true);
        panel.add(crashReportsCheckBox, gbc);

        // Clear data button
        gbc.gridy = 6;
        JButton clearDataButton = new JButton("Clear App Data");
        styleButton(clearDataButton, new Color(220, 53, 69));
        clearDataButton.addActionListener(e -> clearAppData());
        panel.add(clearDataButton, gbc);

        return panel;
    }

    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(52, 73, 94));
        return label;
    }

    private void loadCurrentSettings() {
        // Load current settings (in a real app, these would come from preferences/database)
        themeComboBox.setSelectedItem("Light");
        fontSizeComboBox.setSelectedItem("Medium");
        languageComboBox.setSelectedItem("English");
        brightnessSlider.setValue(80);
        notificationsCheckBox.setSelected(true);
        soundCheckBox.setSelected(true);
        autoLoginCheckBox.setSelected(true);
        locationServicesCheckBox.setSelected(true);
        dataCollectionCheckBox.setSelected(false);
    }

    private void saveSettings() {
        // Validate password change if fields are filled
        String currentPass = new String(currentPasswordField.getPassword());
        String newPass = new String(newPasswordField.getPassword());
        String confirmPass = new String(confirmPasswordField.getPassword());

        if (!currentPass.isEmpty() || !newPass.isEmpty() || !confirmPass.isEmpty()) {
            if (newPass.length() < 6) {
                JOptionPane.showMessageDialog(this, "New password must be at least 6 characters long", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!newPass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(this, "New passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // In a real app, you would verify current password and update it
            JOptionPane.showMessageDialog(this, "Password updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Clear password fields
            currentPasswordField.setText("");
            newPasswordField.setText("");
            confirmPasswordField.setText("");
        }

        // Save other settings
        String theme = (String) themeComboBox.getSelectedItem();
        String fontSize = (String) fontSizeComboBox.getSelectedItem();
        boolean notifications = notificationsCheckBox.isSelected();
        boolean autoLogin = autoLoginCheckBox.isSelected();

        // Show success message
        JOptionPane.showMessageDialog(this,
                "Settings saved successfully!\n" +
                        "Theme: " + theme + "\n" +
                        "Font Size: " + fontSize + "\n" +
                        "Notifications: " + (notifications ? "Enabled" : "Disabled") + "\n" +
                        "Auto Login: " + (autoLogin ? "Enabled" : "Disabled"),
                "Settings Saved",
                JOptionPane.INFORMATION_MESSAGE);

        dispose();
    }

    private void resetToDefaults() {
        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to reset all settings to default values?",
                "Confirm Reset",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            themeComboBox.setSelectedItem("Light");
            fontSizeComboBox.setSelectedItem("Medium");
            languageComboBox.setSelectedItem("English");
            brightnessSlider.setValue(100);
            notificationsCheckBox.setSelected(true);
            soundCheckBox.setSelected(true);
            autoLoginCheckBox.setSelected(true);
            locationServicesCheckBox.setSelected(true);
            dataCollectionCheckBox.setSelected(false);

            currentPasswordField.setText("");
            newPasswordField.setText("");
            confirmPasswordField.setText("");

            JOptionPane.showMessageDialog(this, "Settings reset to default values", "Reset Complete", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void clearAppData() {
        int result = JOptionPane.showConfirmDialog(this,
                "This will clear all app data including preferences and cache. Continue?",
                "Clear App Data",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            // In a real app, you would clear the data here
            JOptionPane.showMessageDialog(this,
                    "App data cleared successfully!\nThe application will now restart.",
                    "Data Cleared",
                    JOptionPane.INFORMATION_MESSAGE);

            // Simulate app restart
            dispose();
            // In a real app, you would restart the application here
        }
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
            SettingsPage settings = new SettingsPage(frame);
            settings.setVisible(true);
        });
    }
}