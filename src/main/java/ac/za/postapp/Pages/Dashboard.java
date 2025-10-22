package ac.za.postapp.Pages;

// emeritusApex

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Dashboard extends JFrame {
    private User currentUser;
    private ImageIcon logoIcon;

    // Modern UI Components
    private JLabel timeLabel;
    private JLabel greetingLabel;
    private Timer clockTimer;
    private Timer animationTimer;
    private Timer notificationTimer;

    private JFrame mapFrame;
    private JFrame settingsFrame;
    private JFrame liveUpdatesFrame;
    private JFrame eventsFrame;
    private NotificationsPage notificationsPage;

    // Color scheme matching Register page
    private final Color PRIMARY_COLOR = new Color(74, 107, 136);
    private final Color SECONDARY_COLOR = new Color(33, 64, 98);
    private final Color ACCENT_COLOR = new Color(46, 204, 113);
    private final Color WARNING_COLOR = new Color(231, 76, 60);
    private final Color DARK_BG = new Color(15, 32, 55);
    private final Color LIGHT_BG = new Color(248, 249, 250);
    private final Color GLASS_COLOR = new Color(255, 255, 255, 180);

    // Animation variables
    private AnimatedCard[] featureCards;
    private int activeCardIndex = -1;
    private float[] cardScales = {1.0f, 1.0f, 1.0f, 1.0f};
    private Point[] cardPositions = new Point[4];

    // Notification variables
    private int notificationCount = 3;
    private JLabel notificationBadge;

    public Dashboard() {
        initializeDashboard();
        setupModernUI();
        startAnimations();
        startNotificationTimer();
    }

    private void initializeDashboard() {
        loadLogo();

        setTitle("Campus Access Guide - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(true);

        // Set application icon
        if (logoIcon != null) {
            setIconImage(logoIcon.getImage());
        }

        // Modern layout
        setLayout(new BorderLayout());
        getContentPane().setBackground(DARK_BG);
    }

    private void setupModernUI() {
        // Create main container with gradient matching Register page
        JPanel mainContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient matching Register page
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(74, 107, 136),
                        getWidth(), getHeight(), new Color(33, 64, 98)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Add subtle particles for depth
                g2d.setColor(new Color(255, 255, 255, 8));
                Random rand = new Random();
                for (int i = 0; i < 100; i++) {
                    int x = rand.nextInt(getWidth());
                    int y = rand.nextInt(getHeight());
                    int size = rand.nextInt(3) + 1;
                    g2d.fillOval(x, y, size, size);
                }
            }
        };
        mainContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Add components
        mainContainer.add(createHeader(), BorderLayout.NORTH);
        mainContainer.add(createMainContent(), BorderLayout.CENTER);
        mainContainer.add(createBottomBar(), BorderLayout.SOUTH);

        add(mainContainer);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Glass morphism effect matching Register
                g2d.setColor(new Color(255, 255, 255, 25));
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Subtle border
                g2d.setColor(new Color(255, 255, 255, 40));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
            }
        };
        header.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        header.setOpaque(false);

        // Left side - Logo and Title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setOpaque(false);

        JLabel logoLabel = createStaticLogo();
        leftPanel.add(logoLabel);

        JLabel titleLabel = new JLabel("CAMPUS ACCESS GUIDE");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        leftPanel.add(titleLabel);

        // Center - Time and Greeting
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        centerPanel.setOpaque(false);

        timeLabel = new JLabel("", SwingConstants.CENTER);
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        timeLabel.setForeground(new Color(200, 220, 240));

        greetingLabel = new JLabel("", SwingConstants.CENTER);
        greetingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        greetingLabel.setForeground(new Color(180, 200, 220));

        centerPanel.add(timeLabel);
        centerPanel.add(greetingLabel);

        // Right side - Quick Actions with visible avatars
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);

        JButton notificationBtn = createHeaderButton("🔔", "Notifications");
        JButton settingsBtn = createHeaderButton("⚙️", "Settings");
        JButton profileBtn = createHeaderButton("👤", "Profile");

        // Add notification badge to notification button
        notificationBadge = createNotificationBadge();
        updateNotificationBadge();
        JPanel notificationPanel = new JPanel(new BorderLayout());
        notificationPanel.setOpaque(false);
        notificationPanel.add(notificationBtn, BorderLayout.CENTER);
        notificationPanel.add(notificationBadge, BorderLayout.NORTH);

        notificationBtn.addActionListener(e -> openNotifications());
        settingsBtn.addActionListener(e -> openSettings());
        profileBtn.addActionListener(e -> openProfile());

        rightPanel.add(notificationPanel);
        rightPanel.add(settingsBtn);
        rightPanel.add(profileBtn);

        header.add(leftPanel, BorderLayout.WEST);
        header.add(centerPanel, BorderLayout.CENTER);
        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    private JLabel createNotificationBadge() {
        JLabel badge = new JLabel("0") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw red circle background
                g2d.setColor(new Color(231, 76, 60));
                g2d.fillOval(0, 0, getWidth(), getHeight());

                // Draw white text
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 10));
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(getText(), x, y);
            }
        };
        badge.setPreferredSize(new Dimension(18, 18));
        badge.setForeground(Color.WHITE);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setHorizontalAlignment(SwingConstants.CENTER);
        badge.setVisible(false);
        return badge;
    }

    private void updateNotificationBadge() {
        if (notificationCount > 0) {
            notificationBadge.setText(String.valueOf(notificationCount));
            notificationBadge.setVisible(true);

            // Add pulse animation to badge
            Timer pulseTimer = new Timer(500, new ActionListener() {
                boolean visible = true;
                @Override
                public void actionPerformed(ActionEvent e) {
                    visible = !visible;
                    notificationBadge.setVisible(visible);
                }
            });
            pulseTimer.setRepeats(false);
            pulseTimer.start();
        } else {
            notificationBadge.setVisible(false);
        }
    }

    private JButton createHeaderButton(String icon, String tooltip) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(255, 255, 255, 80));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(255, 255, 255, 60));
                } else {
                    g2d.setColor(new Color(255, 255, 255, 40));
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Icon with better visibility - using proper font rendering
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(icon)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(icon, x, y);
            }
        };

        button.setPreferredSize(new Dimension(50, 50));
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText(tooltip);

        return button;
    }

    private JLabel createStaticLogo() {
        JLabel logoLabel = new JLabel();
        if (logoIcon != null) {
            logoLabel.setIcon(logoIcon);
        } else {
            logoLabel.setText("🏛️");
            logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
            logoLabel.setForeground(Color.WHITE);
        }
        return logoLabel;
    }

    private JPanel createMainContent() {
        JPanel content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Left sidebar - User profile and navigation
        content.add(createSidebar(), BorderLayout.WEST);

        // Center - Dashboard cards with animation
        content.add(createDashboardCards(), BorderLayout.CENTER);

        return content;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setOpaque(false);
        sidebar.setPreferredSize(new Dimension(280, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        // User profile card with glass effect
        sidebar.add(createUserProfileCard());
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        // Navigation shortcuts
        sidebar.add(createNavigationShortcuts());

        return sidebar;
    }

    private JPanel createUserProfileCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Glass morphism effect matching Register
                g2d.setColor(new Color(255, 255, 255, 35));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Border
                g2d.setColor(new Color(255, 255, 255, 60));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 20, 20);
            }
        };
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setOpaque(false);

        // User avatar section
        JPanel avatarPanel = new JPanel(new BorderLayout());
        avatarPanel.setOpaque(false);

        JLabel avatar = new JLabel("👋", SwingConstants.CENTER);
        avatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        avatar.setForeground(new Color(52, 152, 219));
        avatar.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        avatarPanel.add(avatar, BorderLayout.CENTER);

        // User information section
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        // Welcome message with user's actual name
        JLabel welcomeLabel = new JLabel("Welcome back!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // User's actual name (will be updated when user data is set)
        JLabel nameLabel = new JLabel(currentUser != null ? currentUser.getName() + " " + currentUser.getSurname() : "Student");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nameLabel.setForeground(new Color(46, 204, 113));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Student information
        JLabel studentIdLabel = new JLabel(currentUser != null ? currentUser.getStudentNumber() : "Student ID");
        studentIdLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        studentIdLabel.setForeground(new Color(200, 220, 240));
        studentIdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emailLabel = new JLabel(currentUser != null ? currentUser.getEmail() : "email@campus.edu");
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        emailLabel.setForeground(new Color(180, 200, 220));
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        infoPanel.add(welcomeLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        infoPanel.add(studentIdLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(emailLabel);

        // Accessibility status section
        JPanel statusPanel = createAccessibilityStatus();

        card.add(avatarPanel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(statusPanel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createAccessibilityStatus() {
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setOpaque(false);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        // Main status badge
        JPanel badgePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(46, 204, 113, 120));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            }
        };
        badgePanel.setOpaque(false);
        badgePanel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        badgePanel.setMaximumSize(new Dimension(250, 40));

        JLabel badgeText = new JLabel("♿ Accessibility Active");
        badgeText.setFont(new Font("Segoe UI", Font.BOLD, 12));
        badgeText.setForeground(new Color(255, 255, 255));

        badgePanel.add(badgeText);

        // Accessibility features summary
        JPanel featuresPanel = new JPanel();
        featuresPanel.setLayout(new BoxLayout(featuresPanel, BoxLayout.Y_AXIS));
        featuresPanel.setOpaque(false);
        featuresPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 5));

        if (currentUser != null) {
            // Show actual user accessibility preferences
            String deviceType = currentUser.getDeviceType() != null && !currentUser.getDeviceType().equals("None")
                    ? currentUser.getDeviceType() : "No specific device";

            String stairsPref = currentUser.isAvoidStairs() ? "Avoids stairs" : "Stairs OK";
            String rampsPref = currentUser.isPreferRamps() ? "Prefers ramps" : "No ramp preference";
            String widthPref = "Min width: " + currentUser.getMinPathWidthCm() + "cm";

            JLabel deviceLabel = new JLabel("📱 " + deviceType);
            JLabel stairsLabel = new JLabel("🚫 " + stairsPref);
            JLabel rampsLabel = new JLabel("↗️ " + rampsPref);
            JLabel widthLabel = new JLabel("📏 " + widthPref);

            Font featureFont = new Font("Segoe UI", Font.PLAIN, 10);
            Color featureColor = new Color(200, 220, 240);

            for (JLabel label : new JLabel[]{deviceLabel, stairsLabel, rampsLabel, widthLabel}) {
                label.setFont(featureFont);
                label.setForeground(featureColor);
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                featuresPanel.add(label);
                featuresPanel.add(Box.createRigidArea(new Dimension(0, 2)));
            }
        } else {
            // Default message when no user data
            JLabel defaultLabel = new JLabel("Configure your preferences in Settings");
            defaultLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
            defaultLabel.setForeground(new Color(200, 220, 240));
            defaultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            featuresPanel.add(defaultLabel);
        }

        statusPanel.add(badgePanel);
        statusPanel.add(featuresPanel);

        return statusPanel;
    }

    private JPanel createNavigationShortcuts() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        String[] shortcuts = {
                "🗺️ Interactive Map",
                "📊 Live Updates",
                "📅 Campus Events",
                "🔔 Notifications",
                "⚙️ Settings",
                "🏛️ Facilities",
                "👤 My Profile"
        };

        for (String shortcut : shortcuts) {
            JButton btn = createModernNavButton(shortcut);
            btn.addActionListener(e -> handleNavigation(shortcut));
            panel.add(btn);
            panel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        return panel;
    }

    private JPanel createDashboardCards() {
        JPanel cardsPanel = new JPanel(new GridBagLayout());
        cardsPanel.setOpaque(false);

        // Initialize animated cards
        featureCards = new AnimatedCard[4];
        String[] titles = {"Quick Navigation", "Campus Events", "Campus Facilities", "Live Updates"};
        String[] emojis = {"🧭", "📅", "🏛️", "📊"};
        String[] descriptions = {
                "Get instant directions to any campus location with accessibility-optimized routes",
                "Browse and filter accessible campus events with detailed information",
                "Discover buildings, facilities, and accessible amenities across campus",
                "Monitor real-time status of elevators, stairs, and campus facilities"
        };
        Color[] colors = {
                new Color(52, 152, 219),    // Blue for Navigation
                new Color(155, 89, 182),    // Purple for Events
                new Color(46, 204, 113),    // Green for Facilities
                new Color(41, 128, 185)     // Professional Blue for Live Updates
        };

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        for (int i = 0; i < 4; i++) {
            gbc.gridx = i % 2;
            gbc.gridy = i / 2;

            featureCards[i] = new AnimatedCard(emojis[i], titles[i], descriptions[i], colors[i], "Explore →", i);
            cardsPanel.add(featureCards[i], gbc);
            cardPositions[i] = new Point(0, 0);
        }

        return cardsPanel;
    }

    // Animated Card Class with 3D tile effect
    class AnimatedCard extends JPanel {
        private String emoji;
        private String title;
        private String description;
        private Color color;
        private String buttonText;
        private int cardIndex;

        private float scale = 1.0f;
        private boolean isHovered = false;
        private boolean isPressed = false;
        private int shadowOffset = 0;
        private float elevation = 0.0f;

        public AnimatedCard(String emoji, String title, String description, Color color, String buttonText, int index) {
            this.emoji = emoji;
            this.title = title;
            this.description = description;
            this.color = color;
            this.buttonText = buttonText;
            this.cardIndex = index;

            setOpaque(false);
            setPreferredSize(new Dimension(300, 250));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            setupAnimations();
            addMouseListener(new CardMouseAdapter());
        }

        private void setupAnimations() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    if (activeCardIndex != cardIndex) {
                        animateCard(cardIndex, 1.05f);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    if (activeCardIndex != cardIndex) {
                        animateCard(cardIndex, 1.0f);
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    isPressed = true;
                    animateCardPress(cardIndex, true);
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    isPressed = false;
                    animateCardPress(cardIndex, false);
                    handleCardClick();
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            int width = getWidth();
            int height = getHeight();

            // Calculate scaled dimensions
            int scaledWidth = (int)(width * scale);
            int scaledHeight = (int)(height * scale);
            int xOffset = (width - scaledWidth) / 2;
            int yOffset = (height - scaledHeight) / 2 - (int)(elevation * 10);

            // Draw shadow
            if (shadowOffset > 0) {
                g2d.setColor(new Color(0, 0, 0, 80 - shadowOffset * 2));
                g2d.fillRoundRect(xOffset + shadowOffset, yOffset + shadowOffset,
                        scaledWidth, scaledHeight, 25, 25);
            }

            // Draw card background with gradient
            GradientPaint gradient = new GradientPaint(
                    0, yOffset, color.brighter(),
                    0, yOffset + scaledHeight, color.darker()
            );
            g2d.setPaint(gradient);
            g2d.fillRoundRect(xOffset, yOffset, scaledWidth, scaledHeight, 25, 25);

            // Draw border
            g2d.setColor(new Color(255, 255, 255, 80));
            g2d.setStroke(new BasicStroke(2f));
            g2d.drawRoundRect(xOffset, yOffset, scaledWidth, scaledHeight, 25, 25);

            // Draw content
            drawCardContent(g2d, xOffset, yOffset, scaledWidth, scaledHeight);
        }

        private void drawCardContent(Graphics2D g2d, int x, int y, int width, int height) {
            int padding = 20;

            // Emoji - Larger and more visible
            g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
            FontMetrics emojiMetrics = g2d.getFontMetrics();
            int emojiWidth = emojiMetrics.stringWidth(emoji);
            g2d.drawString(emoji, x + (width - emojiWidth) / 2, y + padding + 40);

            // Title
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
            g2d.setColor(Color.WHITE);
            FontMetrics titleMetrics = g2d.getFontMetrics();
            int titleWidth = titleMetrics.stringWidth(title);
            g2d.drawString(title, x + (width - titleWidth) / 2, y + padding + 80);

            // Description
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g2d.setColor(new Color(255, 255, 255, 220));
            drawWrappedText(g2d, description, x + padding, y + padding + 100, width - 2 * padding, 60);

            // Button
            drawCardButton(g2d, x, y, width, height);
        }

        private void drawWrappedText(Graphics2D g2d, String text, int x, int y, int maxWidth, int maxHeight) {
            String[] words = text.split(" ");
            StringBuilder line = new StringBuilder();
            int lineHeight = g2d.getFontMetrics().getHeight();
            int currentY = y;

            for (String word : words) {
                String testLine = line + (line.length() == 0 ? "" : " ") + word;
                int testWidth = g2d.getFontMetrics().stringWidth(testLine);

                if (testWidth > maxWidth) {
                    if (line.length() > 0) {
                        g2d.drawString(line.toString(), x, currentY);
                        currentY += lineHeight;
                        if (currentY > y + maxHeight) break;
                    }
                    line = new StringBuilder(word);
                } else {
                    line.append(line.length() == 0 ? "" : " ").append(word);
                }
            }

            if (line.length() > 0 && currentY <= y + maxHeight) {
                g2d.drawString(line.toString(), x, currentY);
            }
        }

        private void drawCardButton(Graphics2D g2d, int x, int y, int width, int height) {
            int buttonWidth = 120;
            int buttonHeight = 35;
            int buttonX = x + (width - buttonWidth) / 2;
            int buttonY = y + height - 50;

            // Button background
            Color btnColor = isPressed ? color.darker().darker() :
                    isHovered ? color.brighter() : color;
            g2d.setColor(btnColor);
            g2d.fillRoundRect(buttonX, buttonY, buttonWidth, buttonHeight, 15, 15);

            // Button text
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
            FontMetrics btnMetrics = g2d.getFontMetrics();
            int textWidth = btnMetrics.stringWidth(buttonText);
            int textX = buttonX + (buttonWidth - textWidth) / 2;
            int textY = buttonY + (buttonHeight - btnMetrics.getHeight()) / 2 + btnMetrics.getAscent();
            g2d.drawString(buttonText, textX, textY);
        }

        private void handleCardClick() {
            switch (title) {
                case "Quick Navigation":
                    openInteractiveMap();
                    break;
                case "Campus Events":
                    openEvents();
                    break;
                case "Campus Facilities":
                    openFacilities();
                    break;
                case "Live Updates":
                    openLiveUpdates();
                    break;
            }

            // Animate card press effect
            if (activeCardIndex != cardIndex) {
                setActiveCard(cardIndex);
            }
        }
    }

    private class CardMouseAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            AnimatedCard card = (AnimatedCard) e.getSource();
            card.handleCardClick();
        }
    }

    private void setActiveCard(int index) {
        int previousActive = activeCardIndex;
        activeCardIndex = index;

        // Reset all cards to normal scale
        for (int i = 0; i < 4; i++) {
            if (i != index) {
                animateCard(i, 1.0f);
            }
        }

        // Bring active card forward
        if (index != -1) {
            animateCard(index, 1.1f);
        }
    }

    private void animateCard(int cardIndex, float targetScale) {
        Timer animTimer = new Timer(10, new ActionListener() {
            float currentScale = cardScales[cardIndex];

            @Override
            public void actionPerformed(ActionEvent e) {
                float diff = targetScale - currentScale;
                if (Math.abs(diff) < 0.01f) {
                    currentScale = targetScale;
                    ((Timer)e.getSource()).stop();
                } else {
                    currentScale += diff * 0.3f;
                }

                cardScales[cardIndex] = currentScale;
                if (featureCards[cardIndex] != null) {
                    featureCards[cardIndex].scale = currentScale;
                    featureCards[cardIndex].repaint();
                }
            }
        });
        animTimer.start();
    }

    private void animateCardPress(int cardIndex, boolean pressed) {
        featureCards[cardIndex].shadowOffset = pressed ? 2 : 0;
        featureCards[cardIndex].elevation = pressed ? -0.1f : 0.0f;
        featureCards[cardIndex].isPressed = pressed;
        featureCards[cardIndex].repaint();
    }

    private JPanel createBottomBar() {
        JPanel bottomBar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(255, 255, 255, 15));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bottomBar.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        bottomBar.setOpaque(false);

        JLabel statusLabel = new JLabel("🟢 System Online • ♿ Accessibility Active • 📍 Location Services Enabled");
        statusLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(200, 220, 240));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setOpaque(false);

        JButton helpBtn = createTextButton("Help");
        JButton feedbackBtn = createTextButton("Feedback");
        JButton eventsBtn = createTextButton("Events");
        JButton logoutBtn = createTextButton("Logout");

        // Add action listeners
        helpBtn.addActionListener(e -> openHelp());
        feedbackBtn.addActionListener(e -> openFeedback());
        eventsBtn.addActionListener(e -> openEvents());
        logoutBtn.addActionListener(e -> logout());

        buttonPanel.add(helpBtn);
        buttonPanel.add(feedbackBtn);
        buttonPanel.add(eventsBtn);
        buttonPanel.add(logoutBtn);

        bottomBar.add(statusLabel, BorderLayout.WEST);
        bottomBar.add(buttonPanel, BorderLayout.EAST);

        return bottomBar;
    }

    private JButton createModernNavButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(255, 255, 255, 60));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(255, 255, 255, 40));
                } else {
                    g2d.setColor(new Color(255, 255, 255, 20));
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                // Text
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
                FontMetrics fm = g2d.getFontMetrics();
                int x = 20; // Left aligned
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(getText(), x, y);
            }
        };

        button.setPreferredSize(new Dimension(250, 45));
        button.setMaximumSize(new Dimension(250, 45));
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);

        return button;
    }

    private JButton createTextButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setForeground(new Color(200, 220, 240));
        button.setBackground(new Color(255, 255, 255, 0));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(Color.WHITE);
                button.setBackground(new Color(255, 255, 255, 30));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(new Color(200, 220, 240));
                button.setBackground(new Color(255, 255, 255, 0));
            }
        });

        return button;
    }

    // ===== ANIMATION AND DYNAMIC UPDATES =====

    private void startAnimations() {
        // Real-time clock
        clockTimer = new Timer(1000, e -> updateTimeAndGreeting());
        clockTimer.start();
        updateTimeAndGreeting();

        // Subtle background animation
        animationTimer = new Timer(50, e -> {
            repaint();
        });
        animationTimer.start();
    }

    private void startNotificationTimer() {
        // Notification timer - shows notification every 1 minute
        notificationTimer = new Timer(60000, new ActionListener() { // 60000 ms = 1 minute
            @Override
            public void actionPerformed(ActionEvent e) {
                showRandomNotification();
            }
        });
        notificationTimer.start();
    }

    private void showRandomNotification() {
        notificationCount++;
        updateNotificationBadge();

        // Random notification messages
        String[] notifications = {
                "🚨 Elevator maintenance scheduled for Engineering Building",
                "📢 New accessible route available to Library",
                "⚠️ Staircase B temporarily closed for repairs",
                "🎓 Campus tour starting in 15 minutes",
                "🔧 Accessibility features updated in Business Wing",
                "📚 Study rooms available in Library",
                "🍽️ Cafeteria special menu today",
                "🚌 Shuttle service running on revised schedule"
        };

        Random rand = new Random();
        String notification = notifications[rand.nextInt(notifications.length)];

        // Show notification popup
        JOptionPane.showMessageDialog(this,
                "<html><div style='text-align: center; width: 300px;'>" +
                        "<h3 style='color: #2e86c1;'>🔔 Campus Notification</h3>" +
                        "<p>" + notification + "</p>" +
                        "<small>Notification #" + notificationCount + "</small>" +
                        "</div></html>",
                "Campus Alert",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateTimeAndGreeting() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy");

        String currentTime = timeFormat.format(new Date());
        String currentDate = dateFormat.format(new Date());

        timeLabel.setText(currentTime);

        String greeting = getTimeBasedGreeting();
        if (currentUser != null) {
            greetingLabel.setText(greeting + ", " + currentUser.getName() + "! • " + currentDate);
        } else {
            greetingLabel.setText(greeting + "! • " + currentDate);
        }
    }

    private String getTimeBasedGreeting() {
        int hour = Integer.parseInt(new SimpleDateFormat("HH").format(new Date()));
        if (hour < 12) return "Good Morning";
        else if (hour < 17) return "Good Afternoon";
        else return "Good Evening";
    }

    // ===== NAVIGATION HANDLERS =====

    private void handleNavigation(String action) {
        if (action.contains("Map")) {
            openInteractiveMap();
        } else if (action.contains("Live Updates")) {
            openLiveUpdates();
        } else if (action.contains("Events")) {
            openEvents();
        } else if (action.contains("Notifications")) {
            openNotifications();
        } else if (action.contains("Settings")) {
            openSettings();
        } else if (action.contains("Facilities")) {
            openFacilities();
        } else if (action.contains("Profile")) {
            openProfile();
        }
    }

    private void openLiveUpdates() {
        try {
            if (liveUpdatesFrame == null || !liveUpdatesFrame.isVisible()) {
                liveUpdatesFrame = new LiveUpdates();
                liveUpdatesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                liveUpdatesFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        liveUpdatesFrame = null;
                    }
                });
            }
            liveUpdatesFrame.setVisible(true);
            liveUpdatesFrame.toFront();
        } catch (Exception e) {
            showFeatureMessage("Live Updates", "Real-time campus status and accessibility information");
        }
    }

    private void openInteractiveMap() {
        try {
            if (mapFrame == null || !mapFrame.isVisible()) {
                mapFrame = new CampusMap();
                mapFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                mapFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        mapFrame = null;
                    }
                });
            }
            mapFrame.setVisible(true);
            mapFrame.toFront();
        } catch (Exception e) {
            showFeatureMessage("Interactive Map", "Explore campus with accessibility-optimized routes");
        }
    }

    private void openEvents() {
        try {
            if (eventsFrame == null || !eventsFrame.isVisible()) {
                eventsFrame = new AvailableEventsGUI();
                eventsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                eventsFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        eventsFrame = null;
                    }
                });
            }
            eventsFrame.setVisible(true);
            eventsFrame.toFront();
        } catch (Exception e) {
            showFeatureMessage("Campus Events", "Browse and filter accessible campus events");
        }
    }

    // ===== ENHANCED NOTIFICATIONS METHOD =====
    private void openNotifications() {
        // Create and show the modern notifications page
        if (notificationsPage == null || !notificationsPage.isVisible()) {
            notificationsPage = new NotificationsPage(this);
            notificationsPage.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    // Clear notifications when the notifications page is closed
                    notificationCount = 0;
                    updateNotificationBadge();
                }

                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    // Clear notifications when the notifications page is closing
                    notificationCount = 0;
                    updateNotificationBadge();
                }
            });
        }

        // Center the notifications page relative to dashboard
        notificationsPage.setLocationRelativeTo(this);
        notificationsPage.setVisible(true);

        // Bring to front and request focus
        notificationsPage.toFront();
        notificationsPage.requestFocus();

        // Optional: Add opening animation effect
        animateNotificationOpening();
    }

    private void animateNotificationOpening() {
        // Add a subtle animation when opening notifications
        Timer openTimer = new Timer(10, new ActionListener() {
            float scale = 0.8f;
            @Override
            public void actionPerformed(ActionEvent e) {
                scale += 0.05f;
                if (scale >= 1.0f) {
                    scale = 1.0f;
                    ((Timer)e.getSource()).stop();
                }
            }
        });
        openTimer.start();
    }

    private void openSettings() {
        try {
            SettingsPage settingsDialog = new SettingsPage(this);
            settingsDialog.setVisible(true);
        } catch (Exception e) {
            showFeatureMessage("Settings", "Configure your preferences and accessibility options");
        }
    }

    private void openFacilities() {
        try {
            JFrame facilitiesFrame = new JFrame("Campus Facilities");
            facilitiesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            facilitiesFrame.add(new FacultyPage());
            facilitiesFrame.pack();
            facilitiesFrame.setSize(900, 700);
            facilitiesFrame.setLocationRelativeTo(this);
            facilitiesFrame.setVisible(true);
        } catch (Exception e) {
            showFeatureMessage("Campus Facilities", "Discover accessible buildings and amenities");
        }
    }

    private void openProfile() {
        try {
            ProfilePage profileDialog = new ProfilePage(this, currentUser);
            profileDialog.setVisible(true);
        } catch (Exception e) {
            showFeatureMessage("User Profile", "Manage your account and accessibility settings");
        }
    }

    private void openHelp() {
        try {
            HelpPage helpDialog = new HelpPage(this);
            helpDialog.setVisible(true);
        } catch (Exception e) {
            showFeatureMessage("Help & Support", "Get assistance and learn how to use the app");
        }
    }

    private void openFeedback() {
        try {
            FeedbackPage feedbackDialog = new FeedbackPage(this);
            feedbackDialog.setVisible(true);
        } catch (Exception e) {
            showFeatureMessage("Feedback", "Share your feedback and report issues");
        }
    }

    private void showFeatureMessage(String title, String message) {
        JOptionPane.showMessageDialog(this,
                "<html><div style='text-align: center; width: 250px;'><b>" + title + "</b><br><br>" + message + "</div></html>",
                title,
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "<html><div style='text-align: center;'>Are you sure you want to logout?</div></html>",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (clockTimer != null) clockTimer.stop();
            if (animationTimer != null) animationTimer.stop();
            if (notificationTimer != null) notificationTimer.stop();

            if (mapFrame != null) mapFrame.dispose();
            if (settingsFrame != null) settingsFrame.dispose();
            if (liveUpdatesFrame != null) liveUpdatesFrame.dispose();
            if (eventsFrame != null) eventsFrame.dispose();
            if (notificationsPage != null) notificationsPage.dispose();

            this.dispose();
            new Login().setVisible(true);
        }
    }

    public void setUserData(User user) {
        this.currentUser = user;
        updateUserInterface();
    }

    private void updateUserInterface() {
        if (currentUser != null && greetingLabel != null) {
            String greeting = getTimeBasedGreeting();
            greetingLabel.setText(greeting + ", " + currentUser.getName() + "! • " +
                    new SimpleDateFormat("EEEE, MMMM d, yyyy").format(new Date()));

            // Refresh the sidebar to show actual user data
            refreshSidebar();
        }
    }

    private void refreshSidebar() {
        // This would typically involve recreating or updating the sidebar components
        // For now, we'll just repaint
        repaint();
    }

    private void loadLogo() {
        try {
            logoIcon = new ImageIcon("resources/images/20251013_0428_Campus Access Logo_simple_compose_01k7dp8zxzftq9kghhg6typvaz.png");
            if (logoIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                Image image = logoIcon.getImage();
                Image resizedImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                logoIcon = new ImageIcon(resizedImage);
            } else {
                logoIcon = null;
            }
        } catch (Exception e) {
            logoIcon = null;
        }
    }

    // Method to add notifications from other parts of the application
    public void addNotification(String message, String priority) {
        notificationCount++;
        updateNotificationBadge();

        // Optional: Show a toast notification
        showToastNotification("New Notification: " + message);
    }

    private void showToastNotification(String message) {
        // Create a toast-like notification
        JWindow toastWindow = new JWindow(this);
        toastWindow.setSize(300, 60);
        toastWindow.setLocationRelativeTo(this);
        toastWindow.setAlwaysOnTop(true);

        JPanel toastPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(33, 64, 98, 230));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        toastPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        toastPanel.setOpaque(false);

        JLabel toastLabel = new JLabel(message);
        toastLabel.setForeground(Color.WHITE);
        toastLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        toastPanel.add(toastLabel, BorderLayout.CENTER);
        toastWindow.add(toastPanel);
        toastWindow.setVisible(true);

        // Auto-close after 3 seconds
        Timer closeTimer = new Timer(3000, e -> toastWindow.dispose());
        closeTimer.setRepeats(false);
        closeTimer.start();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new Dashboard().setVisible(true);
        });
    }
}