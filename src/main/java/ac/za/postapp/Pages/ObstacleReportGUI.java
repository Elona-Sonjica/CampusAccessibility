package ac.za.postapp.Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ObstacleReportGUI extends JFrame implements ActionListener {
    private JComboBox<String> cmbBuilding, cmbObstacleType, cmbSeverity;
    private JTextField txtReportId, txtIssueTitle, txtRoom, txtDateTime;
    private JTextArea txtDescription;
    private JButton btnClose, btnSubmit, btnClear;

    public ObstacleReportGUI() {
        super("Campus Obstacle Reporting System");
        this.setLayout(new BorderLayout());

        // Main Panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        mainPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        // Header Icon and Title
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(255, 248, 225));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        ImageIcon warningIcon = loadIcon("warning.png", 50, 50);
        JLabel iconLabel = new JLabel(warningIcon);
        headerPanel.add(iconLabel);

        JLabel titleLabel = new JLabel("Campus Obstacle Report");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(230, 126, 34));
        headerPanel.add(titleLabel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(0, 0, 20, 0);
        mainPanel.add(headerPanel, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 8, 8, 8);

        // Row 1: Building and Report ID
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel lblBuilding = new JLabel("Building / Location:");
        lblBuilding.setFont(new Font("Segoe UI", Font.BOLD, 13));
        mainPanel.add(lblBuilding, gbc);

        gbc.gridx = 1;
        cmbBuilding = new JComboBox<>(new String[]{
                "Select Building", "Engineering", "Commerce", "Design", "Science", "Education", "Informatics and Design"});
        cmbBuilding.setPreferredSize(new Dimension(300, 40));
        cmbBuilding.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        styleComboBox(cmbBuilding, new Color(52, 152, 219));

        // Add listener to update Report ID when building is selected
        cmbBuilding.addActionListener(e -> {
            updateReportId();
        });

        mainPanel.add(cmbBuilding, gbc);

        gbc.gridx = 2;
        JLabel lblReportId = new JLabel("Report ID:");
        lblReportId.setFont(new Font("Segoe UI", Font.BOLD, 13));
        mainPanel.add(lblReportId, gbc);

        gbc.gridx = 3;
        JPanel reportIdPanel = new JPanel(new BorderLayout(5, 0));
        reportIdPanel.setBackground(Color.WHITE);

        txtReportId = new JTextField("Auto-generated");
        txtReportId.setEditable(false);
        txtReportId.setPreferredSize(new Dimension(250, 40));
        txtReportId.setBackground(new Color(236, 240, 241));
        txtReportId.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtReportId.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        ImageIcon lockIcon = loadIcon("lock.png", 16, 16);
        JLabel lockLabel = new JLabel(lockIcon);
        lockLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        reportIdPanel.add(txtReportId, BorderLayout.CENTER);
        reportIdPanel.add(lockLabel, BorderLayout.EAST);
        mainPanel.add(reportIdPanel, gbc);

        // Row 2: Issue Title and Description
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblTitle = new JLabel("Issue Title:");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        mainPanel.add(lblTitle, gbc);

        gbc.gridx = 1;
        txtIssueTitle = new JTextField();
        txtIssueTitle.setPreferredSize(new Dimension(300, 40));
        txtIssueTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtIssueTitle.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        mainPanel.add(txtIssueTitle, gbc);

        gbc.gridx = 2;
        JLabel lblDescription = new JLabel("Description:");
        lblDescription.setFont(new Font("Segoe UI", Font.BOLD, 13));
        mainPanel.add(lblDescription, gbc);

        gbc.gridx = 3;
        gbc.gridheight = 2;
        txtDescription = new JTextArea(3, 20);
        txtDescription.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        JScrollPane scrollDescription = new JScrollPane(txtDescription);
        scrollDescription.setPreferredSize(new Dimension(300, 95));
        scrollDescription.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        mainPanel.add(scrollDescription, gbc);

        gbc.gridheight = 1;

        // Row 3: Date & Time
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblDateTime = new JLabel("Date & Time Reported:");
        lblDateTime.setFont(new Font("Segoe UI", Font.BOLD, 13));
        mainPanel.add(lblDateTime, gbc);

        gbc.gridx = 1;
        JPanel dateTimePanel = new JPanel(new BorderLayout(5, 0));
        dateTimePanel.setBackground(Color.WHITE);

        txtDateTime = new JTextField(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        txtDateTime.setEditable(false);
        txtDateTime.setPreferredSize(new Dimension(250, 40));
        txtDateTime.setBackground(new Color(236, 240, 241));
        txtDateTime.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDateTime.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        ImageIcon calendarIcon = loadIcon("calendar.png", 16, 16);
        JLabel calendarLabel = new JLabel(calendarIcon);
        calendarLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        dateTimePanel.add(txtDateTime, BorderLayout.CENTER);
        dateTimePanel.add(calendarLabel, BorderLayout.EAST);
        mainPanel.add(dateTimePanel, gbc);

        // Row 4: Room and Obstacle Type
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel lblRoom = new JLabel("Room / Specific Location:");
        lblRoom.setFont(new Font("Segoe UI", Font.BOLD, 13));
        mainPanel.add(lblRoom, gbc);

        gbc.gridx = 1;
        txtRoom = new JTextField();
        txtRoom.setPreferredSize(new Dimension(300, 40));
        txtRoom.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtRoom.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        mainPanel.add(txtRoom, gbc);

        gbc.gridx = 2;
        JLabel lblObstacleType = new JLabel("Obstacle Type:");
        lblObstacleType.setFont(new Font("Segoe UI", Font.BOLD, 13));
        mainPanel.add(lblObstacleType, gbc);

        gbc.gridx = 3;
        cmbObstacleType = new JComboBox<>(new String[]{
                "Select Obstacle Type",
                "Blocked Pathway",
                "Broken Elevator/Lift",
                "Construction Area",
                "Unsafe Conditions (floods/debris)",
                "Broken Ramp",
                "Inaccessible Entrance",
                "Out of Order Restroom",
                "Missing Signage",
                "Other"
        });
        cmbObstacleType.setPreferredSize(new Dimension(300, 40));
        cmbObstacleType.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        styleComboBox(cmbObstacleType, new Color(231, 76, 60));
        mainPanel.add(cmbObstacleType, gbc);

        // Row 5: Severity Level
        gbc.gridx = 2;
        gbc.gridy = 5;
        JLabel lblSeverity = new JLabel("Severity Level:");
        lblSeverity.setFont(new Font("Segoe UI", Font.BOLD, 13));
        mainPanel.add(lblSeverity, gbc);

        gbc.gridx = 3;
        cmbSeverity = new JComboBox<>(new String[]{"Select Severity", "Low", "Medium", "High"});
        cmbSeverity.setPreferredSize(new Dimension(300, 40));
        cmbSeverity.setFont(new Font("Segoe UI", Font.BOLD, 14));
        styleComboBox(cmbSeverity, new Color(46, 204, 113));

        // Add listener to change color based on selection
        cmbSeverity.addActionListener(e -> {
            String selected = (String) cmbSeverity.getSelectedItem();
            if ("Low".equals(selected)) {
                styleComboBox(cmbSeverity, new Color(46, 204, 113));
            } else if ("Medium".equals(selected)) {
                styleComboBox(cmbSeverity, new Color(243, 156, 18));
            } else if ("High".equals(selected)) {
                styleComboBox(cmbSeverity, new Color(231, 76, 60));
            } else {
                styleComboBox(cmbSeverity, new Color(46, 204, 113));
            }
        });

        mainPanel.add(cmbSeverity, gbc);

        this.add(mainPanel, BorderLayout.CENTER);

        // Bottom buttons
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        pnlBottom.setBackground(Color.WHITE);

        ImageIcon submitIcon = loadIcon("submit.png", 16, 16);
        btnSubmit = new JButton("Submit Report", submitIcon);
        styleButton(btnSubmit, new Color(39, 174, 96));

        ImageIcon clearIcon = loadIcon("clear.png", 16, 16);
        btnClear = new JButton("Clear", clearIcon);
        styleButton(btnClear, new Color(243, 156, 18));

        ImageIcon closeIcon = loadIcon("close.png", 16, 16);
        btnClose = new JButton("Close", closeIcon);
        styleButton(btnClose, new Color(127, 140, 141));

        pnlBottom.add(btnSubmit);
        pnlBottom.add(btnClear);
        pnlBottom.add(btnClose);

        this.add(pnlBottom, BorderLayout.SOUTH);

        btnClose.addActionListener(this);
        btnSubmit.addActionListener(this);
        btnClear.addActionListener(this);

        this.setSize(1400, 800);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    private ImageIcon loadIcon(String filename, int width, int height) {
        try {
            // Try loading from /icons/ in resources
            java.net.URL imgURL = getClass().getResource("/icons/" + filename);

            // If not found, try from classpath root
            if (imgURL == null) {
                imgURL = getClass().getClassLoader().getResource("icons/" + filename);
            }

            // Try other possible locations
            if (imgURL == null) {
                imgURL = getClass().getResource("/ac/za/postapp/icons/" + filename);
            }

            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            } else {
                System.err.println("Icon not found: " + filename);
                System.err.println("Make sure icons are in: src/main/resources/icons/");
                return createFallbackIcon(width, height, filename);
            }
        } catch (Exception e) {
            System.err.println("Error loading icon: " + filename);
            e.printStackTrace();
            return createFallbackIcon(width, height, filename);
        }
    }

    private ImageIcon createFallbackIcon(int width, int height, String filename) {
        // Create a simple colored square as fallback
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(
                width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        // Different colors for different icons
        if (filename.contains("warning")) {
            g2d.setColor(new Color(243, 156, 18));
        } else if (filename.contains("lock")) {
            g2d.setColor(new Color(230, 126, 34));
        } else if (filename.contains("calendar")) {
            g2d.setColor(new Color(52, 152, 219));
        } else if (filename.contains("submit")) {
            g2d.setColor(new Color(39, 174, 96));
        } else if (filename.contains("clear")) {
            g2d.setColor(new Color(243, 156, 18));
        } else if (filename.contains("close")) {
            g2d.setColor(new Color(127, 140, 141));
        } else {
            g2d.setColor(new Color(149, 165, 166));
        }

        g2d.fillRoundRect(0, 0, width, height, 5, 5);
        g2d.dispose();

        return new ImageIcon(img);
    }

    private void styleComboBox(JComboBox<String> combo, Color color) {
        combo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setPreferredSize(new Dimension(150, 40));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnClose){
            this.dispose();
        } else if(e.getSource() == btnSubmit){
            submitReport();
        } else if(e.getSource() == btnClear){
            clearForm();
        }
    }

    private void submitReport() {
        // Validation
        if(cmbBuilding.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a building!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(txtIssueTitle.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an issue title!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(txtDescription.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a description!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(txtRoom.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter room/location!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(cmbObstacleType.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select an obstacle type!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(cmbSeverity.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a severity level!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String building = cmbBuilding.getSelectedItem().toString();
        String title = txtIssueTitle.getText().trim();
        String description = txtDescription.getText().trim();
        String room = txtRoom.getText().trim();
        String obstacleType = cmbObstacleType.getSelectedItem().toString();
        String severity = cmbSeverity.getSelectedItem().toString();
        String reportId = txtReportId.getText().trim();
        String dateTime = txtDateTime.getText().trim();

        // Here you would save to database
        // Uncomment when your database is ready:
        /*
        ObstacleReport report = new ObstacleReport(
            reportId, building, title, description, room,
            obstacleType, severity, dateTime
        );
        boolean success = ObstacleReportDAO.save(report);
        if (success) {
            showSuccessMessage(building, title, obstacleType, severity);
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this,
                "Error saving report to database. Please try again.",
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
        */

        // Temporary success message (remove when database is implemented)
        showSuccessMessage(building, title, obstacleType, severity);
        clearForm();
    }

    private void showSuccessMessage(String building, String title, String obstacleType, String severity) {
        JOptionPane.showMessageDialog(this,
                "Obstacle report submitted successfully!\n\n" +
                        "Report ID: " + txtReportId.getText() + "\n" +
                        "Building: " + building + "\n" +
                        "Issue: " + title + "\n" +
                        "Obstacle Type: " + obstacleType + "\n" +
                        "Severity: " + severity + "\n\n" +
                        "The facilities team will be notified and address this issue promptly.",
                "Report Submitted",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearForm() {
        cmbBuilding.setSelectedIndex(0);
        txtIssueTitle.setText("");
        txtDescription.setText("");
        txtRoom.setText("");
        cmbObstacleType.setSelectedIndex(0);
        cmbSeverity.setSelectedIndex(0);
        styleComboBox(cmbSeverity, new Color(46, 204, 113)); // Reset to default green
        txtDateTime.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        txtReportId.setText("Auto-generated");
    }

    private void updateReportId() {
        String selectedBuilding = (String) cmbBuilding.getSelectedItem();
        String buildingId = "";

        // Map each building to its unique ID
        switch (selectedBuilding) {
            case "Engineering":
                buildingId = "ENG-1001";
                break;
            case "Commerce":
                buildingId = "COM-2001";
                break;
            case "Design":
                buildingId = "DES-3001";
                break;
            case "Science":
                buildingId = "SCI-4001";
                break;
            case "Education":
                buildingId = "EDU-5001";
                break;
            case "Informatics and Design":
                buildingId = "IND-6001";
                break;
            default:
                buildingId = "Auto-generated";
                break;
        }

        // Generate a unique report number with 4-digit random number
        if (!buildingId.equals("Auto-generated")) {
            int randomNum = 1000 + (int)(Math.random() * 9000); // Generates 4-digit number (1000-9999)
            txtReportId.setText(buildingId + "-" + randomNum);
        } else {
            txtReportId.setText("Auto-generated");
        }
    }

    public static void main(String[] args) {
        // Set system look and feel for better appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new ObstacleReportGUI();
        });
    }
}
