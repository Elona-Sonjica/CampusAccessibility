package ac.za.postapp.Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.image.BufferedImage;

/**
 *
 * @author sinox
 */

public class FacultyPage extends JPanel {

    private JTextArea facilityInfoArea;
    private JLabel iconLabel;
    private JPanel cardsPanel;

    private Object[][] facilityData = {
            {"Library", "Accessible entrance, elevators, quiet study areas and computer lab.", "library.png"},
            {"Cafeteria", "Wheelchair accessible, menu available in large print. Assistance available on request.", "cafeteria.png"},
            {"Computer Lab", "Wheelchair accessible, adjustable seats. Screen reader software installed.", "computer.png"},
            {"Health Center", "Full accessibility with ramps and wide doors. Priority services for students with disabilities.", "health.png"},
            {"Disability Support Office", "Assistance for navigation, learning aids, and student support programs.", "support.png"},
            {"Lecture Hall A", "Equipped with ramps, hearing loop system, and reserved seating.", "lecture.png"},
            {"Sports Center", "Accessible changing rooms, adaptive sports equipment available.", "sports.png"}
    };

    private final Color PRIMARY_BLUE = new Color(30, 144, 255);
    private final Color SECONDARY_BLUE = new Color(100, 149, 237);
    private final Color BACKGROUND_BLUE = new Color(240, 248, 255);
    private final Color CARD_BLUE = new Color(224, 238, 255);

