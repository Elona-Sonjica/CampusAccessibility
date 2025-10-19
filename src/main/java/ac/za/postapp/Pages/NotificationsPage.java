package ac.za.postapp.Pages;

// emeritusApex


import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotificationsPage extends JDialog {
    private JList<String> notificationsList;
    private DefaultListModel<String> listModel;
    private List<AccessibilityNotification> notifications;
    private JButton clearAllButton;
    private JButton markAllReadButton;
    private JButton backToDashboardButton;
    private JButton emergencyHelpButton;

    // Animation variables
    private Timer entranceTimer;
    private Timer notificationTimer;
    private Timer emergencyPulseTimer;
    private float opacity = 0.0f;
    private int slideOffset = 50;
    private boolean emergencyPulse = false;

    // Color scheme matching Dashboard
    private final Color PRIMARY_COLOR = new Color(74, 107, 136);
    private final Color SECONDARY_COLOR = new Color(33, 64, 98);
    private final Color ACCENT_COLOR = new Color(46, 204, 113);
    private final Color WARNING_COLOR = new Color(231, 76, 60);
    private final Color EMERGENCY_COLOR = new Color(192, 57, 43);
    private final Color DARK_BG = new Color(15, 32, 55);

    // Accessibility avatars
    private final String[] AVATARS = {"🧑‍🦽", "🧑‍🦯", "👨‍🦼", "🧑‍🦼", "👩‍🦽", "👨‍🦯"};
    private Random random = new Random();

    public NotificationsPage(JFrame parent) {
        super(parent, "Accessibility Alerts", true);
        setSize(650, 750);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        notifications = new ArrayList<>();
        initializeAccessibilityNotifications();

        JPanel mainPanel = createMainPanel();
        add(mainPanel);

        startEntranceAnimation();
        startNotificationPulse();
        startEmergencyPulse();
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Emergency background pulse
                if (emergencyPulse && hasEmergencyNotifications()) {
                    g2d.setColor(new Color(192, 57, 43, (int)(30 * opacity)));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }

                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(74, 107, 136, (int)(opacity * 255)),
                        getWidth(), getHeight(), new Color(33, 64, 98, (int)(opacity * 255))
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.setColor(new Color(255, 255, 255, (int)(30 * opacity)));
                g2d.fillRoundRect(slideOffset, slideOffset,
                        getWidth() - (2 * slideOffset),
                        getHeight() - (2 * slideOffset), 40, 40);

                g2d.setColor(new Color(255, 255, 255, (int)(80 * opacity)));
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawRoundRect(slideOffset, slideOffset,
                        getWidth() - (2 * slideOffset),
                        getHeight() - (2 * slideOffset), 40, 40);
            }
        };
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(createTitleBar(), BorderLayout.NORTH);
        mainPanel.add(createContentArea(), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        return mainPanel;
    }

    private JPanel createTitleBar() {
        JPanel titleBar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(255, 255, 255, (int)(50 * opacity)));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        titleBar.setOpaque(false);
        titleBar.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        titleBar.setPreferredSize(new Dimension(0, 80));

        JLabel titleLabel = new JLabel("♿ Accessibility Alerts", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        long activeAlerts = notifications.stream().filter(n -> !n.isResolved()).count();
        JLabel subtitleLabel = new JLabel(activeAlerts + " active accessibility alerts", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(200, 220, 240));

        JPanel titleContainer = new JPanel(new BorderLayout());
        titleContainer.setOpaque(false);
        titleContainer.add(titleLabel, BorderLayout.CENTER);
        titleContainer.add(subtitleLabel, BorderLayout.SOUTH);

        JButton closeButton = createIconButton("✕", "Close");
        closeButton.addActionListener(e -> closeWithAnimation());

        titleBar.add(titleContainer, BorderLayout.CENTER);
        titleBar.add(closeButton, BorderLayout.EAST);

        return titleBar;
    }

    private JPanel createContentArea() {
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        contentPanel.add(createStatsPanel(), BorderLayout.NORTH);
        contentPanel.add(createNotificationsList(), BorderLayout.CENTER);

        return contentPanel;
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(255, 255, 255, (int)(30 * opacity)));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        statsPanel.setPreferredSize(new Dimension(0, 90));

        long activeAlerts = notifications.stream().filter(n -> !n.isResolved()).count();
        long elevatorIssues = notifications.stream().filter(n -> n.getType().equals("elevator") && !n.isResolved()).count();
        long pathObstacles = notifications.stream().filter(n -> n.getType().equals("obstacle") && !n.isResolved()).count();
        long emergencyCount = notifications.stream().filter(n -> n.getPriority().equals("emergency")).count();

        statsPanel.add(createStatCard("🚨 Active", String.valueOf(activeAlerts),
                emergencyCount > 0 ? EMERGENCY_COLOR : WARNING_COLOR));
        statsPanel.add(createStatCard("🛗 Elevators", String.valueOf(elevatorIssues),
                elevatorIssues > 0 ? WARNING_COLOR : ACCENT_COLOR));
        statsPanel.add(createStatCard("🚧 Obstacles", String.valueOf(pathObstacles),
                pathObstacles > 0 ? WARNING_COLOR : ACCENT_COLOR));
        statsPanel.add(createStatCard("✅ Resolved",
                String.valueOf(notifications.size() - activeAlerts), ACCENT_COLOR));

        return statsPanel;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(80 * opacity)));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(120 * opacity)));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 12, 12);
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 8));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        titleLabel.setForeground(new Color(255, 255, 255, (int)(220 * opacity)));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valueLabel.setForeground(Color.WHITE);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JScrollPane createNotificationsList() {
        listModel = new DefaultListModel<>();
        notificationsList = new JList<>(listModel);
        notificationsList.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        notificationsList.setCellRenderer(new AccessibilityNotificationRenderer());
        notificationsList.setFixedCellHeight(100);
        notificationsList.setOpaque(false);
        notificationsList.setBackground(new Color(0, 0, 0, 0));
        notificationsList.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        notificationsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = notificationsList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        toggleNotificationStatus(index);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(notificationsList);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUI(new ModernScrollBarUI());

        loadNotifications();
        return scrollPane;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 8, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));

        emergencyHelpButton = createModernButton("🆘 Emergency Help", EMERGENCY_COLOR);
        backToDashboardButton = createModernButton("🏠 Dashboard", PRIMARY_COLOR);
        markAllReadButton = createModernButton("✓ Mark All Resolved", ACCENT_COLOR);
        clearAllButton = createModernButton("🗑️ Clear All", WARNING_COLOR);

        emergencyHelpButton.addActionListener(e -> sendEmergencyHelp());
        backToDashboardButton.addActionListener(e -> closeWithAnimation());
        markAllReadButton.addActionListener(e -> markAllAsResolved());
        clearAllButton.addActionListener(e -> clearAllNotifications());

        buttonPanel.add(emergencyHelpButton);
        buttonPanel.add(backToDashboardButton);
        buttonPanel.add(markAllReadButton);
        buttonPanel.add(clearAllButton);

        return buttonPanel;
    }

    private JButton createModernButton(String text, Color color) {
        JButton button = new JButton(text) {
            private float hoverState = 0.0f;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bgColor = new Color(color.getRed(), color.getGreen(), color.getBlue(),
                        (int)((getModel().isPressed() ? 200 :
                                getModel().isRollover() ? 180 : 150) * (hoverState * opacity)));

                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                g2d.setColor(new Color(255, 255, 255, (int)(80 * hoverState * opacity)));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 12, 12);

                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 11));
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(getText(), x, y);
            }
        };

        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            private Timer hoverTimer;

            @Override
            public void mouseEntered(MouseEvent e) {
                if (hoverTimer != null) hoverTimer.stop();
                hoverTimer = new Timer(16, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        AnimatedButton btn = (AnimatedButton) e.getSource();
                        if (btn.hoverState < 1.0f) {
                            btn.hoverState += 0.1f;
                            btn.repaint();
                        } else {
                            hoverTimer.stop();
                        }
                    }
                });
                hoverTimer.start();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (hoverTimer != null) hoverTimer.stop();
                hoverTimer = new Timer(16, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        AnimatedButton btn = (AnimatedButton) e.getSource();
                        if (btn.hoverState > 0.0f) {
                            btn.hoverState -= 0.1f;
                            btn.repaint();
                        } else {
                            hoverTimer.stop();
                        }
                    }
                });
                hoverTimer.start();
            }
        });

        return button;
    }

    private class AnimatedButton extends JButton {
        public float hoverState = 0.0f;
        public AnimatedButton(String text) { super(text); }
    }

    private JButton createIconButton(String icon, String tooltip) {
        JButton button = new JButton(icon);
        button.setPreferredSize(new Dimension(40, 40));
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText(tooltip);
        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        button.setForeground(Color.WHITE);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(255, 255, 255, 40));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(255, 255, 255, 0));
            }
        });

        return button;
    }

    private void initializeAccessibilityNotifications() {
        // Elevator-related notifications
        notifications.add(new AccessibilityNotification(
                "Main Building Elevator Out of Service",
                "elevator", "high", false,
                "Elevator A in Main Building temporarily unavailable for maintenance",
                "Engineering"
        ));

        notifications.add(new AccessibilityNotification(
                "Library Elevator Operational",
                "elevator", "low", true,
                "Elevator B in Library now fully operational with voice assistance",
                "Library"
        ));

        // Path obstacle notifications
        notifications.add(new AccessibilityNotification(
                "Construction Blocking Access Ramp",
                "obstacle", "high", false,
                "Temporary construction blocking accessible ramp near Student Center",
                "Campus Services"
        ));

        notifications.add(new AccessibilityNotification(
                "Wet Floor in Science Building",
                "obstacle", "medium", false,
                "Main corridor wet - use alternative accessible route via North Wing",
                "Facilities"
        ));

        // Route updates
        notifications.add(new AccessibilityNotification(
                "New Accessible Route Available",
                "route", "low", true,
                "New paved pathway connecting Library to Arts Building now open",
                "Campus Planning"
        ));

        // Emergency alerts
        notifications.add(new AccessibilityNotification(
                "EMERGENCY: Power Outage Affecting Elevators",
                "emergency", "emergency", false,
                "Multiple elevators offline due to campus power outage. Emergency teams deployed.",
                "Campus Safety"
        ));

        notifications.add(new AccessibilityNotification(
                "Accessible Parking Lot Maintenance",
                "service", "medium", false,
                "Accessible parking spots near Admin Building temporarily relocated",
                "Parking Services"
        ));
    }

    private void loadNotifications() {
        listModel.clear();
        for (AccessibilityNotification notification : notifications) {
            listModel.addElement(notification.getMessage());
        }
    }

    private void markAllAsResolved() {
        animateButtonClick(markAllReadButton);

        for (AccessibilityNotification notification : notifications) {
            notification.setResolved(true);
        }

        loadNotifications();
        showConfirmation("All alerts marked as resolved");
    }

    private void toggleNotificationStatus(int index) {
        if (index >= 0 && index < notifications.size()) {
            AccessibilityNotification notification = notifications.get(index);
            notification.setResolved(!notification.isResolved());
            loadNotifications();
            notificationsList.repaint(notificationsList.getCellBounds(index, index));
        }
    }

    private void clearAllNotifications() {
        animateButtonClick(clearAllButton);

        int result = JOptionPane.showConfirmDialog(this,
                "<html><div style='text-align: center; width: 280px;'>" +
                        "Clear all accessibility alerts?<br><br>" +
                        "<small>This will remove all notifications including active alerts</small>" +
                        "</div></html>",
                "Confirm Clear All",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            notifications.clear();
            loadNotifications();
            showConfirmation("All alerts cleared");
        }
    }

    private void sendEmergencyHelp() {
        animateButtonClick(emergencyHelpButton);

        // Simulate emergency help request
        String location = "Current Location: Main Campus";
        String emergencyMsg = "<html><div style='text-align: center; width: 300px;'>" +
                "<h3 style='color: #c0392b;'>🆘 EMERGENCY ASSISTANCE REQUESTED</h3>" +
                "<p>Accessibility support team has been notified.</p>" +
                "<p><b>" + location + "</b></p>" +
                "<p>Help will arrive within 5 minutes.</p>" +
                "<small>Stay calm and remain in your location</small>" +
                "</div></html>";

        JOptionPane.showMessageDialog(this, emergencyMsg, "Emergency Assistance", JOptionPane.WARNING_MESSAGE);

        // Add emergency notification
        notifications.add(0, new AccessibilityNotification(
                "EMERGENCY: Assistance Requested - " + location,
                "emergency", "emergency", false,
                "User has requested immediate accessibility assistance at current location",
                "Emergency Response"
        ));
        loadNotifications();
    }

    private void animateButtonClick(JButton button) {
        Timer clickTimer = new Timer(10, new ActionListener() {
            float scale = 1.0f;
            @Override
            public void actionPerformed(ActionEvent e) {
                scale -= 0.1f;
                if (scale <= 0.8f) {
                    ((Timer)e.getSource()).stop();
                    Timer resetTimer = new Timer(10, new ActionListener() {
                        float resetScale = 0.8f;
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            resetScale += 0.1f;
                            if (resetScale >= 1.0f) {
                                ((Timer)evt.getSource()).stop();
                            }
                        }
                    });
                    resetTimer.start();
                }
            }
        });
        clickTimer.start();
    }

    private void showConfirmation(String message) {
        JOptionPane.showMessageDialog(this,
                "<html><div style='text-align: center;'>" + message + "</div></html>",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void startEntranceAnimation() {
        entranceTimer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                slideOffset -= 2;

                if (opacity >= 1.0f) {
                    opacity = 1.0f;
                    slideOffset = 0;
                    ((Timer)e.getSource()).stop();
                }

                repaint();
            }
        });
        entranceTimer.start();
    }

    private void startNotificationPulse() {
        notificationTimer = new Timer(3000, e -> {
            repaint();
        });
        notificationTimer.start();
    }

    private void startEmergencyPulse() {
        emergencyPulseTimer = new Timer(1000, e -> {
            emergencyPulse = !emergencyPulse;
            if (hasEmergencyNotifications()) {
                repaint();
            }
        });
        emergencyPulseTimer.start();
    }

    private boolean hasEmergencyNotifications() {
        return notifications.stream().anyMatch(n -> n.getPriority().equals("emergency") && !n.isResolved());
    }

    private void closeWithAnimation() {
        entranceTimer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                opacity -= 0.05f;
                slideOffset += 2;

                if (opacity <= 0.0f) {
                    opacity = 0.0f;
                    ((Timer)e.getSource()).stop();
                    dispose();
                }

                repaint();
            }
        });
        entranceTimer.start();
    }

    // Accessibility Notification Class
    private class AccessibilityNotification {
        private String message;
        private String type; // elevator, obstacle, route, emergency, service
        private String priority; // low, medium, high, emergency
        private boolean resolved;
        private String description;
        private String department;
        private LocalDateTime timestamp;
        private String avatar;

        public AccessibilityNotification(String message, String type, String priority,
                                         boolean resolved, String description, String department) {
            this.message = message;
            this.type = type;
            this.priority = priority;
            this.resolved = resolved;
            this.description = description;
            this.department = department;
            this.timestamp = LocalDateTime.now();
            this.avatar = AVATARS[random.nextInt(AVATARS.length)];
        }

        // Getters
        public String getMessage() { return message; }
        public String getType() { return type; }
        public String getPriority() { return priority; }
        public boolean isResolved() { return resolved; }
        public String getDescription() { return description; }
        public String getDepartment() { return department; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public String getAvatar() { return avatar; }

        public void setResolved(boolean resolved) { this.resolved = resolved; }
    }

    // Custom renderer for accessibility notifications
    private class AccessibilityNotificationRenderer extends JPanel implements ListCellRenderer<String> {
        private String text;
        private boolean isSelected;
        private AccessibilityNotification notification;
        private float hoverState = 0.0f;

        public AccessibilityNotificationRenderer() {
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends String> list,
                                                      String value, int index, boolean isSelected, boolean cellHasFocus) {
            this.text = value;
            this.isSelected = isSelected;

            if (index >= 0 && index < notifications.size()) {
                this.notification = notifications.get(index);
            }

            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            // Background based on priority and status
            Color bgColor = getNotificationColor();
            g2d.setColor(new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(),
                    (int)((isSelected ? 80 : 40) * opacity)));
            g2d.fillRoundRect(5, 2, width - 10, height - 4, 20, 20);

            // Left avatar section
            g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
            if (notification != null) {
                g2d.drawString(notification.getAvatar(), 15, 30);
            }

            // Type icon
            String typeIcon = getTypeIcon();
            g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
            g2d.drawString(typeIcon, 15, 60);

            // Main content area
            int contentX = 60;

            // Priority indicator and title
            if (notification != null) {
                Color priorityColor = getPriorityColor(notification.getPriority());
                g2d.setColor(priorityColor);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));

                String priorityText = notification.getPriority().toUpperCase();
                if (notification.getPriority().equals("emergency")) {
                    priorityText = "🚨 " + priorityText;
                }
                g2d.drawString(priorityText, contentX, 20);

                // Title
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 13));
                String displayText = text.length() > 40 ? text.substring(0, 37) + "..." : text;
                g2d.drawString(displayText, contentX, 40);

                // Description
                g2d.setColor(new Color(255, 255, 255, 180));
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                String desc = notification.getDescription();
                if (desc.length() > 60) desc = desc.substring(0, 57) + "...";
                g2d.drawString(desc, contentX, 60);

                // Department and time
                g2d.setColor(new Color(255, 255, 255, 150));
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                g2d.drawString(notification.getDepartment() + " • " + formatTimeAgo(notification.getTimestamp()),
                        contentX, 80);

                // Status indicator
                if (notification.isResolved()) {
                    g2d.setColor(ACCENT_COLOR);
                    g2d.setFont(new Font("Segoe UI", Font.BOLD, 10));
                    g2d.drawString("✓ RESOLVED", width - 80, 80);
                }
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(super.getPreferredSize().width, 100);
        }

        private Color getNotificationColor() {
            if (notification == null) return PRIMARY_COLOR;

            switch (notification.getPriority()) {
                case "emergency": return EMERGENCY_COLOR;
                case "high": return WARNING_COLOR;
                case "medium": return new Color(241, 196, 15);
                case "low": return notification.isResolved() ? ACCENT_COLOR : PRIMARY_COLOR;
                default: return PRIMARY_COLOR;
            }
        }

        private String getTypeIcon() {
            if (notification == null) return "📢";

            switch (notification.getType()) {
                case "elevator": return "🛗";
                case "obstacle": return "🚧";
                case "route": return "🛣️";
                case "emergency": return "🚨";
                case "service": return "🔧";
                default: return "📢";
            }
        }

        private Color getPriorityColor(String priority) {
            switch (priority) {
                case "emergency": return new Color(231, 76, 60);
                case "high": return new Color(230, 126, 34);
                case "medium": return new Color(241, 196, 15);
                case "low": return new Color(46, 204, 113);
                default: return new Color(149, 165, 166);
            }
        }
    }

    private String formatTimeAgo(LocalDateTime timestamp) {
        long minutes = ChronoUnit.MINUTES.between(timestamp, LocalDateTime.now());
        if (minutes < 1) return "Just now";
        if (minutes < 60) return minutes + " min ago";
        if (minutes < 1440) return (minutes / 60) + " hours ago";
        return (minutes / 1440) + " days ago";
    }

    // Modern scrollbar UI
    private class ModernScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = new Color(255, 255, 255, 100);
            this.trackColor = new Color(255, 255, 255, 30);
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createInvisibleButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createInvisibleButton();
        }

        private JButton createInvisibleButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(thumbColor);
            g2d.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2,
                    thumbBounds.width - 4, thumbBounds.height - 4, 10, 10);
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(trackColor);
            g2d.fillRoundRect(trackBounds.x + 2, trackBounds.y + 2,
                    trackBounds.width - 4, trackBounds.height - 4, 5, 5);
        }
    }
}