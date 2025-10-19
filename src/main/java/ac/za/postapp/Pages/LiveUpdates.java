package ac.za.postapp.Pages;

// emeritusApex

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;
import java.util.Random;

public class LiveUpdates extends JFrame {
    private JButton backToDashboardButton;
    private JPanel mainPanel;
    private Timer statusUpdateTimer;
    private Timer animationTimer;
    private HashMap<String, FacilityStatus> facilityStatus;
    private Random random;
    private float pulseValue = 0f;
    private boolean pulseDirection = true;

    // Magical color scheme
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Color WARNING_COLOR = new Color(243, 156, 18);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color DARK_BG = new Color(23, 32, 42);
    private final Color LIGHT_BG = new Color(236, 240, 241);
    private final Color GLASS_COLOR = new Color(255, 255, 255, 40);
    private final Color NEON_BLUE = new Color(0, 191, 255);
    private final Color NEON_GREEN = new Color(57, 255, 20);
    private final Color NEON_PURPLE = new Color(138, 43, 226);

    public LiveUpdates() {
        setTitle("Campus Access Guide - Live Facility Updates");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setResizable(true);

        initializeFacilityStatus();
        setupUI();
        startStatusUpdates();
        startAnimations();
    }

    private void initializeFacilityStatus() {
        facilityStatus = new HashMap<>();
        random = new Random();

        // Elevators with more detailed status
        facilityStatus.put("Elevator A - Engineering Wing", new FacilityStatus("Operational", "All systems normal • Speed: 2.5m/s", SUCCESS_COLOR, 95));
        facilityStatus.put("Elevator B - Health Sciences", new FacilityStatus("Maintenance", "Scheduled maintenance • ETA: 4:00 PM", WARNING_COLOR, 35));
        facilityStatus.put("Elevator C - Business Wing", new FacilityStatus("Operational", "Running smoothly • Capacity: 12 people", SUCCESS_COLOR, 88));
        facilityStatus.put("Elevator D - Library", new FacilityStatus("Out of Service", "Motor replacement • Parts ordered", DANGER_COLOR, 5));
        facilityStatus.put("Elevator E - Administration", new FacilityStatus("Operational", "Recently serviced • Next check: 2 weeks", SUCCESS_COLOR, 92));

        // Stairs with traffic levels
        facilityStatus.put("Stairs A - Main Building East", new FacilityStatus("Clear", "No obstructions • Low traffic", SUCCESS_COLOR, 85));
        facilityStatus.put("Stairs B - Main Building West", new FacilityStatus("Partial Blockage", "Cleaning in progress • Use alternative", WARNING_COLOR, 45));
        facilityStatus.put("Stairs C - Science Wing", new FacilityStatus("Clear", "Fully accessible • Moderate traffic", SUCCESS_COLOR, 72));
        facilityStatus.put("Stairs D - Library Wing", new FacilityStatus("Closed", "Structural repairs • Reopening: Tomorrow", DANGER_COLOR, 0));
        facilityStatus.put("Stairs E - Student Center", new FacilityStatus("Clear", "Normal access • High traffic", SUCCESS_COLOR, 68));

        // Ramps with accessibility scores
        facilityStatus.put("Ramp A - Main Entrance", new FacilityStatus("Operational", "Wheelchair accessible • Slope: 1:12", SUCCESS_COLOR, 94));
        facilityStatus.put("Ramp B - Library Entrance", new FacilityStatus("Maintenance", "Handrail installation • 30 min delay", WARNING_COLOR, 28));
        facilityStatus.put("Ramp C - Science Building", new FacilityStatus("Operational", "Good condition • Non-slip surface", SUCCESS_COLOR, 89));

        // Automatic Doors
        facilityStatus.put("Auto Door A - Main Entrance", new FacilityStatus("Operational", "Sensor working properly • Fast response", SUCCESS_COLOR, 96));
        facilityStatus.put("Auto Door B - Library", new FacilityStatus("Manual Mode", "Sensor calibration needed • Push to open", WARNING_COLOR, 42));
        facilityStatus.put("Auto Door C - Cafeteria", new FacilityStatus("Operational", "Normal operation • Delay: 2s", SUCCESS_COLOR, 91));

        // Restrooms
        facilityStatus.put("Restroom A - Ground Floor", new FacilityStatus("Available", "Recently cleaned • Supplies stocked", SUCCESS_COLOR, 87));
        facilityStatus.put("Restroom B - First Floor", new FacilityStatus("Cleaning", "Temporarily closed • 15 min estimate", WARNING_COLOR, 22));
        facilityStatus.put("Accessible Restroom - Library", new FacilityStatus("Available", "Fully equipped • Emergency cord tested", SUCCESS_COLOR, 93));
    }