    public FacultyPage() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_BLUE);

        JLabel headerLabel = new JLabel("Campus Facilities", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setOpaque(true);
        headerLabel.setBackground(PRIMARY_BLUE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        cardsPanel = new JPanel();
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
        cardsPanel.setBackground(BACKGROUND_BLUE);

        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);

        JPanel detailPanel = createDetailPanel();

        createFacilityCards();

        add(headerLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(detailPanel, BorderLayout.SOUTH);

        if (facilityData.length > 0) {
            showFacilityDetails(0);
        }
    }

    private void createFacilityCards() {
        for (int i = 0; i < facilityData.length; i++) {
            JPanel card = createFacilityCard(i);
            cardsPanel.add(card);
            cardsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
    }

    private JPanel createFacilityCard(int index) {
        JPanel card = new JPanel(new BorderLayout(8, 0));
        card.setBackground(CARD_BLUE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_BLUE, 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // image loading with error handling
        JLabel iconLabel = createImageLabel(facilityData[index][2].toString(), 40, 40);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(CARD_BLUE);

        JLabel nameLabel = new JLabel(facilityData[index][0].toString());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setForeground(PRIMARY_BLUE);

        String briefDescription = facilityData[index][1].toString();
        if (briefDescription.length() > 60) {
            briefDescription = briefDescription.substring(0, 57) + "...";
        }

        JLabel infoLabel = new JLabel("<html><div style='width: 200px; font-size: 11px;'>" + briefDescription + "</div></html>");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        infoLabel.setForeground(Color.DARK_GRAY);

        textPanel.add(nameLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        textPanel.add(infoLabel);

        JButton viewButton = new JButton("View");
        styleButton(viewButton);

        final int facilityIndex = index;
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFacilityDetails(facilityIndex);
            }
        });

        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        card.add(viewButton, BorderLayout.EAST);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showFacilityDetails(facilityIndex);
            }
        });

        return card;
    }

    // method to handle image loading with error checking
    private JLabel createImageLabel(String imageName, int width, int height) {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(width, height));

        try {
            // Try multiple possible locations for the images
            ImageIcon icon = loadImageIcon(imageName, width, height);
            if (icon != null) {
                label.setIcon(icon);
                System.out.println(" Image loaded successfully: " + imageName);
            } else {
                System.out.println(" Image not found: " + imageName);
                setFallbackIcon(label, width, height);
            }
        } catch (Exception e) {
            System.out.println(" Error loading image: " + imageName + " - " + e.getMessage());
            setFallbackIcon(label, width, height);
        }

        return label;
    }

    // Improved image loading method that checks multiple locations
    private ImageIcon loadImageIcon(String imageName, int width, int height) {
        // Try different possible paths
        String[] possiblePaths = {
                "icons/" + imageName,
                "Icons/" + imageName,
                "src/main/resources/icons/" + imageName,
                "resources/icons/" + imageName,
                "ac/za/postapp/icons/" + imageName,
                "ac/za/postapp/Icons/" + imageName,
                imageName // try direct path
        };

        for (String path : possiblePaths) {
            try {
                // Try loading from file system
                File imageFile = new File(path);
                if (imageFile.exists()) {
                    ImageIcon originalIcon = new ImageIcon(path);
                    if (originalIcon.getIconWidth() > 0) {
                        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                        return new ImageIcon(scaledImage);
                    }
                }

                // Try loading from classpath/resources
                java.net.URL imageUrl = getClass().getClassLoader().getResource(path);
                if (imageUrl == null) {
                    imageUrl = getClass().getResource("/" + path);
                }
                if (imageUrl == null) {
                    imageUrl = getClass().getResource(path);
                }

                if (imageUrl != null) {
                    ImageIcon originalIcon = new ImageIcon(imageUrl);
                    if (originalIcon.getIconWidth() > 0) {
                        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                        return new ImageIcon(scaledImage);
                    }
                }
            } catch (Exception e) {
                // Continue to next path
                System.out.println(" Failed to load from: " + path);
            }
        }

        return null;
    }

    // Fallback icon when images don't load
    private void setFallbackIcon(JLabel label, int width, int height) {
        // Create a colored circle as fallback
        ImageIcon fallbackIcon = createColoredCircle(width, height, PRIMARY_BLUE);
        label.setIcon(fallbackIcon);
    }

    // Create a colored circle as fallback icon
    private ImageIcon createColoredCircle(int width, int height, Color color) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Set rendering hints for better quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw circle
        g2d.setColor(color);
        g2d.fillOval(2, 2, width-4, height-4);

        // Draw border
        g2d.setColor(color.darker());
        g2d.drawOval(2, 2, width-4, height-4);

        // Add first letter of facility name as text (for debugging)
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));

        g2d.dispose();
        return new ImageIcon(image);
    }

    private JPanel createDetailPanel() {
        JPanel detailPanel = new JPanel(new BorderLayout(8, 8));
        detailPanel.setBackground(Color.WHITE);
        detailPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(2, 0, 0, 0, PRIMARY_BLUE),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        detailPanel.setPreferredSize(new Dimension(0, 180));

        iconLabel = new JLabel("", JLabel.CENTER);
        iconLabel.setPreferredSize(new Dimension(70, 70));

        facilityInfoArea = new JTextArea();
        facilityInfoArea.setEditable(false);
        facilityInfoArea.setLineWrap(true);
        facilityInfoArea.setWrapStyleWord(true);
        facilityInfoArea.setFont(new Font("Arial", Font.PLAIN, 12));
        facilityInfoArea.setBackground(Color.WHITE);
        facilityInfoArea.setMargin(new Insets(5, 5, 5, 5));

        JScrollPane infoScrollPane = new JScrollPane(facilityInfoArea);
        infoScrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_BLUE, 1),
                "Facility Details",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 12)
        ));

        detailPanel.add(iconLabel, BorderLayout.WEST);
        detailPanel.add(infoScrollPane, BorderLayout.CENTER);

        return detailPanel;
    }

    private void showFacilityDetails(int index) {
        facilityInfoArea.setText(getDetailedFacilityInfo(index));

        String imageName = facilityData[index][2].toString();
        try {
            ImageIcon icon = loadImageIcon(imageName, 60, 60);
            if (icon != null) {
                iconLabel.setIcon(icon);
            } else {
                // Use fallback icon
                iconLabel.setIcon(createColoredCircle(60, 60, PRIMARY_BLUE));
            }
        } catch (Exception e) {
            // Use fallback icon
            iconLabel.setIcon(createColoredCircle(60, 60, PRIMARY_BLUE));
        }
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 11));
        button.setBackground(PRIMARY_BLUE);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_BLUE, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(SECONDARY_BLUE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_BLUE);
            }
        });
    }

    private String getDetailedFacilityInfo(int index) {
        String facilityName = facilityData[index][0].toString();
        String basicInfo = facilityData[index][1].toString();

        switch (facilityName) {
            case "Library":
                return facilityName + ":\n" + basicInfo +
                        "\n\nFeatures:\n• Extended exam hours\n• Braille books\n• Audio books\n• Research help\n• Study rooms";

            case "Cafeteria":
                return facilityName + ":\n" + basicInfo +
                        "\n\nFeatures:\n• Dietary options\n• Extended hours\n• Online orders\n• Outdoor seating";

            case "Computer Lab":
                return facilityName + ":\n" + basicInfo +
                        "\n\nFeatures:\n• 24/7 access\n• High-speed internet\n• Special software\n• Tech support\n• Printing";

            case "Health Center":
                return facilityName + ":\n" + basicInfo +
                        "\n\nFeatures:\n• Emergency care\n• Mental health\n• Pharmacy\n• Health education\n• Appointments";

            case "Disability Support Office":
                return facilityName + ":\n" + basicInfo +
                        "\n\nFeatures:\n• Accommodation plans\n• Tech training\n• Advocacy\n• Peer support";

            case "Lecture Hall A":
                return facilityName + ":\n" + basicInfo +
                        "\n\nFeatures:\n• Recording available\n• Wireless presentation\n• 300 capacity\n• AV equipment";

            case "Sports Center":
                return facilityName + ":\n" + basicInfo +
                        "\n\nFeatures:\n• Olympic pool\n• Fitness classes\n• Personal training\n• Sports clubs\n• Lockers";

            default:
                return facilityName + ":\n" + basicInfo;
        }
    }

    public JPanel getPanel() {
        return this;
    }

    public static void main(String[] args) {
        // Set system look and feel for better appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //frame to hold the FacultyPage panel
        JFrame frame = new JFrame("Campus Facilities");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLocationRelativeTo(null);

        // Create and add the FacultyPage panel
        FacultyPage facultyPage = new FacultyPage();
        frame.add(facultyPage);

        // Make the frame visible
        frame.setVisible(true);
    }
}