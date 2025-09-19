package ac.za.postapp.Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class MapPanel extends JPanel {
    private HashMap<String, Point> locationCoordinates = new HashMap<>();
    private List<Point> currentPath = new ArrayList<>();
    private Point currentLocation;
    private Timer animationTimer;
    private int currentStep = 0;
    private boolean isAnimating = false;
    private JTextArea directionsArea;
    private JComboBox<String> startComboBox;
    private JComboBox<String> endComboBox;
    private JButton navigateButton;
    private JButton stopButton;

    // For A* algorithm
    private int[][] mapGrid;
    private final int GRID_SIZE = 20;
    private final int MAP_WIDTH = 40;
    private final int MAP_HEIGHT = 30;

    public MapPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 650));
        setBackground(new Color(245, 247, 250));

        // Initialize map grid and locations
        initializeMapGrid();
        initializeLocations();

        // Control panel
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.NORTH);

        // Directions panel
        directionsArea = new JTextArea(5, 30);
        directionsArea.setEditable(false);
        directionsArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        directionsArea.setLineWrap(true);
        directionsArea.setWrapStyleWord(true);
        directionsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        directionsArea.setBackground(new Color(240, 240, 240));
        JScrollPane scrollPane = new JScrollPane(directionsArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Live Directions"));
        add(scrollPane, BorderLayout.SOUTH);
    }

    private void initializeMapGrid() {
        mapGrid = new int[MAP_HEIGHT][MAP_WIDTH];

        // Initialize empty grid (0 = walkable, 1 = obstacle)
        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                // Add some random obstacles for demonstration
                if (Math.random() < 0.2) {
                    mapGrid[y][x] = 1; // Obstacle
                } else {
                    mapGrid[y][x] = 0; // Walkable
                }
            }
        }

        // Ensure paths between major locations are clear
        clearPathBetween(2, 2, 7, 2);   // Engineering to Health Sciences
        clearPathBetween(7, 2, 12, 2);  // Health Sciences to Business
        clearPathBetween(2, 2, 4, 7);   // Engineering to Library
        clearPathBetween(7, 2, 9, 7);   // Health Sciences to Food Court
        clearPathBetween(12, 2, 14, 7); // Business to Computer Lab
        clearPathBetween(9, 7, 14, 14); // Food Court to Toilets
        clearPathBetween(14, 7, 19, 16); // Computer Lab to Exit
    }

    private void clearPathBetween(int x1, int y1, int x2, int y2) {
        // Bresenham's line algorithm to clear a path
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        int err = dx - dy;

        while (true) {
            // Clear current cell and surrounding cells
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (x1+i >= 0 && x1+i < MAP_WIDTH && y1+j >= 0 && y1+j < MAP_HEIGHT) {
                        mapGrid[y1+j][x1+i] = 0;
                    }
                }
            }

            if (x1 == x2 && y1 == y2) break;

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y1 += sy;
            }
        }
    }

    private void initializeLocations() {
        // Map grid coordinates to pixel coordinates
        locationCoordinates.put("Engineering Building", gridToPixel(2, 2));
        locationCoordinates.put("Health Sciences Building", gridToPixel(7, 2));
        locationCoordinates.put("Business Building", gridToPixel(12, 2));
        locationCoordinates.put("Library", gridToPixel(4, 7));
        locationCoordinates.put("Food Court", gridToPixel(9, 7));
        locationCoordinates.put("Computer Lab", gridToPixel(14, 7));
        locationCoordinates.put("Toilets", gridToPixel(14, 14));
        locationCoordinates.put("Exit", gridToPixel(19, 16));

        // Set initial location
        currentLocation = locationCoordinates.get("Engineering Building");
    }

    private Point gridToPixel(int gridX, int gridY) {
        int pixelX = gridX * GRID_SIZE + GRID_SIZE/2;
        int pixelY = gridY * GRID_SIZE + GRID_SIZE/2;
        return new Point(pixelX, pixelY);
    }

    private Point pixelToGrid(Point pixel) {
        int gridX = pixel.x / GRID_SIZE;
        int gridY = pixel.y / GRID_SIZE;
        return new Point(gridX, gridY);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(new Color(230, 230, 230));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] locations = locationCoordinates.keySet().toArray(new String[0]);
        Arrays.sort(locations);

        startComboBox = new JComboBox<>(locations);
        endComboBox = new JComboBox<>(locations);

        navigateButton = new JButton("Start Navigation");
        navigateButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        navigateButton.setBackground(new Color(40, 167, 69));
        navigateButton.setForeground(Color.WHITE);
        navigateButton.setFocusPainted(false);
        navigateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        stopButton = new JButton("Stop");
        stopButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        stopButton.setBackground(new Color(220, 53, 69));
        stopButton.setForeground(Color.WHITE);
        stopButton.setFocusPainted(false);
        stopButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        stopButton.setEnabled(false);

        navigateButton.addActionListener(e -> {
            try {
                startNavigation();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        stopButton.addActionListener(e -> {
            try {
                stopNavigation();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        panel.add(new JLabel("From:"));
        panel.add(startComboBox);
        panel.add(new JLabel("To:"));
        panel.add(endComboBox);
        panel.add(navigateButton);
        panel.add(stopButton);

        return panel;
    }

    private void startNavigation() throws InterruptedException {
        String startName = (String) startComboBox.getSelectedItem();
        String endName = (String) endComboBox.getSelectedItem();

        if (startName != null && endName != null && !startName.equals(endName)) {
            Point start = locationCoordinates.get(startName);
            Point end = locationCoordinates.get(endName);

            // Convert to grid coordinates for pathfinding
            Point startGrid = pixelToGrid(start);
            Point endGrid = pixelToGrid(end);

            currentPath = findPathAStar(startGrid, endGrid);

            if (currentPath.isEmpty()) {
                directionsArea.setText("No path found! There may be obstacles blocking the way.");
                return;
            }

            // Convert grid path back to pixel coordinates
            List<Point> pixelPath = new ArrayList<>();
            for (Point gridPoint : currentPath) {
                pixelPath.add(gridToPixel(gridPoint.x, gridPoint.y));
            }
            currentPath = pixelPath;

            currentStep = 0;
            isAnimating = true;

            navigateButton.setEnabled(false);
            stopButton.setEnabled(true);

            generateDirections(start, end);

            if (animationTimer != null) {
                animationTimer.wait();
            }

            // FIXED: Using javax.swing.Timer with ActionListener
            animationTimer = new Timer();
            animationTimer.wait();
        }
    }

    private void stopNavigation() throws InterruptedException {
        if (animationTimer != null) {
            animationTimer.wait();
        }
        isAnimating = false;
        navigateButton.setEnabled(true);
        stopButton.setEnabled(false);
        directionsArea.append("\n\n⏹️ Navigation stopped.");
    }

    // A* Pathfinding Algorithm
    private List<Point> findPathAStar(Point start, Point end) {
        // Priority queue for open nodes
        PriorityQueue<Node> openSet = new PriorityQueue<>();
        // Maps to track costs and paths
        Map<Point, Double> gScore = new HashMap<>(); // Cost from start to node
        Map<Point, Double> fScore = new HashMap<>(); // Estimated total cost (g + h)
        Map<Point, Point> cameFrom = new HashMap<>(); // Navigation path

        // Initialize start node
        gScore.put(start, 0.0);
        fScore.put(start, heuristic(start, end));
        openSet.add(new Node(start, fScore.get(start)));

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            // If we reached the destination
            if (current.point.equals(end)) {
                return reconstructPath(cameFrom, current.point);
            }

            // Check all neighbors
            for (Point neighbor : getNeighbors(current.point)) {
                // Calculate tentative gScore
                double tentativeGScore = gScore.get(current.point) +
                        distance(current.point, neighbor);

                // If this path to neighbor is better
                if (!gScore.containsKey(neighbor) || tentativeGScore < gScore.get(neighbor)) {
                    cameFrom.put(neighbor, current.point);
                    gScore.put(neighbor, tentativeGScore);
                    fScore.put(neighbor, tentativeGScore + heuristic(neighbor, end));

                    // Add to open set if not already there
                    if (!openSet.contains(new Node(neighbor, 0))) {
                        openSet.add(new Node(neighbor, fScore.get(neighbor)));
                    }
                }
            }
        }

        // No path found
        return new ArrayList<>();
    }

    // Helper methods for A* algorithm
    private double heuristic(Point a, Point b) {
        // Manhattan distance
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    private double distance(Point a, Point b) {
        // Euclidean distance
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    private List<Point> getNeighbors(Point point) {
        List<Point> neighbors = new ArrayList<>();
        int[][] directions = {{0,1}, {1,0}, {0,-1}, {-1,0}, {1,1}, {1,-1}, {-1,1}, {-1,-1}};

        for (int[] dir : directions) {
            int newX = point.x + dir[0];
            int newY = point.y + dir[1];

            // Check bounds and obstacles
            if (newX >= 0 && newX < MAP_WIDTH && newY >= 0 && newY < MAP_HEIGHT &&
                    mapGrid[newY][newX] == 0) {
                neighbors.add(new Point(newX, newY));
            }
        }

        return neighbors;
    }

    private List<Point> reconstructPath(Map<Point, Point> cameFrom, Point current) {
        List<Point> path = new ArrayList<>();
        path.add(current);

        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.add(0, current);
        }

        return path;
    }

    // Node class for A* algorithm
    private class Node implements Comparable<Node> {
        Point point;
        double fScore;

        Node(Point point, double fScore) {
            this.point = point;
            this.fScore = fScore;
        }

        @Override
        public int compareTo(Node other) {
            return Double.compare(this.fScore, other.fScore);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Node other = (Node) obj;
            return point.equals(other.point);
        }

        @Override
        public int hashCode() {
            return point.hashCode();
        }
    }

    private void generateDirections(Point start, Point end) {
        directionsArea.setText("");
        directionsArea.append("🗺️  Navigation Started\n");
        directionsArea.append("📍 Starting from: " + getLocationName(start) + "\n");
        directionsArea.append("🎯 Destination: " + getLocationName(end) + "\n");

        double distance = Math.sqrt(Math.pow(end.x - start.x, 2) + Math.pow(end.y - start.y, 2));
        directionsArea.append(String.format("📏 Total distance: %.1f meters\n", distance/2));
        directionsArea.append("➡️  Follow the blue path on the map\n");
    }

    private void updateDirections() {
        if (currentStep > 0 && currentStep < currentPath.size()) {
            Point current = currentPath.get(currentStep);
            Point previous = currentPath.get(currentStep-1);

            double dx = current.x - previous.x;
            double dy = current.y - previous.y;

            String direction;
            if (Math.abs(dx) > Math.abs(dy)) {
                direction = dx > 0 ? "East" : "West";
            } else {
                direction = dy > 0 ? "South" : "North";
            }

            Point destination = currentPath.get(currentPath.size()-1);
            double distance = Math.sqrt(Math.pow(destination.x - current.x, 2) +
                    Math.pow(destination.y - current.y, 2));

            int timeEstimate = (int)(distance / 20); // Rough time estimate

            directionsArea.setText("🧭 Current direction: " + direction + "\n");
            directionsArea.append(String.format("📏 Distance to destination: %.1f meters\n", distance/2));
            directionsArea.append(String.format("⏱️  Estimated time: %d seconds", timeEstimate));
        }
    }

    private String getLocationName(Point point) {
        for (String entry : locationCoordinates.keySet()) {
            if (entry.getClass().equals(point)) {
                return entry;
            }
        }
        return "Unknown Location";
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw grid background
        g2.setColor(new Color(240, 240, 240));
        for (int y = 0; y < MAP_HEIGHT; y++) {
            for (int x = 0; x < MAP_WIDTH; x++) {
                if (mapGrid[y][x] == 1) {
                    g2.setColor(new Color(200, 200, 200));
                    g2.fillRect(x * GRID_SIZE, y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
                }
                g2.setColor(new Color(220, 220, 220));
                g2.drawRect(x * GRID_SIZE, y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
            }
        }

        // Draw paths
        if (!currentPath.isEmpty()) {
            g2.setColor(new Color(30, 144, 255, 200));
            g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            for (int i = 1; i < currentPath.size(); i++) {
                Point p1 = currentPath.get(i-1);
                Point p2 = currentPath.get(i);
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);
            }

            // Draw direction arrows
            g2.setColor(new Color(0, 100, 200));
            for (int i = 5; i < currentPath.size(); i += 5) {
                Point p1 = currentPath.get(i-1);
                Point p2 = currentPath.get(i);
                drawArrow(g2, p1.x, p1.y, p2.x, p2.y);
            }
        }

        // Draw locations
        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        for (Map.Entry<String, Point> entry : locationCoordinates.entrySet()) {
            String name = entry.getKey();
            Point point = entry.getValue();

            // Draw location circle
            GradientPaint gradient = new GradientPaint(
                    point.x - 20, point.y - 20, new Color(70, 130, 180),
                    point.x + 20, point.y + 20, new Color(30, 144, 255)
            );
            g2.setPaint(gradient);
            g2.fillOval(point.x - 15, point.y - 15, 30, 30);

            g2.setColor(Color.BLACK);
            g2.drawOval(point.x - 15, point.y - 15, 30, 30);

            // Draw label with background
            g2.setColor(new Color(255, 255, 255, 200));
            g2.fillRoundRect(point.x - 25, point.y - 35,
                    g2.getFontMetrics().stringWidth(name) + 10, 20, 5, 5);
            g2.setColor(Color.BLACK);
            g2.drawString(name, point.x - 20, point.y - 20);
        }

        // Draw current location
        if (currentLocation != null) {
            // Pulsing effect
            int pulse = (int)(5 * Math.sin(System.currentTimeMillis() / 200.0));

            // Outer circle
            g2.setColor(new Color(255, 0, 0, 100));
            g2.fillOval(currentLocation.x - 15 - pulse, currentLocation.y - 15 - pulse,
                    30 + 2*pulse, 30 + 2*pulse);

            // Inner circle
            g2.setColor(Color.RED);
            g2.fillOval(currentLocation.x - 10, currentLocation.y - 10, 20, 20);

            // White center
            g2.setColor(Color.WHITE);
            g2.fillOval(currentLocation.x - 5, currentLocation.y - 5, 10, 10);
        }

        // Draw legend
        g2.setColor(Color.BLACK);
        g2.drawString("📍 Your Location", 10, MAP_HEIGHT * GRID_SIZE + 20);
        g2.setColor(new Color(30, 144, 255));
        g2.drawString("➡️ Navigation Path", 120, MAP_HEIGHT * GRID_SIZE + 20);
        g2.setColor(new Color(200, 200, 200));
        g2.drawString("⬜ Obstacles", 250, MAP_HEIGHT * GRID_SIZE + 20);
    }

    private void drawArrow(Graphics2D g2, int x1, int y1, int x2, int y2) {
        double angle = Math.atan2(y2 - y1, x2 - x1);
        int arrowSize = 10;

        Polygon arrowHead = new Polygon();
        arrowHead.addPoint(0, 0);
        arrowHead.addPoint(-arrowSize, -arrowSize/2);
        arrowHead.addPoint(-arrowSize, arrowSize/2);

        AffineTransform transform = new AffineTransform();
        transform.translate(x2, y2);
        transform.rotate(angle - Math.PI/2);

        Shape transformedArrow = transform.createTransformedShape(arrowHead);
        g2.fill(transformedArrow);
    }
}