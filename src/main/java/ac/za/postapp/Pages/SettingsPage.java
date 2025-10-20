package ac.za.postapp.Pages;

import javax.swing.*;
import java.awt.*;

/**
 * Elona Sonjica
 */
public class SettingsPage extends JDialog {
    private JCheckBox notificationsCheckBox;
    private JCheckBox soundCheckBox;
    private JCheckBox vibrationCheckBox;
    private JCheckBox popupCheckBox;
    private JCheckBox badgeCheckBox;
    private JCheckBox highContrast;
    private JCheckBox screenReader;
    private JCheckBox largeText;
    private JCheckBox reduceMotion;

    public SettingsPage(JFrame parent) {
        super(parent, "Settings", true);
        setSize(720, 620);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout());
        main.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel center = new JPanel(new GridLayout(1, 2, 14, 0));
        center.add(createNotificationPanel());
        center.add(createAccessibilityPanel());

        main.add(center, BorderLayout.CENTER);
        main.add(createButtonPanel(), BorderLayout.SOUTH);

        add(main);
    }

    private JPanel createNotificationPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Notifications"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; gbc.gridy = 0;

        notificationsCheckBox = new JCheckBox("Enable Notifications", true);
        panel.add(notificationsCheckBox, gbc);

        gbc.gridy++;
        soundCheckBox = new JCheckBox("Play Sound for Notifications", true);
        panel.add(soundCheckBox, gbc);

        gbc.gridy++;
        vibrationCheckBox = new JCheckBox("Vibration for Notifications", false);
        panel.add(vibrationCheckBox, gbc);

        gbc.gridy++;
        popupCheckBox = new JCheckBox("Show Popup Notifications", true);
        panel.add(popupCheckBox, gbc);

        gbc.gridy++;
        badgeCheckBox = new JCheckBox("Show Badge Count", true);
        panel.add(badgeCheckBox, gbc);

        gbc.gridy++;
        JButton testNotif = new JButton("Test Notification");
        testNotif.addActionListener(e -> testNotification());
        panel.add(testNotif, gbc);

        gbc.gridy++;
        JButton testSound = new JButton("Test Sound");
        testSound.addActionListener(e -> testSound());
        panel.add(testSound, gbc);

        return panel;
    }

    private JPanel createAccessibilityPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Accessibility"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; gbc.gridy = 0;

        highContrast = new JCheckBox("High Contrast Mode", false);
        panel.add(highContrast, gbc);

        gbc.gridy++;
        screenReader = new JCheckBox("Screen Reader Support", false);
        panel.add(screenReader, gbc);

        gbc.gridy++;
        largeText = new JCheckBox("Large Text Mode", false);
        panel.add(largeText, gbc);

        gbc.gridy++;
        reduceMotion = new JCheckBox("Reduce Animations", true);
        panel.add(reduceMotion, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JButton resetBtn = new JButton("Reset to Default");
        JButton cancelBtn = new JButton("Cancel");
        JButton applyBtn = new JButton("Apply");
        JButton saveBtn = new JButton("Save");

        resetBtn.addActionListener(e -> resetToDefaults());
        cancelBtn.addActionListener(e -> dispose());
        applyBtn.addActionListener(e -> applySettings());
        saveBtn.addActionListener(e -> {
            applySettings();
            // show saved feedback - persistence can be added later
            JOptionPane.showMessageDialog(this, "Settings saved.", "Settings", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        panel.add(resetBtn);
        panel.add(cancelBtn);
        panel.add(applyBtn);
        panel.add(saveBtn);

        return panel;
    }

    private void resetToDefaults() {
        notificationsCheckBox.setSelected(true);
        soundCheckBox.setSelected(true);
        vibrationCheckBox.setSelected(false);
        popupCheckBox.setSelected(true);
        badgeCheckBox.setSelected(true);

        highContrast.setSelected(false);
        screenReader.setSelected(false);
        largeText.setSelected(false);
        reduceMotion.setSelected(true);

        JOptionPane.showMessageDialog(this, "Settings reset to defaults.", "Reset", JOptionPane.INFORMATION_MESSAGE);
    }

    private void applySettings() {
        // TODO: persist settings
        boolean notificationsEnabled = notificationsCheckBox.isSelected();
        boolean soundEnabled = soundCheckBox.isSelected();
        boolean vibration = vibrationCheckBox.isSelected();
        boolean popup = popupCheckBox.isSelected();

        // For now, just show a confirmation toast-like dialog
        String summary = "<html><b>Applied Settings</b><br>"
                + "Notifications: " + (notificationsEnabled ? "On" : "Off") + "<br>"
                + "Sound: " + (soundEnabled ? "On" : "Off") + "<br>"
                + "Vibration: " + (vibration ? "On" : "Off") + "</html>";

        JOptionPane.showMessageDialog(this, summary, "Settings Applied", JOptionPane.INFORMATION_MESSAGE);
    }

    private void testNotification() {
        JOptionPane.showMessageDialog(this,
                "<html><b>🔔 Test Notification</b><br>This is a notification preview.</html>",
                "Test Notification", JOptionPane.INFORMATION_MESSAGE);
    }

    private void testSound() {
        JOptionPane.showMessageDialog(this,
                "🔊 (Simulated) Test sound - implement audio playback for real sound test.",
                "Test Sound", JOptionPane.INFORMATION_MESSAGE);
    }
}