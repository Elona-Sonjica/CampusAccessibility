package ac.za.postapp.Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CampusMap extends JFrame {
    private JComboBox<String> currentLocationCombo;
    private JComboBox<String> destinationCombo;
    private JTextArea directionsArea;
    private JButton getDirectionsButton;

    public CampusMap() {
        setTitle("CPUT D6 Campus Map");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        // ===== Top Panel =====
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // ==== Current Location Dropdown ====
        gbc.gridx = 0; gbc.gridy = 0;
        topPanel.add(new JLabel("Current Location:"), gbc);
        gbc.gridx = 1;
        currentLocationCombo = new JComboBox<>(generateLocations());
        topPanel.add(currentLocationCombo, gbc);

        // ==== Destination Dropdown ====
        gbc.gridx = 0; gbc.gridy = 1;
        topPanel.add(new JLabel("Destination:"), gbc);
        gbc.gridx = 1;
        destinationCombo = new JComboBox<>(generateLocations());
        topPanel.add(destinationCombo, gbc);

        // ==== Get Directions Button ====
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        getDirectionsButton = new JButton("Get Directions");
        topPanel.add(getDirectionsButton, gbc);

        add(topPanel, BorderLayout.NORTH);

        // ===== Directions Area =====
        directionsArea = new JTextArea();
        directionsArea.setEditable(false);
        directionsArea.setLineWrap(true);
        directionsArea.setWrapStyleWord(true);
        directionsArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(directionsArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        // ===== Button Action =====
        getDirectionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String from = (String) currentLocationCombo.getSelectedItem();
                String to = (String) destinationCombo.getSelectedItem();
                showDirections(from, to);
            }
        });
    }

    // Generate all campus locations
    private String[] generateLocations() {
        String[] faculties = {"Engineering", "Health Sciences", "Business", "Education", "Applied Sciences"};
        String[] locations = new String[5 * 5 + 15 + 5]; // 5 floors per faculty + labs + library + food + toilets
        int index = 0;

        // Add faculty floors
        for (String faculty : faculties) {
            for (int floor = 1; floor <= 5; floor++) {
                locations[index++] = faculty + " - Floor " + floor;
            }
        }

        // Add labs
        for (int i = 1; i <= 5; i++) locations[index++] = "Lab Room " + i;
        for (int i = 1; i <= 5; i++) locations[index++] = "Computer Lab " + i;
        // Add toilets
        for (int i = 1; i <= 3; i++) locations[index++] = "Toilet Area " + i;
        // Add library
        locations[index++] = "Library";
        // Add food areas
        for (int i = 1; i <= 2; i++) locations[index++] = "Food Area " + i;

        return locations;
    }

    // Show directions between locations
    private void showDirections(String from, String to) {
        if (from.equals(to)) {
            directionsArea.setText("You are already at your destination: " + to);
            return;
        }

        StringBuilder directions = new StringBuilder();
        directions.append("Directions from ").append(from).append(" to ").append(to).append(":\n\n");

        directions.append("- Walk straight from ").append(from).append("\n");
        directions.append("- Go past the hallway / stairs\n");
        directions.append("- Turn right or left as necessary\n");
        directions.append("- Arrive at ").append(to).append("\n\n");
        directions.append("⚠️ Note: This is a simulated route for guidance purposes.");

        directionsArea.setText(directions.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CampusMap map = new CampusMap();
            map.setVisible(true);
        });
    }
}