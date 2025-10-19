package ac.za.postapp.Pages;

// emeritusApex

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.ArrayList;

public class CampusMap extends JFrame {
    private JComboBox<String> currentLocationCombo;
    private JComboBox<String> destinationCombo;
    private JTextArea directionsArea;
    private JButton getDirectionsButton;
    private JButton startNavigationButton;
    private JButton pauseButton;
    private JButton resetButton;
    private JButton backToDashboardButton;
    private MapPanel mapPanel;
    private Timer animationTimer;
    private int animationStep = 0;
    private boolean isNavigating = false;
    private boolean isPaused = false;
    private String[] pathLocations;
    private HashMap<String, Point> locationCoordinates;
    private ArrayList<Point> pathPoints;
    private HashMap<String, Boolean> blockedPaths;

    private boolean avoidStairs = true;
    private boolean preferElevators = true;
    private boolean wheelchairAccessible = false;

    public CampusMap() {
        setTitle("Campus Map - Navigation");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 900);
        setLocationRelativeTo(null);
        setResizable(true);

        initializeLocationCoordinates();
        initializeBlockedPaths();

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(74, 107, 136),
                        getWidth(), getHeight(), new Color(33, 64, 98)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel topPanel = createControlPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);

        JSplitPane centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        centerSplit.setDividerLocation(800);
        centerSplit.setResizeWeight(0.7);

        mapPanel = new MapPanel();
        JScrollPane mapScroll = new JScrollPane(mapPanel);
        mapScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mapScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        TitledBorder mapBorder = BorderFactory.createTitledBorder("Campus Floor Plan - Level 2");
        mapBorder.setTitleColor(Color.WHITE);
        mapScroll.setBorder(mapBorder);

        centerSplit.setLeftComponent(mapScroll);

        JPanel directionsPanel = createDirectionsPanel();
        centerSplit.setRightComponent(directionsPanel);

        mainPanel.add(centerSplit, BorderLayout.CENTER);

        JPanel bottomNavPanel = createBottomNavigationPanel();
        mainPanel.add(bottomNavPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setupAnimationTimer();
        setupButtonListeners();
    }

    private void initializeLocationCoordinates() {
        locationCoordinates = new HashMap<>();

        locationCoordinates.put("Engineering Main Entrance", new Point(150, 650));
        locationCoordinates.put("Engineering Deans Office", new Point(200, 600));
        locationCoordinates.put("Engineering Lab 101", new Point(250, 550));
        locationCoordinates.put("Engineering Lab 102", new Point(300, 550));
        locationCoordinates.put("Engineering Lecture Hall A", new Point(200, 500));
        locationCoordinates.put("Engineering Workshop", new Point(250, 450));
        locationCoordinates.put("Robotics Lab", new Point(300, 450));
        locationCoordinates.put("Electrical Engineering Dept", new Point(180, 550));
        locationCoordinates.put("Mechanical Engineering Dept", new Point(220, 500));
        locationCoordinates.put("Engineering Library", new Point(280, 500));

        locationCoordinates.put("Health Sciences Entrance", new Point(500, 650));
        locationCoordinates.put("Medical Lab 201", new Point(550, 600));
        locationCoordinates.put("Anatomy Lab 202", new Point(600, 600));
        locationCoordinates.put("Nursing Simulation Center", new Point(550, 500));
        locationCoordinates.put("Health Sciences Library", new Point(600, 450));
        locationCoordinates.put("Pharmacy Lab", new Point(520, 550));
        locationCoordinates.put("Dentistry Clinic", new Point(580, 550));
        locationCoordinates.put("Physiotherapy Room", new Point(540, 500));
        locationCoordinates.put("Medical Library", new Point(590, 500));
        locationCoordinates.put("Health Admin Office", new Point(560, 450));

        locationCoordinates.put("Business School Entrance", new Point(800, 650));
        locationCoordinates.put("Finance Lab 301", new Point(850, 600));
        locationCoordinates.put("Marketing Center", new Point(900, 600));
        locationCoordinates.put("Business Lecture Hall B", new Point(850, 500));
        locationCoordinates.put("Entrepreneurship Hub", new Point(900, 450));
        locationCoordinates.put("Accounting Dept", new Point(820, 550));
        locationCoordinates.put("Management Office", new Point(870, 550));
        locationCoordinates.put("Business Library", new Point(830, 500));
        locationCoordinates.put("Case Study Room", new Point(880, 500));
        locationCoordinates.put("Executive Boardroom", new Point(860, 450));

        locationCoordinates.put("Main Library", new Point(400, 400));
        locationCoordinates.put("Library Study Area", new Point(350, 350));
        locationCoordinates.put("Library Computer Lab", new Point(450, 350));
        locationCoordinates.put("Quiet Study Zone", new Point(380, 380));
        locationCoordinates.put("Group Study Room A", new Point(420, 380));
        locationCoordinates.put("Group Study Room B", new Point(460, 380));
        locationCoordinates.put("Research Center", new Point(400, 350));
        locationCoordinates.put("Periodicals Section", new Point(440, 350));

        locationCoordinates.put("Computer Lab 401", new Point(300, 350));
        locationCoordinates.put("Physics Lab 402", new Point(250, 300));
        locationCoordinates.put("Chemistry Lab 403", new Point(350, 300));
        locationCoordinates.put("Research Lab 404", new Point(400, 300));
        locationCoordinates.put("Biology Lab", new Point(320, 320));
        locationCoordinates.put("Data Science Lab", new Point(370, 320));
        locationCoordinates.put("AI Research Center", new Point(340, 300));
        locationCoordinates.put("Innovation Hub", new Point(390, 320));

        locationCoordinates.put("Main Cafeteria", new Point(700, 400));
        locationCoordinates.put("Coffee Shop", new Point(650, 450));
        locationCoordinates.put("Vending Area", new Point(750, 450));
        locationCoordinates.put("Food Court", new Point(680, 420));
        locationCoordinates.put("Snack Bar", new Point(720, 420));
        locationCoordinates.put("Student Lounge", new Point(670, 380));
        locationCoordinates.put("Campus Store", new Point(730, 380));

        locationCoordinates.put("Admin Office", new Point(500, 550));
        locationCoordinates.put("Student Services", new Point(480, 500));
        locationCoordinates.put("Registrar Office", new Point(520, 500));
        locationCoordinates.put("Finance Office", new Point(490, 450));
        locationCoordinates.put("HR Department", new Point(530, 450));

        locationCoordinates.put("Elevator A", new Point(350, 600));
        locationCoordinates.put("Elevator B", new Point(700, 600));
        locationCoordinates.put("Elevator C", new Point(500, 400));
        locationCoordinates.put("Stairs A", new Point(400, 650));
        locationCoordinates.put("Stairs B", new Point(750, 650));
        locationCoordinates.put("Stairs C", new Point(450, 400));
        locationCoordinates.put("Emergency Exit A", new Point(100, 550));
        locationCoordinates.put("Emergency Exit B", new Point(950, 550));
        locationCoordinates.put("Emergency Exit C", new Point(500, 200));
        locationCoordinates.put("Restrooms A", new Point(450, 550));
        locationCoordinates.put("Restrooms B", new Point(800, 550));
        locationCoordinates.put("Restrooms C", new Point(600, 400));
        locationCoordinates.put("Information Desk", new Point(500, 700));
        locationCoordinates.put("Security Office", new Point(550, 700));
        locationCoordinates.put("First Aid Room", new Point(600, 700));

        locationCoordinates.put("Hallway Junction A", new Point(350, 500));
        locationCoordinates.put("Hallway Junction B", new Point(500, 500));
        locationCoordinates.put("Hallway Junction C", new Point(700, 500));
        locationCoordinates.put("Hallway Junction D", new Point(500, 400));
        locationCoordinates.put("Hallway Junction E", new Point(350, 400));
        locationCoordinates.put("Hallway Junction F", new Point(700, 400));
        locationCoordinates.put("Hallway Junction G", new Point(200, 600));
        locationCoordinates.put("Hallway Junction H", new Point(800, 600));

        locationCoordinates.put("Engineering Corridor", new Point(250, 600));
        locationCoordinates.put("Health Corridor", new Point(550, 600));
        locationCoordinates.put("Business Corridor", new Point(850, 600));
        locationCoordinates.put("Library Corridor", new Point(400, 350));
        locationCoordinates.put("Central Hallway", new Point(500, 600));
    }

    private void initializeBlockedPaths() {
        blockedPaths = new HashMap<>();
        blockedPaths.put("Engineering Lab 101-Engineering Workshop", true);
        blockedPaths.put("Health Sciences Entrance-Medical Lab 201", false);
        blockedPaths.put("Stairs A-Elevator A", false);
        blockedPaths.put("Emergency Exit A-Hallway Junction G", true);
        blockedPaths.put("Library Study Area-Quiet Study Zone", false);
    }

    private JPanel createControlPanel() {
        JPanel topPanel = new JPanel(new GridBagLayout());

        TitledBorder controlBorder = BorderFactory.createTitledBorder("Navigation Controls");
        controlBorder.setTitleColor(Color.WHITE);
        topPanel.setBorder(BorderFactory.createCompoundBorder(
                controlBorder,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        topPanel.setBackground(new Color(255, 255, 255, 180));
        topPanel.setOpaque(true);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel currentLabel = new JLabel("Current Location:");
        currentLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        currentLabel.setForeground(Color.WHITE);
        topPanel.add(currentLabel, gbc);

        gbc.gridx = 1;
        currentLocationCombo = new JComboBox<>(generateLocations());
        currentLocationCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        currentLocationCombo.setPreferredSize(new Dimension(250, 30));
        currentLocationCombo.setBackground(Color.WHITE);
        topPanel.add(currentLocationCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel destLabel = new JLabel("Destination:");
        destLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        destLabel.setForeground(Color.WHITE);
        topPanel.add(destLabel, gbc);

        gbc.gridx = 1;
        destinationCombo = new JComboBox<>(generateLocations());
        destinationCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        destinationCombo.setPreferredSize(new Dimension(250, 30));
        destinationCombo.setBackground(Color.WHITE);
        topPanel.add(destinationCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        getDirectionsButton = createStyledButton("Get Directions", new Color(52, 152, 219));
        startNavigationButton = createStyledButton("Start Navigation", new Color(46, 204, 113));
        pauseButton = createStyledButton("Pause", new Color(241, 196, 15));
        resetButton = createStyledButton("Reset", new Color(155, 89, 182));

        startNavigationButton.setEnabled(false);
        pauseButton.setEnabled(false);

        buttonPanel.add(getDirectionsButton);
        buttonPanel.add(startNavigationButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(resetButton);

        topPanel.add(buttonPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 3;
        addMobilityOptions(topPanel);

        return topPanel;
    }

    private void addMobilityOptions(JPanel topPanel) {
        JPanel mobilityPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        mobilityPanel.setOpaque(false);

        TitledBorder mobilityBorder = BorderFactory.createTitledBorder("Mobility Options");
        mobilityBorder.setTitleColor(Color.WHITE);
        mobilityPanel.setBorder(mobilityBorder);

        JCheckBox stairsCheckbox = new JCheckBox("Avoid Stairs", true);
        stairsCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        stairsCheckbox.setForeground(Color.WHITE);
        stairsCheckbox.addActionListener(e -> avoidStairs = stairsCheckbox.isSelected());

        JCheckBox elevatorCheckbox = new JCheckBox("Prefer Elevators", true);
        elevatorCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        elevatorCheckbox.setForeground(Color.WHITE);
        elevatorCheckbox.addActionListener(e -> preferElevators = elevatorCheckbox.isSelected());

        JCheckBox wheelchairCheckbox = new JCheckBox("Wheelchair Accessible");
        wheelchairCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        wheelchairCheckbox.setForeground(Color.WHITE);
        wheelchairCheckbox.addActionListener(e -> {
            wheelchairAccessible = wheelchairCheckbox.isSelected();
            if (wheelchairAccessible) {
                avoidStairs = true;
                preferElevators = true;
                stairsCheckbox.setSelected(true);
                elevatorCheckbox.setSelected(true);
            }
        });

        mobilityPanel.add(stairsCheckbox);
        mobilityPanel.add(elevatorCheckbox);
        mobilityPanel.add(wheelchairCheckbox);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3;
        gbc.insets = new Insets(10, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        topPanel.add(mobilityPanel, gbc);
    }

    private JPanel createDirectionsPanel() {
        JPanel directionsPanel = new JPanel(new BorderLayout());

        TitledBorder directionsBorder = BorderFactory.createTitledBorder("Step-by-Step Directions");
        directionsBorder.setTitleColor(Color.WHITE);
        directionsPanel.setBorder(BorderFactory.createCompoundBorder(
                directionsBorder,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        directionsPanel.setBackground(new Color(255, 255, 255, 180));

        directionsArea = new JTextArea();
        directionsArea.setEditable(false);
        directionsArea.setLineWrap(true);
        directionsArea.setWrapStyleWord(true);
        directionsArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        directionsArea.setBackground(new Color(240, 249, 255));
        directionsArea.setForeground(new Color(33, 64, 98));
        directionsArea.setText("CAMPUS NAVIGATION SYSTEM\n\n" +
                "Select your current location and destination to get detailed navigation instructions.\n\n" +
                "CAMPUS FACILITIES:\n" +
                "• Engineering Department (Left Wing)\n" +
                "• Health Sciences (Center Wing)\n" +
                "• Business School (Right Wing)\n" +
                "• Library & Study Areas\n" +
                "• Labs & Research Centers\n" +
                "• Food Court & Services\n" +
                "• Elevators & Staircases\n\n" +
                "MOBILITY FEATURES:\n" +
                "• Stair avoidance routes\n" +
                "• Elevator preferred paths\n" +
                "• Wheelchair accessible navigation");

        JScrollPane scrollPane = new JScrollPane(directionsArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(300, 400));
        scrollPane.getViewport().setBackground(new Color(240, 249, 255));
        directionsPanel.add(scrollPane, BorderLayout.CENTER);

        return directionsPanel;
    }

    private JPanel createBottomNavigationPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(255, 255, 255, 180));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel statusLabel = new JLabel("System: Online • Accessibility: Active • GPS: Connected");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setOpaque(false);

        backToDashboardButton = createNavigationButton("Back to Dashboard", new Color(52, 152, 219));

        buttonPanel.add(backToDashboardButton);

        bottomPanel.add(statusLabel, BorderLayout.WEST);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        return bottomPanel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(color.brighter());
                } else {
                    g2.setColor(color);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.dispose();

                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color.darker().darker());
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 25, 25);
                g2.dispose();
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setOpaque(false);

        return button;
    }

    private JButton createNavigationButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });

        return button;
    }

    private void setupAnimationTimer() {
        animationTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isNavigating && !isPaused && animationStep < pathPoints.size() - 1) {
                    animationStep++;
                    mapPanel.setCurrentPosition(pathPoints.get(animationStep));
                    mapPanel.repaint();
                    updateNavigationProgress();
                } else if (animationStep >= pathPoints.size() - 1) {
                    animationTimer.stop();
                    isNavigating = false;
                    showCelebration();

                    Timer resetTimer = new Timer(5000, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            resetNavigation();
                            directionsArea.setText("Navigation completed successfully!\n\nReady for your next destination.");
                        }
                    });
                    resetTimer.setRepeats(false);
                    resetTimer.start();
                }
            }
        });
    }

    private void showCelebration() {
        directionsArea.setText("CONGRATULATIONS!\n\n" +
                "You have successfully reached your destination!\n\n" +
                "MISSION ACCOMPLISHED!\n" +
                "Destination reached safely\n" +
                "Journey completed successfully\n\n" +
                "Thank you for using Campus Navigation System!\n\n" +
                "Auto-resetting in 5 seconds...");

        mapPanel.startCelebration();
    }

    private void setupButtonListeners() {
        getDirectionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String from = (String) currentLocationCombo.getSelectedItem();
                String to = (String) destinationCombo.getSelectedItem();
                showDirections(from, to);
            }
        });

        startNavigationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isNavigating) {
                    isNavigating = true;
                    isPaused = false;
                    animationStep = 0;
                    startNavigationButton.setText("Stop Navigation");
                    pauseButton.setText("Pause");
                    pauseButton.setEnabled(true);
                    mapPanel.setShowBlockedPaths(true);
                    mapPanel.repaint();
                    animationTimer.start();
                } else {
                    isNavigating = false;
                    animationTimer.stop();
                    startNavigationButton.setText("Start Navigation");
                    pauseButton.setEnabled(false);
                }
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isPaused = !isPaused;
                if (isPaused) {
                    pauseButton.setText("Resume");
                } else {
                    pauseButton.setText("Pause");
                }
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetNavigation();
            }
        });

        backToDashboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void resetNavigation() {
        isNavigating = false;
        isPaused = false;
        animationStep = 0;
        animationTimer.stop();

        startNavigationButton.setText("Start Navigation");
        startNavigationButton.setEnabled(false);
        pauseButton.setEnabled(false);
        pauseButton.setText("Pause");

        mapPanel.setCurrentPosition(null);
        mapPanel.setDestination(null);
        mapPanel.setPathLocations(null);
        mapPanel.setShowBlockedPaths(false);
        mapPanel.stopCelebration();
        mapPanel.repaint();

        directionsArea.setText("CAMPUS NAVIGATION SYSTEM\n\n" +
                "Select your current location and destination to get detailed navigation instructions.\n\n" +
                "CAMPUS FACILITIES:\n" +
                "• Engineering Department (Left Wing)\n" +
                "• Health Sciences (Center Wing)\n" +
                "• Business School (Right Wing)\n" +
                "• Library & Study Areas\n" +
                "• Labs & Research Centers\n" +
                "• Food Court & Services\n" +
                "• Elevators & Staircases\n\n" +
                "MOBILITY FEATURES:\n" +
                "• Stair avoidance routes\n" +
                "• Elevator preferred paths\n" +
                "• Wheelchair accessible navigation");
    }

    private String[] generateLocations() {
        return locationCoordinates.keySet().toArray(new String[0]);
    }

    private void showDirections(String from, String to) {
        resetNavigation();

        if (from.equals(to)) {
            directionsArea.setText("You are already at your destination!\n\n" +
                    "Current Location: " + to + "\n\n" +
                    "No navigation needed. You're already here!");
            return;
        }

        Point fromPoint = locationCoordinates.get(from);
        Point toPoint = locationCoordinates.get(to);

        if (fromPoint == null || toPoint == null) {
            directionsArea.setText("NAVIGATION ERROR\n\n" +
                    "Location coordinates not found.\n" +
                    "Please select valid locations from the dropdown menus.");
            return;
        }

        pathLocations = generateDirectPath(from, to);
        pathPoints = generateGridPathPoints(pathLocations);

        StringBuilder directions = new StringBuilder();
        directions.append("NAVIGATION INSTRUCTIONS\n\n");
        directions.append("FROM: ").append(from).append("\n");
        directions.append("TO: ").append(to).append("\n\n");

        if (wheelchairAccessible) {
            directions.append("WHEELCHAIR ACCESSIBLE ROUTE ACTIVATED\n");
            directions.append("• Stair-free path ensured\n");
            directions.append("• Elevator access prioritized\n");
            directions.append("• Accessible routes only\n\n");
        }
        if (avoidStairs) {
            directions.append("STAIR AVOIDANCE ENABLED\n");
            directions.append("• Using elevators instead of stairs\n");
            directions.append("• Alternative routes activated\n\n");
        }

        directions.append("STEP-BY-STEP DIRECTIONS:\n\n");

        for (int i = 1; i < pathLocations.length; i++) {
            String stepDescription = getDetailedDirectionText(pathLocations[i-1], pathLocations[i]);
            directions.append("STEP ").append(i).append(":\n");
            directions.append("   ").append(stepDescription).append("\n\n");
        }

        directions.append("FINAL DESTINATION REACHED\n");
        directions.append("• You have arrived at: ").append(to).append("\n");
        directions.append("• Total distance: ").append(pathPoints.size()).append(" meters\n");
        directions.append("• Estimated time: ").append(calculateWalkingTime(pathPoints.size())).append(" minutes\n\n");
        directions.append("Click 'Start Navigation' to begin your guided journey!");

        directionsArea.setText(directions.toString());
        startNavigationButton.setEnabled(true);
        animationStep = 0;
        mapPanel.setCurrentPosition(fromPoint);
        mapPanel.setDestination(toPoint);
        mapPanel.setPathLocations(pathLocations);
        mapPanel.repaint();
    }

    private String[] generateDirectPath(String from, String to) {
        ArrayList<String> path = new ArrayList<>();
        path.add(from);

        Point fromPoint = locationCoordinates.get(from);
        Point toPoint = locationCoordinates.get(to);

        boolean sameBuilding = isSameBuilding(fromPoint, toPoint);

        if (sameBuilding) {
            if (Math.abs(fromPoint.y - toPoint.y) > 100) {
                if (fromPoint.y < toPoint.y) {
                    path.add("Central Hallway");
                } else {
                    path.add(getNearestJunction(fromPoint));
                }
            }
        } else {
            path.add(getNearestCorridor(fromPoint));
            path.add("Central Hallway");
            path.add(getNearestCorridor(toPoint));
        }

        path.add(to);
        return path.toArray(new String[0]);
    }

    private boolean isSameBuilding(Point from, Point to) {
        boolean fromEngineering = from.x < 400;
        boolean fromHealth = from.x >= 400 && from.x <= 700;
        boolean fromBusiness = from.x > 700;

        boolean toEngineering = to.x < 400;
        boolean toHealth = to.x >= 400 && to.x <= 700;
        boolean toBusiness = to.x > 700;

        return (fromEngineering && toEngineering) ||
                (fromHealth && toHealth) ||
                (fromBusiness && toBusiness);
    }

    private String getNearestCorridor(Point point) {
        if (point.x < 400) return "Engineering Corridor";
        if (point.x > 700) return "Business Corridor";
        return "Health Corridor";
    }

    private String getNearestJunction(Point point) {
        if (point.y < 400) return "Hallway Junction D";
        if (point.x < 400) return "Hallway Junction A";
        if (point.x > 700) return "Hallway Junction C";
        return "Hallway Junction B";
    }

    private ArrayList<Point> generateGridPathPoints(String[] locations) {
        ArrayList<Point> points = new ArrayList<>();
        for (String location : locations) {
            points.add(locationCoordinates.get(location));
        }

        ArrayList<Point> gridPath = new ArrayList<>();

        for (int i = 0; i < points.size() - 1; i++) {
            Point start = points.get(i);
            Point end = points.get(i + 1);

            gridPath.add(start);

            if (start.x != end.x) {
                int xStep = (end.x > start.x) ? 5 : -5;
                for (int x = start.x + xStep; x != end.x; x += xStep) {
                    gridPath.add(new Point(x, start.y));
                }
            }

            if (start.y != end.y) {
                int yStep = (end.y > start.y) ? 5 : -5;
                for (int y = start.y + yStep; y != end.y; y += yStep) {
                    gridPath.add(new Point(end.x, y));
                }
            }
        }

        gridPath.add(points.get(points.size() - 1));
        return gridPath;
    }

    private String getDetailedDirectionText(String from, String to) {
        Point fromPt = locationCoordinates.get(from);
        Point toPt = locationCoordinates.get(to);

        if (fromPt == null || toPt == null) return "Proceed directly to " + to;

        StringBuilder direction = new StringBuilder();

        if (toPt.y < fromPt.y - 20) {
            direction.append("HEAD NORTH: Continue straight ahead");
        } else if (toPt.y > fromPt.y + 20) {
            direction.append("HEAD SOUTH: Continue straight ahead");
        } else if (toPt.x > fromPt.x + 20) {
            direction.append("HEAD EAST: Turn right and continue");
        } else if (toPt.x < fromPt.x - 20) {
            direction.append("HEAD WEST: Turn left and continue");
        } else {
            direction.append("PROCEED: Continue forward to ");
        }

        if (to.contains("Elevator")) {
            direction.append("\n   ELEVATOR ACCESS: Use the elevator to change floors");
            if (wheelchairAccessible) {
                direction.append(" (Wheelchair Accessible)");
            }
        } else if (to.contains("Stairs") && avoidStairs) {
            direction.append("\n   STAIR AVOIDANCE: Alternative route activated - using elevator instead");
        } else if (to.contains("Stairs")) {
            direction.append("\n   STAIRCASE: Use the stairs to access different levels");
            if (wheelchairAccessible) {
                direction.append(" (Not wheelchair accessible)");
            }
        } else if (to.contains("Emergency")) {
            direction.append("\n   EMERGENCY EXIT: Emergency exit location - for emergency use only");
        } else if (to.contains("Restrooms")) {
            direction.append("\n   RESTROOMS: Restroom facilities available");
            if (wheelchairAccessible && to.contains("A")) {
                direction.append(" (Accessible facilities)");
            }
        } else if (to.contains("Library")) {
            direction.append("\n   LIBRARY AREA: Entering library and study zone");
        } else if (to.contains("Lab")) {
            direction.append("\n   LABORATORY: Science and research laboratory area");
        } else if (to.contains("Food") || to.contains("Cafeteria") || to.contains("Coffee")) {
            direction.append("\n   DINING AREA: Food court and refreshment zone");
        } else if (to.contains("Corridor") || to.contains("Hallway")) {
            direction.append("\n   MAIN PASSAGEWAY: Continue through the corridor");
        }

        double distance = fromPt.distance(toPt);
        direction.append(String.format("\n   Distance: Approximately %.0f meters", distance * 0.8));

        if (!direction.toString().contains("to " + to)) {
            direction.append("\n   Destination: ").append(to);
        }

        return direction.toString();
    }

    private String calculateWalkingTime(int steps) {
        int minutes = steps / 20;
        return String.valueOf(Math.max(1, minutes));
    }

    private void updateNavigationProgress() {
        if (animationStep < pathPoints.size() && pathLocations != null) {
            int progress = (animationStep * 100) / pathPoints.size();

            StringBuilder navText = new StringBuilder();
            navText.append("LIVE NAVIGATION IN PROGRESS\n\n");
            navText.append("PROGRESS: ").append(progress).append("% Complete\n\n");

            String currentLocation = getCurrentLocationName();
            String nextLocation = getNextLocationName();

            navText.append("CURRENT POSITION: ").append(currentLocation).append("\n");
            navText.append("NEXT WAYPOINT: ").append(nextLocation).append("\n\n");

            navText.append("JOURNEY STATS:\n");
            navText.append("   • Steps completed: ").append(animationStep).append("/").append(pathPoints.size()).append("\n");
            navText.append("   • Estimated time remaining: ").append((pathPoints.size() - animationStep) / 20).append(" minutes\n");
            navText.append("   • Distance remaining: ").append(pathPoints.size() - animationStep).append(" meters\n\n");

            if (nextLocation.contains("Elevator")) {
                navText.append("ELEVATOR APPROACHING: Prepare to enter elevator\n");
            } else if (nextLocation.contains("Stairs")) {
                navText.append("STAIRS AHEAD: Use handrails for safety\n");
            } else if (nextLocation.contains("Door")) {
                navText.append("DOOR AHEAD: Watch your step\n");
            } else if (nextLocation.contains("Corridor") || nextLocation.contains("Hallway")) {
                navText.append("CORRIDOR AHEAD: Continue straight\n");
            }

            directionsArea.setText(navText.toString());
        }
    }

    private String getCurrentLocationName() {
        if (pathLocations != null && animationStep < pathPoints.size()) {
            Point currentPos = pathPoints.get(animationStep);
            String closest = pathLocations[0];
            double minDist = Double.MAX_VALUE;

            for (String location : pathLocations) {
                Point locPoint = locationCoordinates.get(location);
                if (locPoint != null) {
                    double dist = currentPos.distance(locPoint);
                    if (dist < minDist) {
                        minDist = dist;
                        closest = location;
                    }
                }
            }
            return closest;
        }
        return "Navigating...";
    }

    private String getNextLocationName() {
        if (pathLocations != null && animationStep < pathLocations.length - 1) {
            return pathLocations[Math.min(animationStep + 1, pathLocations.length - 1)];
        }
        return "Final Destination";
    }

    class MapPanel extends JPanel {
        private Point currentPosition;
        private Point destination;
        private String[] pathLocations;
        private boolean showBlockedPaths = false;
        private boolean celebration = false;
        private long celebrationStartTime = 0;
        private final int PERSON_RADIUS = 8;
        private final int GRID_SIZE = 50;

        public MapPanel() {
            setPreferredSize(new Dimension(1500, 1000));
        }

        public void setCurrentPosition(Point position) {
            this.currentPosition = position;
        }

        public void setDestination(Point destination) {
            this.destination = destination;
        }

        public void setPathLocations(String[] pathLocations) {
            this.pathLocations = pathLocations;
        }

        public void setShowBlockedPaths(boolean show) {
            this.showBlockedPaths = show;
        }

        public void startCelebration() {
            this.celebration = true;
            this.celebrationStartTime = System.currentTimeMillis();
            repaint();
        }

        public void stopCelebration() {
            this.celebration = false;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            GradientPaint backgroundGradient = new GradientPaint(
                    0, 0, new Color(74, 107, 136),
                    getWidth(), getHeight(), new Color(33, 64, 98)
            );
            g2d.setPaint(backgroundGradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            if (celebration) {
                drawCelebrationEffect(g2d);
            }

            drawGrid(g2d);
            drawBuildingLayout(g2d);
            drawExpandedPassages(g2d);
            drawDoors(g2d);
            drawElevators(g2d);
            drawStairs(g2d);
            if (showBlockedPaths) {
                drawBlockedPaths(g2d);
            }
            drawPath(g2d);
            drawLocationLabels(g2d);
            drawPerson(g2d);
            drawLegend(g2d);
        }

        private void drawCelebrationEffect(Graphics2D g2d) {
            long currentTime = System.currentTimeMillis();
            long elapsed = currentTime - celebrationStartTime;
            float alpha = (float) (0.5 + 0.5 * Math.sin(elapsed * 0.01));

            g2d.setColor(new Color(255, 255, 0, (int)(100 * alpha)));
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.setColor(new Color(255, 215, 0));
            g2d.setFont(new Font("Arial", Font.BOLD, 48));
            String congrats = "CONGRATULATIONS!";
            FontMetrics fm = g2d.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(congrats)) / 2;
            int y = 100;
            g2d.drawString(congrats, x, y);
        }

        private void drawGrid(Graphics2D g2d) {
            g2d.setColor(new Color(200, 230, 255));
            g2d.setStroke(new BasicStroke(1));

            for (int x = 0; x < getWidth(); x += GRID_SIZE) {
                g2d.drawLine(x, 0, x, getHeight());
            }
            for (int y = 0; y < getHeight(); y += GRID_SIZE) {
                g2d.drawLine(0, y, getWidth(), y);
            }
        }

        private void drawBuildingLayout(Graphics2D g2d) {
            g2d.setColor(new Color(180, 200, 220));
            g2d.setStroke(new BasicStroke(15));

            g2d.drawLine(100, 600, 900, 600);
            g2d.drawLine(200, 300, 800, 300);

            g2d.drawLine(350, 300, 350, 600);
            g2d.drawLine(500, 300, 500, 600);
            g2d.drawLine(700, 300, 700, 600);

            g2d.setColor(new Color(173, 216, 230, 180));
            g2d.fillRect(100, 450, 250, 200);
            g2d.setColor(new Color(30, 136, 229));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(100, 450, 250, 200);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString("ENGINEERING", 140, 540);

            g2d.setColor(new Color(144, 238, 144, 180));
            g2d.fillRect(350, 450, 350, 200);
            g2d.setColor(new Color(56, 142, 60));
            g2d.drawRect(350, 450, 350, 200);
            g2d.drawString("HEALTH SCIENCES", 420, 540);

            g2d.setColor(new Color(255, 255, 153, 180));
            g2d.fillRect(700, 450, 250, 200);
            g2d.setColor(new Color(245, 124, 0));
            g2d.drawRect(700, 450, 250, 200);
            g2d.drawString("BUSINESS SCHOOL", 740, 540);

            g2d.setColor(new Color(255, 182, 193, 180));
            g2d.fillRect(200, 100, 600, 200);
            g2d.setColor(new Color(194, 24, 91));
            g2d.drawRect(200, 100, 600, 200);
            g2d.drawString("LIBRARY & LABS", 380, 180);
        }

        private void drawExpandedPassages(Graphics2D g2d) {
            g2d.setColor(new Color(150, 180, 210));
            g2d.setStroke(new BasicStroke(8));

            g2d.drawLine(150, 450, 150, 600);
            g2d.drawLine(250, 450, 250, 600);
            g2d.drawLine(350, 450, 350, 600);

            g2d.drawLine(450, 450, 450, 600);
            g2d.drawLine(550, 450, 550, 600);
            g2d.drawLine(650, 450, 650, 600);

            g2d.drawLine(750, 450, 750, 600);
            g2d.drawLine(850, 450, 850, 600);

            g2d.drawLine(100, 500, 350, 500);
            g2d.drawLine(350, 500, 700, 500);
            g2d.drawLine(700, 500, 950, 500);

            g2d.drawLine(200, 150, 800, 150);
            g2d.drawLine(400, 100, 400, 200);
        }

        private void drawBlockedPaths(Graphics2D g2d) {
            g2d.setColor(new Color(255, 0, 0, 100));
            g2d.setStroke(new BasicStroke(6));

            g2d.drawLine(250, 550, 250, 450);
            g2d.drawLine(100, 550, 150, 550);

            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            g2d.drawString("BLOCKED", 240, 500);
            g2d.drawString("BLOCKED", 110, 530);
        }

        private void drawDoors(Graphics2D g2d) {
            g2d.setColor(new Color(139, 69, 19));
            g2d.setStroke(new BasicStroke(4));

            int[][] doors = {
                    {150, 650}, {500, 650}, {800, 650},
                    {350, 600}, {500, 600}, {700, 600},
                    {200, 300}, {400, 300}, {600, 300}, {800, 300},
                    {250, 500}, {550, 500}, {850, 500}
            };

            for (int[] door : doors) {
                g2d.fillRect(door[0]-10, door[1]-4, 20, 8);
                g2d.setColor(Color.YELLOW);
                g2d.fillOval(door[0]+3, door[1]-3, 6, 6);
                g2d.setColor(new Color(139, 69, 19));
            }
        }

        private void drawElevators(Graphics2D g2d) {
            g2d.setColor(new Color(30, 136, 229));
            g2d.fillRect(340, 580, 25, 45);
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(340, 580, 25, 45);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString("ELEV", 342, 600);
            g2d.drawString("A", 350, 615);

            g2d.setColor(new Color(30, 136, 229));
            g2d.fillRect(690, 580, 25, 45);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(690, 580, 25, 45);
            g2d.drawString("ELEV", 692, 600);
            g2d.drawString("B", 700, 615);

            g2d.setColor(new Color(30, 136, 229));
            g2d.fillRect(490, 380, 25, 45);
            g2d.setColor(Color.BLACK);
            g2d.drawRect(490, 380, 25, 45);
            g2d.drawString("ELEV", 492, 400);
            g2d.drawString("C", 500, 415);
        }

        private void drawStairs(Graphics2D g2d) {
            g2d.setColor(new Color(120, 120, 120));

            g2d.fillRect(395, 580, 12, 45);
            for (int i = 0; i < 6; i++) {
                g2d.fillRect(395 + i*2, 580 + i*7, 12, 4);
            }

            g2d.fillRect(745, 580, 12, 45);
            for (int i = 0; i < 6; i++) {
                g2d.fillRect(745 + i*2, 580 + i*7, 12, 4);
            }

            g2d.fillRect(445, 380, 12, 45);
            for (int i = 0; i < 6; i++) {
                g2d.fillRect(445 + i*2, 380 + i*7, 12, 4);
            }

            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 9));
            g2d.drawString("STAIRS", 390, 575);
            g2d.drawString("STAIRS", 740, 575);
            g2d.drawString("STAIRS", 440, 375);
        }

        private void drawPath(Graphics2D g2d) {
            if (pathLocations == null || pathLocations.length < 2) return;

            g2d.setColor(new Color(30, 136, 229, 200));
            g2d.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            for (int i = 0; i < pathLocations.length - 1; i++) {
                Point start = locationCoordinates.get(pathLocations[i]);
                Point end = locationCoordinates.get(pathLocations[i + 1]);
                if (start != null && end != null) {
                    g2d.drawLine(start.x, start.y, end.x, end.y);
                }
            }

            g2d.setColor(new Color(21, 101, 192));
            for (String location : pathLocations) {
                Point point = locationCoordinates.get(location);
                if (point != null) {
                    g2d.fill(new Ellipse2D.Double(point.x - 4, point.y - 4, 8, 8));
                }
            }
        }

        private void drawLocationLabels(Graphics2D g2d) {
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 8));
            g2d.setColor(Color.WHITE);

            for (java.util.Map.Entry<String, Point> entry : locationCoordinates.entrySet()) {
                Point point = entry.getValue();
                String name = entry.getKey();

                g2d.setColor(new Color(30, 136, 229));
                g2d.fill(new Ellipse2D.Double(point.x - 3, point.y - 3, 6, 6));

                g2d.setColor(Color.WHITE);
                String displayName = name;
                if (name.length() > 20) {
                    displayName = name.substring(0, Math.min(18, name.length())) + "...";
                }

                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(displayName);
                g2d.setColor(new Color(0, 0, 0, 180));
                g2d.fillRect(point.x - textWidth/2 - 2, point.y - 15, textWidth + 4, 10);

                g2d.setColor(Color.WHITE);
                g2d.drawString(displayName, point.x - textWidth/2, point.y - 6);
            }
        }

        private void drawPerson(Graphics2D g2d) {
            if (currentPosition == null) return;

            g2d.setColor(new Color(229, 57, 53));

            g2d.fill(new Ellipse2D.Double(
                    currentPosition.x - PERSON_RADIUS,
                    currentPosition.y - PERSON_RADIUS,
                    PERSON_RADIUS * 2,
                    PERSON_RADIUS * 2
            ));

            if (isNavigating && animationStep < pathPoints.size() - 1) {
                Point nextPoint = pathPoints.get(animationStep + 1);

                if (nextPoint.x > currentPosition.x) {
                    g2d.setColor(Color.BLACK);
                    g2d.fillPolygon(new int[]{currentPosition.x + PERSON_RADIUS, currentPosition.x + PERSON_RADIUS + 12, currentPosition.x + PERSON_RADIUS},
                            new int[]{currentPosition.y - 6, currentPosition.y, currentPosition.y + 6}, 3);
                } else if (nextPoint.x < currentPosition.x) {
                    g2d.setColor(Color.BLACK);
                    g2d.fillPolygon(new int[]{currentPosition.x - PERSON_RADIUS, currentPosition.x - PERSON_RADIUS - 12, currentPosition.x - PERSON_RADIUS},
                            new int[]{currentPosition.y - 6, currentPosition.y, currentPosition.y + 6}, 3);
                } else if (nextPoint.y > currentPosition.y) {
                    g2d.setColor(Color.BLACK);
                    g2d.fillPolygon(new int[]{currentPosition.x - 6, currentPosition.x, currentPosition.x + 6},
                            new int[]{currentPosition.y + PERSON_RADIUS, currentPosition.y + PERSON_RADIUS + 12, currentPosition.y + PERSON_RADIUS}, 3);
                } else if (nextPoint.y < currentPosition.y) {
                    g2d.setColor(Color.BLACK);
                    g2d.fillPolygon(new int[]{currentPosition.x - 6, currentPosition.x, currentPosition.x + 6},
                            new int[]{currentPosition.y - PERSON_RADIUS, currentPosition.y - PERSON_RADIUS - 12, currentPosition.y - PERSON_RADIUS}, 3);
                }
            }

            if (isNavigating && !isPaused) {
                int glowSize = PERSON_RADIUS + 5 + (int)(3 * Math.sin(System.currentTimeMillis() * 0.01));
                g2d.setColor(new Color(229, 57, 53, 80));
                g2d.fill(new Ellipse2D.Double(
                        currentPosition.x - glowSize,
                        currentPosition.y - glowSize,
                        glowSize * 2,
                        glowSize * 2
                ));
            }

            if (destination != null) {
                g2d.setColor(new Color(0, 150, 136));
                g2d.setStroke(new BasicStroke(2));
                g2d.draw(new Ellipse2D.Double(
                        destination.x - 10,
                        destination.y - 10,
                        20,
                        20
                ));
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                g2d.drawString("DEST", destination.x - 12, destination.y + 5);
            }
        }

        private void drawLegend(Graphics2D g2d) {
            g2d.setFont(new Font("Arial", Font.PLAIN, 10));
            g2d.setColor(Color.WHITE);

            int legendX = 20;
            int legendY = 30;
            int lineHeight = 15;

            g2d.setColor(new Color(0, 0, 0, 180));
            g2d.fillRect(legendX - 5, legendY - 15, 160, 140);
            g2d.setColor(new Color(30, 136, 229));
            g2d.drawRect(legendX - 5, legendY - 15, 160, 140);
            g2d.drawString("MAP LEGEND", legendX, legendY);

            legendY += lineHeight;
            g2d.setColor(new Color(30, 136, 229));
            g2d.fillRect(legendX, legendY, 10, 10);
            g2d.setColor(Color.WHITE);
            g2d.drawString("Navigation Path", legendX + 15, legendY + 8);

            legendY += lineHeight;
            g2d.setColor(new Color(229, 57, 53));
            g2d.fillOval(legendX, legendY, 10, 10);
            g2d.setColor(Color.WHITE);
            g2d.drawString("Your Position", legendX + 15, legendY + 8);

            legendY += lineHeight;
            g2d.setColor(new Color(30, 136, 229));
            g2d.fillRect(legendX, legendY, 10, 10);
            g2d.setColor(Color.WHITE);
            g2d.drawString("Elevator", legendX + 15, legendY + 8);

            legendY += lineHeight;
            g2d.setColor(Color.GRAY);
            g2d.fillRect(legendX, legendY, 10, 10);
            g2d.setColor(Color.WHITE);
            g2d.drawString("Stairs", legendX + 15, legendY + 8);

            legendY += lineHeight;
            g2d.setColor(new Color(139, 69, 19));
            g2d.fillRect(legendX, legendY, 10, 3);
            g2d.setColor(Color.WHITE);
            g2d.drawString("Doors", legendX + 15, legendY + 8);

            legendY += lineHeight;
            g2d.setColor(Color.RED);
            g2d.fillRect(legendX, legendY, 10, 10);
            g2d.setColor(Color.WHITE);
            g2d.drawString("Blocked Paths", legendX + 15, legendY + 8);
        }
    }
}