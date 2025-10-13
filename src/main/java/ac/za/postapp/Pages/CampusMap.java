package ac.za.postapp.Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.ArrayList;

public class CampusMap extends JFrame {
    private JComboBox<String> currentLocationCombo;
    private JComboBox<String> destinationCombo;
    private JTextArea directionsArea;
    private JButton getDirectionsButton;
    private JButton startNavigationButton;
    private JButton pauseButton;
    private MapPanel mapPanel;
    private Timer animationTimer;
    private int animationStep = 0;
    private boolean isNavigating = false;
    private boolean isPaused = false;
    private String[] pathLocations;
    private HashMap<String, Point> locationCoordinates;
    private ArrayList<Point> pathPoints;

    public CampusMap() {
        setTitle("CPUT D6 Campus Map - Advanced Navigation");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(true);

        initializeLocationCoordinates();

        // ===== Main Container =====
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 245, 249));

        // ===== Top Control Panel =====
        JPanel topPanel = createControlPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // ===== Center - Map and Directions =====
        JSplitPane centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        centerSplit.setDividerLocation(650);
        centerSplit.setResizeWeight(0.7);

        // Map Panel
        mapPanel = new MapPanel();
        JScrollPane mapScroll = new JScrollPane(mapPanel);
        mapScroll.setBorder(BorderFactory.createTitledBorder("🏫 Campus Floor Plan - Level 2"));
        centerSplit.setLeftComponent(mapScroll);

        // Directions Panel
        JPanel directionsPanel = createDirectionsPanel();
        centerSplit.setRightComponent(directionsPanel);

        mainPanel.add(centerSplit, BorderLayout.CENTER);

        add(mainPanel);
        setupAnimationTimer();
    }

    private void initializeLocationCoordinates() {
        locationCoordinates = new HashMap<>();

        // === MAIN BUILDING STRUCTURE ===
        // Engineering Department
        locationCoordinates.put("Engineering Main Entrance", new Point(150, 450));
        locationCoordinates.put("Engineering Dean's Office", new Point(200, 400));
        locationCoordinates.put("Engineering Lab 101", new Point(250, 350));
        locationCoordinates.put("Engineering Lab 102", new Point(300, 350));
        locationCoordinates.put("Engineering Lecture Hall A", new Point(200, 300));

        // Health Sciences Department
        locationCoordinates.put("Health Sciences Entrance", new Point(500, 450));
        locationCoordinates.put("Medical Lab 201", new Point(550, 400));
        locationCoordinates.put("Anatomy Lab 202", new Point(600, 400));
        locationCoordinates.put("Nursing Simulation Center", new Point(550, 300));
        locationCoordinates.put("Health Sciences Library", new Point(600, 250));

        // Business Department
        locationCoordinates.put("Business School Entrance", new Point(800, 450));
        locationCoordinates.put("Finance Lab 301", new Point(850, 400));
        locationCoordinates.put("Marketing Center", new Point(900, 400));
        locationCoordinates.put("Business Lecture Hall B", new Point(850, 300));
        locationCoordinates.put("Entrepreneurship Hub", new Point(900, 250));

        // === COMMON FACILITIES ===
        locationCoordinates.put("Main Library", new Point(400, 200));
        locationCoordinates.put("Library Study Area", new Point(350, 150));
        locationCoordinates.put("Library Computer Lab", new Point(450, 150));

        // Food Areas
        locationCoordinates.put("Main Cafeteria", new Point(700, 200));
        locationCoordinates.put("Coffee Shop", new Point(650, 250));
        locationCoordinates.put("Vending Area", new Point(750, 250));

        // Labs
        locationCoordinates.put("Computer Lab 401", new Point(300, 150));
        locationCoordinates.put("Physics Lab 402", new Point(250, 100));
        locationCoordinates.put("Chemistry Lab 403", new Point(350, 100));
        locationCoordinates.put("Research Lab 404", new Point(400, 100));

        // Special Facilities
        locationCoordinates.put("Elevator A", new Point(350, 400));
        locationCoordinates.put("Elevator B", new Point(700, 400));
        locationCoordinates.put("Stairs A", new Point(400, 450));
        locationCoordinates.put("Stairs B", new Point(750, 450));
        locationCoordinates.put("Emergency Exit A", new Point(100, 350));
        locationCoordinates.put("Emergency Exit B", new Point(950, 350));
        locationCoordinates.put("Restrooms A", new Point(450, 350));
        locationCoordinates.put("Restrooms B", new Point(800, 350));
        locationCoordinates.put("Information Desk", new Point(500, 500));
    }

    private JPanel createControlPanel() {
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("🎯 Navigation Controls"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        topPanel.setBackground(new Color(240, 245, 249));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Current Location
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel currentLabel = new JLabel("📍 Current Location:");
        currentLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        topPanel.add(currentLabel, gbc);

        gbc.gridx = 1;
        currentLocationCombo = new JComboBox<>(generateLocations());
        currentLocationCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        currentLocationCombo.setPreferredSize(new Dimension(250, 30));
        topPanel.add(currentLocationCombo, gbc);

        // Destination
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel destLabel = new JLabel("🎯 Destination:");
        destLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        topPanel.add(destLabel, gbc);

        gbc.gridx = 1;
        destinationCombo = new JComboBox<>(generateLocations());
        destinationCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        destinationCombo.setPreferredSize(new Dimension(250, 30));
        topPanel.add(destinationCombo, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        getDirectionsButton = createStyledButton("🗺️ Get Directions", new Color(52, 152, 219));
        startNavigationButton = createStyledButton("🚶 Start Navigation", new Color(46, 204, 113));
        pauseButton = createStyledButton("⏸️ Pause", new Color(230, 126, 34));

        startNavigationButton.setEnabled(false);
        pauseButton.setEnabled(false);

        buttonPanel.add(getDirectionsButton);
        buttonPanel.add(startNavigationButton);
        buttonPanel.add(pauseButton);

        topPanel.add(buttonPanel, gbc);

        return topPanel;
    }

    private JPanel createDirectionsPanel() {
        JPanel directionsPanel = new JPanel(new BorderLayout());
        directionsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("📋 Step-by-Step Directions"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        directionsArea = new JTextArea();
        directionsArea.setEditable(false);
        directionsArea.setLineWrap(true);
        directionsArea.setWrapStyleWord(true);
        directionsArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        directionsArea.setBackground(new Color(253, 253, 253));
        directionsArea.setText("Select your current location and destination to get navigation instructions.\n\n" +
                "🏫 Campus Features:\n" +
                "• 🟦 Engineering Department (Left)\n" +
                "• 🟩 Health Sciences (Center)\n" +
                "• 🟨 Business School (Right)\n" +
                "• 📚 Library & Labs (Top)\n" +
                "• 🍽️ Food Areas (Top Right)\n" +
                "• ⬆️ Elevators & Stairs\n" +
                "• 🚪 Emergency Exits");

        JScrollPane scrollPane = new JScrollPane(directionsArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(300, 400));

        directionsPanel.add(scrollPane, BorderLayout.CENTER);

        return directionsPanel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;
    }

    private void setupAnimationTimer() {
        animationTimer = new Timer(200, new ActionListener() { // Slower timer for smooth movement
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
                    startNavigationButton.setText("🚶 Start Navigation");
                    startNavigationButton.setEnabled(true);
                    pauseButton.setEnabled(false);
                    directionsArea.append("\n\n🎉 Navigation completed! You have reached your destination.");
                }
            }
        });
    }

    private String[] generateLocations() {
        return locationCoordinates.keySet().toArray(new String[0]);
    }

    private void showDirections(String from, String to) {
        if (from.equals(to)) {
            directionsArea.setText("🎉 You are already at your destination: " + to);
            startNavigationButton.setEnabled(false);
            return;
        }

        Point fromPoint = locationCoordinates.get(from);
        Point toPoint = locationCoordinates.get(to);

        if (fromPoint == null || toPoint == null) {
            directionsArea.setText("❌ Error: Location coordinates not found.");
            return;
        }

        // Generate detailed path
        pathLocations = generatePath(from, to);
        pathPoints = generatePathPoints(pathLocations);

        StringBuilder directions = new StringBuilder();
        directions.append("🗺️ Navigation from ").append(from).append(" to ").append(to).append("\n\n");

        directions.append("📍 Starting at: ").append(from).append("\n\n");

        // Add step-by-step directions
        for (int i = 1; i < pathLocations.length; i++) {
            directions.append("Step ").append(i).append(": ").append(getDirectionText(pathLocations[i-1], pathLocations[i])).append("\n\n");
        }

        directions.append("🎯 Arrive at: ").append(to).append("\n\n");
        directions.append("📏 Estimated walking time: ").append(calculateWalkingTime(pathLocations.length)).append(" minutes\n");
        directions.append("🚶 Total steps: ").append(pathLocations.length - 1).append("\n\n");
        directions.append("Click 'Start Navigation' to begin guided walkthrough.");

        directionsArea.setText(directions.toString());
        startNavigationButton.setEnabled(true);
        animationStep = 0;
        mapPanel.setCurrentPosition(fromPoint);
        mapPanel.setDestination(toPoint);
        mapPanel.setPathLocations(pathLocations);
        mapPanel.repaint();
    }

    private String[] generatePath(String from, String to) {
        ArrayList<String> path = new ArrayList<>();
        path.add(from);

        // Smart path generation based on building layout
        Point fromPoint = locationCoordinates.get(from);
        Point toPoint = locationCoordinates.get(to);

        // Determine building sections
        boolean fromEngineering = fromPoint.x < 400;
        boolean fromHealth = fromPoint.x >= 400 && fromPoint.x <= 700;
        boolean fromBusiness = fromPoint.x > 700;

        boolean toEngineering = toPoint.x < 400;
        boolean toHealth = toPoint.x >= 400 && toPoint.x <= 700;
        boolean toBusiness = toPoint.x > 700;

        // Add intermediate points based on building transitions
        if ((fromEngineering && toHealth) || (fromHealth && toEngineering)) {
            path.add("Elevator A");
            path.add("Information Desk");
        } else if ((fromHealth && toBusiness) || (fromBusiness && toHealth)) {
            path.add("Elevator B");
            path.add("Information Desk");
        } else if (fromEngineering && toBusiness) {
            path.add("Elevator A");
            path.add("Information Desk");
            path.add("Elevator B");
        }

        // Add destination building entrance
        if (toEngineering && !fromEngineering) {
            path.add("Engineering Main Entrance");
        } else if (toHealth && !fromHealth) {
            path.add("Health Sciences Entrance");
        } else if (toBusiness && !fromBusiness) {
            path.add("Business School Entrance");
        }

        path.add(to);
        return path.toArray(new String[0]);
    }

    private ArrayList<Point> generatePathPoints(String[] locations) {
        ArrayList<Point> points = new ArrayList<>();
        for (String location : locations) {
            points.add(locationCoordinates.get(location));
        }

        // Add intermediate points for smoother animation
        ArrayList<Point> detailedPath = new ArrayList<>();
        for (int i = 0; i < points.size() - 1; i++) {
            Point start = points.get(i);
            Point end = points.get(i + 1);

            detailedPath.add(start);

            // Add 5 intermediate points between each major point
            int steps = 10;
            for (int j = 1; j < steps; j++) {
                double ratio = (double) j / steps;
                int x = (int) (start.x + (end.x - start.x) * ratio);
                int y = (int) (start.y + (end.y - start.y) * ratio);
                detailedPath.add(new Point(x, y));
            }
        }
        detailedPath.add(points.get(points.size() - 1));

        return detailedPath;
    }

    private String getDirectionText(String from, String to) {
        Point fromPt = locationCoordinates.get(from);
        Point toPt = locationCoordinates.get(to);

        if (fromPt == null || toPt == null) return "Proceed to " + to;

        String direction = "";
        if (toPt.y < fromPt.y - 20) {
            direction = "⬆️ Go NORTH (toward Library area) ";
        } else if (toPt.y > fromPt.y + 20) {
            direction = "⬇️ Go SOUTH (toward Building Entrances) ";
        } else if (toPt.x > fromPt.x + 20) {
            direction = "➡️ Go EAST (toward Business School) ";
        } else if (toPt.x < fromPt.x - 20) {
            direction = "⬅️ Go WEST (toward Engineering) ";
        } else {
            direction = "➡️ Proceed to ";
        }

        // Add facility-specific instructions
        if (to.contains("Elevator")) {
            direction += " and take the elevator";
        } else if (to.contains("Stairs")) {
            direction += " and use the stairs";
        } else if (to.contains("Emergency")) {
            direction += " - Emergency Exit";
        } else if (to.contains("Restrooms")) {
            direction += " - Restrooms";
        }

        return direction + to;
    }

    private String calculateWalkingTime(int steps) {
        return String.valueOf(steps * 3); // 3 minutes per major step
    }

    private void updateNavigationProgress() {
        if (animationStep < pathPoints.size()) {
            int progress = (animationStep * 100) / pathPoints.size();
            StringBuilder navText = new StringBuilder();
            navText.append("🚶 Navigation in Progress... ").append(progress).append("%\n\n");

            if (animationStep < pathPoints.size() - 1) {
                navText.append("📍 Moving to next point...\n\n");
            } else {
                navText.append("🎉 You have arrived at your destination!\n\n");
            }

            navText.append("📊 Progress: ").append(animationStep).append("/").append(pathPoints.size()).append(" points\n");
            directionsArea.setText(navText.toString());
        }
    }

    // Map Panel for drawing the detailed campus map
    class MapPanel extends JPanel {
        private Point currentPosition;
        private Point destination;
        private String[] pathLocations;
        private final int PERSON_RADIUS = 6;

        public MapPanel() {
            setBackground(new Color(248, 249, 250));
            setPreferredSize(new Dimension(1200, 800));
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

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            drawBuildingLayout(g2d);
            drawPathways(g2d);
            drawPath(g2d);
            drawLocationLabels(g2d);
            drawPerson(g2d);
        }

        private void drawBuildingLayout(Graphics2D g2d) {
            // === MAIN HALLWAYS ===
            g2d.setColor(new Color(220, 220, 220));
            g2d.setStroke(new BasicStroke(8));
            // Main horizontal corridor
            g2d.drawLine(100, 400, 900, 400);
            // Vertical corridors
            g2d.drawLine(350, 100, 350, 400);
            g2d.drawLine(700, 100, 700, 400);

            // === BUILDING OUTLINES ===
            // Engineering Building (Left)
            g2d.setColor(new Color(173, 216, 230, 100));
            g2d.fillRect(100, 250, 250, 200);
            g2d.setColor(Color.BLUE);
            g2d.drawRect(100, 250, 250, 200);

            // Health Sciences (Center)
            g2d.setColor(new Color(144, 238, 144, 100));
            g2d.fillRect(350, 250, 350, 200);
            g2d.setColor(Color.GREEN.darker());
            g2d.drawRect(350, 250, 350, 200);

            // Business School (Right)
            g2d.setColor(new Color(255, 255, 153, 100));
            g2d.fillRect(700, 250, 250, 200);
            g2d.setColor(Color.ORANGE);
            g2d.drawRect(700, 250, 250, 200);

            // Library & Labs (Top)
            g2d.setColor(new Color(255, 182, 193, 100));
            g2d.fillRect(200, 50, 600, 200);
            g2d.setColor(Color.PINK);
            g2d.drawRect(200, 50, 600, 200);
        }

        private void drawPathways(Graphics2D g2d) {
            g2d.setColor(new Color(200, 200, 200));
            g2d.setStroke(new BasicStroke(3));

            // Draw doors
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(2));
            int[][] doors = {{150,450}, {500,450}, {800,450}, {350,400}, {700,400}};
            for (int[] door : doors) {
                g2d.drawLine(door[0]-5, door[1], door[0]+5, door[1]);
            }

            // Draw elevators
            g2d.setColor(Color.CYAN);
            g2d.fillRect(340, 380, 20, 40);
            g2d.fillRect(690, 380, 20, 40);
            g2d.setColor(Color.BLACK);
            g2d.drawString("E", 345, 400);
            g2d.drawString("E", 695, 400);

            // Draw stairs
            g2d.setColor(Color.GRAY);
            int[][] stairs = {{400,450}, {750,450}};
            for (int[] stair : stairs) {
                for (int i = 0; i < 4; i++) {
                    g2d.drawRect(stair[0] + i*3, stair[1] - i*3, 3, 3);
                }
            }

            // Draw emergency exits
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 10));
            g2d.drawString("EXIT", 95, 340);
            g2d.drawString("EXIT", 945, 340);
        }

        private void drawPath(Graphics2D g2d) {
            if (pathLocations == null || pathLocations.length < 2) return;

            g2d.setColor(new Color(52, 152, 219, 180));
            g2d.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            for (int i = 0; i < pathLocations.length - 1; i++) {
                Point start = locationCoordinates.get(pathLocations[i]);
                Point end = locationCoordinates.get(pathLocations[i + 1]);
                if (start != null && end != null) {
                    g2d.drawLine(start.x, start.y, end.x, end.y);
                }
            }
        }

        private void drawLocationLabels(Graphics2D g2d) {
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 9));

            for (java.util.Map.Entry<String, Point> entry : locationCoordinates.entrySet()) {
                Point point = entry.getValue();
                String name = entry.getKey();

                // Draw small circle for location
                g2d.setColor(new Color(52, 152, 219));
                g2d.fill(new Ellipse2D.Double(point.x - 3, point.y - 3, 6, 6));

                // Draw label
                g2d.setColor(Color.BLACK);
                // Split long names for better display
                if (name.length() > 20) {
                    String[] parts = name.split(" ");
                    if (parts.length > 1) {
                        g2d.drawString(parts[0], point.x - 15, point.y - 8);
                        g2d.drawString(parts[1], point.x - 15, point.y + 15);
                    } else {
                        g2d.drawString(name.substring(0, Math.min(15, name.length())), point.x - 20, point.y - 8);
                    }
                } else {
                    g2d.drawString(name, point.x - 15, point.y - 8);
                }
            }
        }

        private void drawPerson(Graphics2D g2d) {
            if (currentPosition == null) return;

            // Draw walking person (simplified)
            g2d.setColor(new Color(231, 76, 60)); // Red color

            // Body
            g2d.fill(new Ellipse2D.Double(
                    currentPosition.x - PERSON_RADIUS,
                    currentPosition.y - PERSON_RADIUS,
                    PERSON_RADIUS * 2,
                    PERSON_RADIUS * 2
            ));

            // Direction indicator
            if (isNavigating && animationStep < pathPoints.size() - 1) {
                Point nextPoint = pathPoints.get(animationStep + 1);
                double angle = Math.atan2(nextPoint.y - currentPosition.y, nextPoint.x - currentPosition.x);

                // Draw direction line
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(1));
                int lineLength = 15;
                int endX = currentPosition.x + (int)(Math.cos(angle) * lineLength);
                int endY = currentPosition.y + (int)(Math.sin(angle) * lineLength);
                g2d.drawLine(currentPosition.x, currentPosition.y, endX, endY);
            }

            // Glow effect
            g2d.setColor(new Color(231, 76, 60, 50));
            g2d.fill(new Ellipse2D.Double(
                    currentPosition.x - PERSON_RADIUS - 3,
                    currentPosition.y - PERSON_RADIUS - 3,
                    (PERSON_RADIUS + 3) * 2,
                    (PERSON_RADIUS + 3) * 2
            ));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CampusMap map = new CampusMap();

            // Add button listeners
            map.getDirectionsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String from = (String) map.currentLocationCombo.getSelectedItem();
                    String to = (String) map.destinationCombo.getSelectedItem();
                    map.showDirections(from, to);
                }
            });

            map.startNavigationButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!map.isNavigating) {
                        map.isNavigating = true;
                        map.isPaused = false;
                        map.animationStep = 0;
                        map.startNavigationButton.setText("⏹️ Stop Navigation");
                        map.pauseButton.setText("⏸️ Pause");
                        map.pauseButton.setEnabled(true);
                        map.animationTimer.start();
                    } else {
                        map.isNavigating = false;
                        map.animationTimer.stop();
                        map.startNavigationButton.setText("🚶 Start Navigation");
                        map.pauseButton.setEnabled(false);
                    }
                }
            });

            map.pauseButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    map.isPaused = !map.isPaused;
                    if (map.isPaused) {
                        map.pauseButton.setText("▶️ Resume");
                    } else {
                        map.pauseButton.setText("⏸️ Pause");
                    }
                }
            });

            map.setVisible(true);
        });
    }
}