    private void setupUI() {
        // Main container with animated background
        mainPanel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Animated gradient background
                float animation = (float) (0.5 + 0.3 * Math.sin(pulseValue * 0.5));
                GradientPaint gradient = new GradientPaint(
                        0, 0, blendColors(new Color(23, 32, 42), new Color(44, 62, 80), animation),
                        getWidth(), getHeight(), blendColors(new Color(52, 73, 94), new Color(30, 40, 55), 1 - animation)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Floating particles effect
                g2d.setColor(new Color(255, 255, 255, 20));
                for (int i = 0; i < 50; i++) {
                    float x = (float) (Math.sin(pulseValue * 0.1 + i) * getWidth() / 4 + getWidth() / 2);
                    float y = (float) (Math.cos(pulseValue * 0.08 + i) * getHeight() / 4 + getHeight() / 2);
                    float size = (float) (2 + Math.sin(pulseValue * 0.05 + i) * 1.5);
                    g2d.fill(new Ellipse2D.Float(x, y, size, size));
                }

                // Grid lines with animation
                g2d.setColor(new Color(255, 255, 255, 8));
                g2d.setStroke(new BasicStroke(1));
                int gridSize = 40;
                for (int x = 0; x < getWidth(); x += gridSize) {
                    float offset = (float) Math.sin(pulseValue * 0.02 + x * 0.01) * 2;
                    g2d.drawLine(x + (int)offset, 0, x + (int)offset, getHeight());
                }
                for (int y = 0; y < getHeight(); y += gridSize) {
                    float offset = (float) Math.cos(pulseValue * 0.02 + y * 0.01) * 2;
                    g2d.drawLine(0, y + (int)offset, getWidth(), y + (int)offset);
                }
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Add header
        mainPanel.add(createAnimatedHeader(), BorderLayout.NORTH);

        // Add main content
        mainPanel.add(createContentPanel(), BorderLayout.CENTER);

        // Add bottom navigation
        mainPanel.add(createBottomPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createAnimatedHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Animated title with gradient text
        JLabel titleLabel = new JLabel("🚀 Campus Facility Live Status", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Animated text gradient
                GradientPaint textGradient = new GradientPaint(
                        0, 0, blendColors(NEON_BLUE, NEON_PURPLE, (float) (0.5 + 0.5 * Math.sin(pulseValue))),
                        getWidth(), 0, blendColors(NEON_GREEN, NEON_BLUE, (float) (0.5 + 0.5 * Math.cos(pulseValue)))
                );
                g2d.setPaint(textGradient);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                String text = getText();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(text, x, y);
            }
        };
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);

        // Subtitle with pulse effect
        JLabel subtitleLabel = new JLabel("Real-time monitoring with animated status indicators • Live updates every 5 seconds", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                float alpha = (float) (0.7 + 0.3 * Math.sin(pulseValue * 2));
                g2d.setColor(new Color(200, 200, 200, (int)(alpha * 255)));
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                String text = getText();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(text, x, y);
            }
        };
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);

        header.add(titlePanel, BorderLayout.CENTER);

        return header;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 25, 0));
        contentPanel.setOpaque(false);

        // Left panel - Elevators with animated charts
        contentPanel.add(createAnimatedFacilityPanel("Elevators Status", "⬆️", getElevatorStatus(), true));

        // Right panel - Stairs and Access with animated indicators
        contentPanel.add(createAnimatedFacilityPanel("Stairs & Accessibility", "♿", getStairsAndAccessStatus(), false));

        return contentPanel;
    }

    private JPanel createAnimatedFacilityPanel(String title, String icon, String[][] facilities, boolean showCharts) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        // Glass effect container with animated border
        JPanel glassPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Animated glass background
                float alpha = (float) (0.1 + 0.05 * Math.sin(pulseValue));
                g2d.setColor(new Color(255, 255, 255, (int)(alpha * 255)));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                // Animated border
                GradientPaint borderGradient = new GradientPaint(
                        0, 0, blendColors(SECONDARY_COLOR, NEON_PURPLE, (float) Math.sin(pulseValue)),
                        getWidth(), getHeight(), blendColors(NEON_BLUE, SECONDARY_COLOR, (float) Math.cos(pulseValue))
                );
                g2d.setPaint(borderGradient);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 25, 25);
            }
        };
        glassPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        glassPanel.setOpaque(false);

        // Animated header
        JLabel headerLabel = new JLabel(icon + " " + title, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint textGradient = new GradientPaint(
                        0, 0, Color.WHITE,
                        getWidth(), 0, blendColors(Color.WHITE, NEON_BLUE, 0.3f)
                );
                g2d.setPaint(textGradient);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                String text = getText();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(text, x, y);
            }
        };
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        glassPanel.add(headerLabel, BorderLayout.NORTH);

        // Facilities list with animated cards
        JPanel facilitiesPanel = new JPanel();
        facilitiesPanel.setLayout(new BoxLayout(facilitiesPanel, BoxLayout.Y_AXIS));
        facilitiesPanel.setOpaque(false);

        for (String[] facility : facilities) {
            facilitiesPanel.add(createAnimatedStatusCard(facility[0], facility[1], facility[2], showCharts));
            facilitiesPanel.add(Box.createRigidArea(new Dimension(0, 12)));
        }

        JScrollPane scrollPane = new JScrollPane(facilitiesPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Custom animated scroll bar
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUI(new ModernScrollBarUI());

        glassPanel.add(scrollPane, BorderLayout.CENTER);
        panel.add(glassPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAnimatedStatusCard(String facilityName, String status, String colorStr, boolean showChart) {
        JPanel card = new JPanel(new BorderLayout(15, 5));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // Parse color from string
        Color statusColor = parseColor(colorStr);
        FacilityStatus facility = facilityStatus.get(facilityName);
        int efficiency = facility != null ? facility.efficiency : 75;

        // Animated status indicator with pulse
        JPanel indicatorPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Pulsing outer ring
                float pulseAlpha = (float) (0.3 + 0.2 * Math.sin(pulseValue * 3));
                g2d.setColor(new Color(statusColor.getRed(), statusColor.getGreen(), statusColor.getBlue(), (int)(pulseAlpha * 255)));
                g2d.fillOval(2, 2, getWidth()-4, getHeight()-4);

                // Inner circle
                g2d.setColor(statusColor);
                g2d.fillOval(6, 6, getWidth()-12, getHeight()-12);

                // Glow effect
                g2d.setColor(new Color(255, 255, 255, 100));
                g2d.fillOval(8, 8, getWidth()-16, getHeight()-16);
            }
        };
        indicatorPanel.setPreferredSize(new Dimension(20, 20));
        indicatorPanel.setOpaque(false);

        // Facility info with animated elements
        JPanel infoPanel = new JPanel(new BorderLayout(5, 5));
        infoPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(facilityName);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nameLabel.setForeground(Color.WHITE);

        JLabel statusLabel = new JLabel(facility != null ? facility.status : status) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Animated status text
                float alpha = (float) (0.8 + 0.2 * Math.sin(pulseValue * 2));
                Color animatedColor = new Color(statusColor.getRed(), statusColor.getGreen(), statusColor.getBlue(), (int)(alpha * 255));
                g2d.setColor(animatedColor);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                String text = getText();
                g2d.drawString(text, 0, fm.getAscent());
            }
        };
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));

        JLabel descLabel = new JLabel(facility != null ? facility.description : "");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        descLabel.setForeground(new Color(180, 180, 180));

        infoPanel.add(nameLabel, BorderLayout.NORTH);
        infoPanel.add(statusLabel, BorderLayout.CENTER);
        infoPanel.add(descLabel, BorderLayout.SOUTH);

        // Animated progress chart for elevators
        if (showChart) {
            JPanel chartPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    int width = getWidth();
                    int height = getHeight();

                    // Background
                    g2d.setColor(new Color(255, 255, 255, 30));
                    g2d.fillRoundRect(0, 0, width, height, 10, 10);

                    // Animated progress bar
                    float progress = efficiency / 100.0f;
                    float animatedProgress = progress * (0.9f + 0.1f * (float) Math.sin(pulseValue * 2));

                    GradientPaint progressGradient = new GradientPaint(
                            0, 0, statusColor.brighter(),
                            width, 0, statusColor.darker()
                    );
                    g2d.setPaint(progressGradient);
                    g2d.fillRoundRect(2, 2, (int)((width-4) * animatedProgress), height-4, 8, 8);

                    // Percentage text
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Segoe UI", Font.BOLD, 10));
                    String percentText = efficiency + "%";
                    FontMetrics fm = g2d.getFontMetrics();
                    int textX = (width - fm.stringWidth(percentText)) / 2;
                    int textY = (height - fm.getHeight()) / 2 + fm.getAscent();
                    g2d.drawString(percentText, textX, textY);
                }
            };
            chartPanel.setPreferredSize(new Dimension(80, 20));
            chartPanel.setOpaque(false);

            JPanel rightPanel = new JPanel(new BorderLayout());
            rightPanel.setOpaque(false);
            rightPanel.add(chartPanel, BorderLayout.CENTER);

            card.add(rightPanel, BorderLayout.EAST);
        }

        card.add(indicatorPanel, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);

        // Glass effect container for card
        JPanel glassCard = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Animated glass background
                float alpha = (float) (0.08 + 0.04 * Math.sin(pulseValue + facilityName.hashCode() * 0.1));
                g2d.setColor(new Color(255, 255, 255, (int)(alpha * 255)));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Subtle border
                g2d.setColor(new Color(255, 255, 255, 40));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
            }
        };
        glassCard.setOpaque(false);
        glassCard.add(card, BorderLayout.CENTER);

        return glassCard;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        // Animated last update time
        JLabel updateLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                String text = "Last updated: " + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
                float alpha = (float) (0.7 + 0.3 * Math.sin(pulseValue * 4));
                g2d.setColor(new Color(200, 200, 200, (int)(alpha * 255)));
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                g2d.drawString(text, 0, fm.getAscent());
            }
        };
        updateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        // Legend with animated indicators
        JPanel legendPanel = createAnimatedLegend();

        // Back button
        backToDashboardButton = createMagicalButton("Back to Dashboard", SECONDARY_COLOR);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(backToDashboardButton);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.add(updateLabel, BorderLayout.WEST);
        leftPanel.add(legendPanel, BorderLayout.EAST);

        bottomPanel.add(leftPanel, BorderLayout.WEST);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        return bottomPanel;
    }

    private JPanel createAnimatedLegend() {
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        legendPanel.setOpaque(false);

        String[][] legendItems = {
                {"Operational", String.valueOf(SUCCESS_COLOR.getRGB())},
                {"Maintenance", String.valueOf(WARNING_COLOR.getRGB())},
                {"Out of Service", String.valueOf(DANGER_COLOR.getRGB())}
        };

        for (String[] item : legendItems) {
            JPanel legendItem = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            legendItem.setOpaque(false);

            JPanel indicator = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    Color color = parseColor(item[1]);
                    float pulse = (float) (0.5 + 0.5 * Math.sin(pulseValue * 2 + item[0].hashCode() * 0.1));
                    g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(pulse * 255)));
                    g2d.fillOval(2, 2, getWidth()-4, getHeight()-4);
                }
            };
            indicator.setPreferredSize(new Dimension(12, 12));
            indicator.setOpaque(false);

            JLabel label = new JLabel(item[0]);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            label.setForeground(new Color(200, 200, 200));

            legendItem.add(indicator);
            legendItem.add(label);
            legendPanel.add(legendItem);
        }

        return legendPanel;
    }

    private JButton createMagicalButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color currentColor = baseColor;
                if (!isEnabled()) {
                    currentColor = Color.GRAY;
                } else if (getModel().isPressed()) {
                    currentColor = baseColor.darker().darker();
                } else if (getModel().isRollover()) {
                    currentColor = baseColor.brighter();
                    // Add extra glow on hover
                    g2.setColor(new Color(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), 80));
                    g2.fillRoundRect(-5, -5, getWidth()+10, getHeight()+10, 35, 35);
                }

                // Animated gradient
                GradientPaint gradient = new GradientPaint(
                        0, 0, blendColors(currentColor.brighter(), NEON_BLUE, (float) Math.sin(pulseValue) * 0.3f),
                        0, getHeight(), blendColors(currentColor.darker(), NEON_PURPLE, (float) Math.cos(pulseValue) * 0.3f)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                // Shine effect
                g2.setColor(new Color(255, 255, 255, 100));
                g2.fillRoundRect(3, 3, getWidth()-6, getHeight()/3, 27, 27);

                // Animated border
                GradientPaint borderGradient = new GradientPaint(
                        0, 0, blendColors(currentColor.darker().darker(), NEON_BLUE, (float) Math.sin(pulseValue) * 0.5f),
                        getWidth(), getHeight(), blendColors(NEON_PURPLE, currentColor.darker().darker(), (float) Math.cos(pulseValue) * 0.5f)
                );
                g2.setPaint(borderGradient);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 30, 30);

                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            public void setContentAreaFilled(boolean b) {
                // Override to prevent default painting
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> {
            // Return to dashboard
            this.dispose();
        });

        return button;
    }

    private String[][] getElevatorStatus() {
        return new String[][] {
                {"Elevator A - Engineering Wing", getStatusWithColor("Elevator A - Engineering Wing"), String.valueOf(SUCCESS_COLOR.getRGB())},
                {"Elevator B - Health Sciences", getStatusWithColor("Elevator B - Health Sciences"), String.valueOf(WARNING_COLOR.getRGB())},
                {"Elevator C - Business Wing", getStatusWithColor("Elevator C - Business Wing"), String.valueOf(SUCCESS_COLOR.getRGB())},
                {"Elevator D - Library", getStatusWithColor("Elevator D - Library"), String.valueOf(DANGER_COLOR.getRGB())},
                {"Elevator E - Administration", getStatusWithColor("Elevator E - Administration"), String.valueOf(SUCCESS_COLOR.getRGB())}
        };
    }

    private String[][] getStairsAndAccessStatus() {
        return new String[][] {
                {"Stairs A - Main Building East", getStatusWithColor("Stairs A - Main Building East"), String.valueOf(SUCCESS_COLOR.getRGB())},
                {"Stairs B - Main Building West", getStatusWithColor("Stairs B - Main Building West"), String.valueOf(WARNING_COLOR.getRGB())},
                {"Stairs C - Science Wing", getStatusWithColor("Stairs C - Science Wing"), String.valueOf(SUCCESS_COLOR.getRGB())},
                {"Stairs D - Library Wing", getStatusWithColor("Stairs D - Library Wing"), String.valueOf(DANGER_COLOR.getRGB())},
                {"Stairs E - Student Center", getStatusWithColor("Stairs E - Student Center"), String.valueOf(SUCCESS_COLOR.getRGB())},
                {"Ramp A - Main Entrance", getStatusWithColor("Ramp A - Main Entrance"), String.valueOf(SUCCESS_COLOR.getRGB())},
                {"Ramp B - Library Entrance", getStatusWithColor("Ramp B - Library Entrance"), String.valueOf(WARNING_COLOR.getRGB())},
                {"Auto Door A - Main Entrance", getStatusWithColor("Auto Door A - Main Entrance"), String.valueOf(SUCCESS_COLOR.getRGB())}
        };
    }

    private String getStatusWithColor(String facilityName) {
        FacilityStatus status = facilityStatus.get(facilityName);
        return status != null ? status.status : "Unknown";
    }

    private Color parseColor(String colorStr) {
        try {
            return new Color(Integer.parseInt(colorStr));
        } catch (NumberFormatException e) {
            return SUCCESS_COLOR;
        }
    }

    private Color blendColors(Color color1, Color color2, float ratio) {
        if (ratio < 0) ratio = 0;
        if (ratio > 1) ratio = 1;
        int r = (int) (color1.getRed() * (1 - ratio) + color2.getRed() * ratio);
        int g = (int) (color1.getGreen() * (1 - ratio) + color2.getGreen() * ratio);
        int b = (int) (color1.getBlue() * (1 - ratio) + color2.getBlue() * ratio);
        return new Color(r, g, b);
    }

    private void startStatusUpdates() {
        statusUpdateTimer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateFacilityStatus();
                refreshUI();
            }
        });
        statusUpdateTimer.start();
    }

    private void startAnimations() {
        animationTimer = new Timer(16, new ActionListener() { // ~60 FPS
            @Override
            public void actionPerformed(ActionEvent e) {
                pulseValue += 0.05f;
                if (pulseValue > 2 * Math.PI) {
                    pulseValue = 0;
                }
                mainPanel.repaint();
            }
        });
        animationTimer.start();
    }

    private void updateFacilityStatus() {
        for (String facilityName : facilityStatus.keySet()) {
            FacilityStatus status = facilityStatus.get(facilityName);

            // 8% chance to change status for more realistic simulation
            if (random.nextInt(100) < 8) {
                int changeType = random.nextInt(4);
                switch (changeType) {
                    case 0: // Improve status
                        if (status.status.equals("Out of Service")) {
                            status.status = "Maintenance";
                            status.description = getRandomUpdateDescription();
                            status.color = WARNING_COLOR;
                            status.efficiency = 25 + random.nextInt(30);
                        } else if (status.status.equals("Maintenance")) {
                            status.status = "Operational";
                            status.description = getRandomUpdateDescription();
                            status.color = SUCCESS_COLOR;
                            status.efficiency = 80 + random.nextInt(20);
                        }
                        break;
                    case 1: // Worsen status
                        if (status.status.equals("Operational")) {
                            status.status = "Maintenance";
                            status.description = getRandomUpdateDescription();
                            status.color = WARNING_COLOR;
                            status.efficiency = 30 + random.nextInt(40);
                        } else if (status.status.equals("Maintenance")) {
                            status.status = "Out of Service";
                            status.description = getRandomUpdateDescription();
                            status.color = DANGER_COLOR;
                            status.efficiency = random.nextInt(10);
                        }
                        break;
                    case 2: // Update description
                        status.description = getRandomUpdateDescription();
                        break;
                    case 3: // Small efficiency change
                        int change = random.nextInt(21) - 10; // -10 to +10
                        status.efficiency = Math.max(0, Math.min(100, status.efficiency + change));
                        break;
                }
            }
        }
    }

    private String getRandomUpdateDescription() {
        String[][] updates = {
                {"Routine check completed", "Performance optimized", "Sensor calibration needed"},
                {"Scheduled inspection", "Cleaning in progress", "Software update applied"},
                {"Hardware replacement scheduled", "Normal operation resumed", "Emergency maintenance"},
                {"Parts replaced successfully", "System reboot completed", "Calibration in progress"},
                {"Safety inspection passed", "Performance test completed", "Manual override active"}
        };
        String[] category = updates[random.nextInt(updates.length)];
        return category[random.nextInt(category.length)];
    }

    private void refreshUI() {
        SwingUtilities.invokeLater(() -> {
            mainPanel.repaint();
        });
    }

    // Enhanced Facility status inner class
    private class FacilityStatus {
        String status;
        String description;
        Color color;
        int efficiency; // 0-100 percentage

        FacilityStatus(String status, String description, Color color, int efficiency) {
            this.status = status;
            this.description = description;
            this.color = color;
            this.efficiency = efficiency;
        }
    }

    // Modern scroll bar UI with animation
    private class ModernScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = PRIMARY_COLOR;
            this.trackColor = new Color(200, 200, 200, 50);
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
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Animated thumb
            float alpha = (float) (0.7 + 0.3 * Math.sin(pulseValue * 3));
            Color animatedThumb = new Color(thumbColor.getRed(), thumbColor.getGreen(), thumbColor.getBlue(), (int)(alpha * 255));
            g2.setColor(animatedThumb);
            g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 10, 10);

            g2.dispose();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(trackColor);
            g2.fillRoundRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height, 5, 5);

            g2.dispose();
        }
    }

    @Override
    public void dispose() {
        // Clean up timers
        if (statusUpdateTimer != null) statusUpdateTimer.stop();
        if (animationTimer != null) animationTimer.stop();
        super.dispose();
    }

    // Remove the main method since Dashboard will launch this
}