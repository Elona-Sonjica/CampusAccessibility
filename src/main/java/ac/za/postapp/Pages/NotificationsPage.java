package ac.za.postapp.Pages;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationsPage extends JDialog {
    private JList<String> notificationsList;
    private DefaultListModel<String> listModel;
    private List<Notification> notifications;
    private JButton clearAllButton;
    private JButton markAllReadButton;

    public NotificationsPage(JFrame parent) {
        super(parent, "Notifications", true);
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        notifications = new ArrayList<>();
        initializeSampleNotifications();

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 247, 250));

        // Title
        JLabel titleLabel = new JLabel("🔔 Notifications", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(52, 73, 94));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Notifications list
        listModel = new DefaultListModel<>();
        notificationsList = new JList<>(listModel);
        notificationsList.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        notificationsList.setCellRenderer(new NotificationRenderer());

        JScrollPane scrollPane = new JScrollPane(notificationsList);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(245, 247, 250));

        markAllReadButton = new JButton("Mark All Read");
        styleButton(markAllReadButton, new Color(40, 167, 69));
        markAllReadButton.addActionListener(e -> markAllAsRead());

        clearAllButton = new JButton("Clear All");
        styleButton(clearAllButton, new Color(220, 53, 69));
        clearAllButton.addActionListener(e -> clearAllNotifications());

        buttonPanel.add(markAllReadButton);
        buttonPanel.add(clearAllButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadNotifications();
    }

    private void initializeSampleNotifications() {
        notifications.add(new Notification("New assignment posted in AppDev", "high", false));
        notifications.add(new Notification("Class cancelled: Project", "medium", false));
        notifications.add(new Notification("Campus event: Design showcasing tomorrow", "low", true));
        notifications.add(new Notification("Library book due soon: Introduction to Java", "high", false));
        notifications.add(new Notification("Your grade for Project assignment is available", "medium", true));
    }

    private void loadNotifications() {
        listModel.clear();
        for (Notification notification : notifications) {
            String status = notification.isRead() ? "✓ " : "● ";
            String priority = "[" + notification.getPriority().toUpperCase() + "] ";
            listModel.addElement(status + priority + notification.getMessage());
        }
    }

    private void markAllAsRead() {
        for (Notification notification : notifications) {
            notification.setRead(true);
        }

        loadNotifications();
    }

    private void clearAllNotifications() {
        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to clear all notifications?",
                "Confirm Clear", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            notifications.clear();
            loadNotifications();
        }
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Notification class
    private class Notification {
        private String message;
        private String priority; // high, medium, low
        private boolean read;
        private LocalDateTime timestamp;

        public Notification(String message, String priority, boolean read) {
            this.message = message;
            this.priority = priority;
            this.read = read;
            this.timestamp = LocalDateTime.now();
        }

        public String getMessage() { return message; }
        public String getPriority() { return priority; }
        public boolean isRead() { return read; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setRead(boolean read) { this.read = read; }
    }

    // Custom renderer for notifications
    private class NotificationRenderer extends JLabel implements ListCellRenderer<String> {
        public NotificationRenderer() {
            setOpaque(true);
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        public Component getListCellRendererComponent(JList<? extends String> list,
                                                      String value, int index, boolean isSelected, boolean cellHasFocus) {

            setText(value);

            if (isSelected) {
                setBackground(new Color(220, 230, 240));
                setForeground(Color.BLACK);
            } else {
                setBackground(Color.WHITE);
                setForeground(Color.BLACK);
            }

            // Color code based on priority
            if (value.contains("[HIGH]")) {
                setForeground(new Color(220, 53, 69));
            } else if (value.contains("[MEDIUM]")) {
                setForeground(new Color(255, 193, 7));
            }

            return this;
        }
    }
}