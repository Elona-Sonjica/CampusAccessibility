package ac.za.postapp.Pages;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class MapPanel extends JPanel {

    // Example campus structure
    private HashMap<String, Rectangle> locations = new HashMap<>();
    private String currentLocation = "Entrance";

    public MapPanel() {
        setPreferredSize(new Dimension(600, 600));
        setBackground(new Color(230, 230, 230));

        // Define buildings, floors, labs, etc.
        locations.put("Engineering Building", new Rectangle(50, 50, 100, 100));
        locations.put("Health Sciences Building", new Rectangle(200, 50, 100, 100));
        locations.put("Business Building", new Rectangle(350, 50, 100, 100));
        locations.put("Library", new Rectangle(50, 200, 150, 100));
        locations.put("Food Court", new Rectangle(250, 200, 150, 100));
        locations.put("Computer Lab", new Rectangle(450, 200, 100, 100));
        locations.put("Toilets", new Rectangle(300, 350, 50, 50));
        locations.put("Exit", new Rectangle(500, 400, 50, 50));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw all locations
        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        for (String name : locations.keySet()) {
            Rectangle rect = locations.get(name);

            // Draw building or area
            g2.setColor(Color.LIGHT_GRAY);
            g2.fill(rect);

            g2.setColor(Color.BLACK);
            g2.draw(rect);

            // Draw label
            g2.drawString(name, rect.x + 5, rect.y + rect.height / 2);
        }

        // Draw current location marker
        Rectangle curr = locations.get(currentLocation);
        if (curr != null) {
            g2.setColor(Color.RED);
            g2.fillOval(curr.x + curr.width/4, curr.y + curr.height/4, curr.width/2, curr.height/2);
        }
    }

    public void setCurrentLocation(String loc) {
        if (locations.containsKey(loc)) {
            this.currentLocation = loc;
            repaint();
        }
    }
}
