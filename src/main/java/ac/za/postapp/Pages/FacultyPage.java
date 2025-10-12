package ac.za.postapp.Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FacultyPage extends JFrame {
    private JList<String> facilitiesList;
    private JTextArea facilityInfoArea;
    private JButton viewButton;
    private JLabel iconLabel;

    private String[] facilities = {
            "Library",
            "Cafeteria",
            "Computer Lab",
            "Health Center",
            "Disability Support Office",
            "Lecture Hall A",
            "Sports Center"
    };

    private String[] facilityInfo = {
            "Library:\n• Accessible entrance\n• Elevators available\n• Quiet study areas\n• Computer lab\n• Wheelchair accessible restrooms",
            "Cafeteria:\n• Wheelchair accessible\n• Menu available in large print\n• Assistance available on request\n• Braille menus available",
            "Computer Lab:\n• Wheelchair accessible\n• Adjustable seats\n• Screen reader software\n• Voice recognition software\n• Large monitor stations",
            "Health Center:\n• Full accessibility with ramps\n• Wide doors\n• Priority services for students with disabilities\n• Accessible examination rooms",
            "Disability Support Office:\n• Navigation assistance\n• Learning aids\n• Student support programs\n• Accessibility resources\n• Counseling services",
            "Lecture Hall A:\n• Ramps and elevators\n• Hearing loop system\n• Reserved seating\n• Wheelchair accessible\n• Assistive listening devices",
            "Sports Center:\n• Accessible changing rooms\n• Adaptive sports equipment\n• Wheelchair accessible pool\n• Accessible fitness equipment"
    };

    public FacultyPage() {
        setTitle("Campus Facilities - Accessibility Information");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Changed to DISPOSE_ON_CLOSE
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 247, 250));

        // Title
        JLabel titleLabel = new JLabel("🏢 Campus Facilities Accessibility", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(52, 73, 94));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));

        // Facilities list on left
        facilitiesList = new JList<>(facilities);
        facilitiesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        facilitiesList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        facilitiesList.setBackground(Color.WHITE);
        facilitiesList.setBorder(BorderFactory.createTitledBorder("Select Facility"));

        JScrollPane listScrollPane = new JScrollPane(facilitiesList);
        listScrollPane.setPreferredSize(new Dimension(250, 0));

        // Right panel with info and icon
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));

        // Facility information
        facilityInfoArea = new JTextArea();
        facilityInfoArea.setEditable(false);
        facilityInfoArea.setLineWrap(true);
        facilityInfoArea.setWrapStyleWord(true);
        facilityInfoArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        facilityInfoArea.setBackground(Color.WHITE);
        facilityInfoArea.setBorder(BorderFactory.createTitledBorder("Accessibility Information"));

        JScrollPane infoScrollPane = new JScrollPane(facilityInfoArea);

        // Icon placeholder (removed file loading since icons might not exist)
        iconLabel = new JLabel("📷", JLabel.CENTER);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        iconLabel.setBorder(BorderFactory.createTitledBorder("Facility"));
        iconLabel.setPreferredSize(new Dimension(150, 150));

        rightPanel.add(infoScrollPane, BorderLayout.CENTER);
        rightPanel.add(iconLabel, BorderLayout.EAST);

        contentPanel.add(listScrollPane, BorderLayout.WEST);
        contentPanel.add(rightPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(new Color(245, 247, 250));

        viewButton = new JButton("View Facility Information");
        styleButton(viewButton, new Color(0, 123, 255));
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFacilityInfo();
            }
        });

        JButton closeButton = new JButton("Close");
        styleButton(closeButton, new Color(108, 117, 125));
        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(viewButton);
        buttonPanel.add(closeButton);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Add double-click functionality to list
        facilitiesList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showFacilityInfo();
            }
        });
    }

    private void showFacilityInfo() {
        int index = facilitiesList.getSelectedIndex();
        if (index != -1) {
            facilityInfoArea.setText(facilityInfo[index]);

            // Set appropriate emoji based on facility type
            String[] emojis = {"📚", "🍽️", "💻", "🏥", "♿", "🎓", "⚽"};
            iconLabel.setText(emojis[index]);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select a facility first.",
                    "Selection Required",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FacultyPage app = new FacultyPage();
            app.setVisible(true);
        });
    }
}