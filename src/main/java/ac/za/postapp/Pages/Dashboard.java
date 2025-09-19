package ac.za.postapp.Pages;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {
    private JLabel nameLabel;
    private JLabel surnameLabel;
    private JLabel studentNumberLabel;
    private JLabel ageLabel;
    private JLabel genderLabel;
    private JLabel emailLabel;
    private JButton logoutButton;

    private JComboBox<String> facultyComboBox;
    private JTextField courseField;
    private JTextField destinationField;

    public Dashboard() {
        setTitle("Dashboard - CPUT Campus D6");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // ===== Scrollable Main Panel =====
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(new




                Color(245, 247, 250));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font valueFont = new Font("Segoe UI", Font.PLAIN, 14);

        // ==== Title ====
        JLabel title = new JLabel("📋 Student Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(52, 73, 94));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        contentPanel.add(title, gbc);
        gbc.gridwidth = 1;

        int row = 1;
        // ==== User Info Labels ====
        gbc.gridx = 0; gbc.gridy = row; contentPanel.add(createStyledLabel("Name:", labelFont), gbc);
        gbc.gridx = 1; nameLabel = createValueLabel(valueFont); contentPanel.add(nameLabel, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; contentPanel.add(createStyledLabel("Surname:", labelFont), gbc);
        gbc.gridx = 1; surnameLabel = createValueLabel(valueFont); contentPanel.add(surnameLabel, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; contentPanel.add(createStyledLabel("Student Number:", labelFont), gbc);
        gbc.gridx = 1; studentNumberLabel = createValueLabel(valueFont); contentPanel.add(studentNumberLabel, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; contentPanel.add(createStyledLabel("Age:", labelFont), gbc);
        gbc.gridx = 1; ageLabel = createValueLabel(valueFont); contentPanel.add(ageLabel, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; contentPanel.add(createStyledLabel("Gender:", labelFont), gbc);
        gbc.gridx = 1; genderLabel = createValueLabel(valueFont); contentPanel.add(genderLabel, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; contentPanel.add(createStyledLabel("Email:", labelFont), gbc);
        gbc.gridx = 1; emailLabel = createValueLabel(valueFont); contentPanel.add(emailLabel, gbc);

        // ==== Faculty Dropdown ====
        row++;
        gbc.gridx = 0; gbc.gridy = row; contentPanel.add(createStyledLabel("Faculty:", labelFont), gbc);
        gbc.gridx = 1;
        String[] faculties = {"Engineering", "Health Sciences", "Business", "Education", "Applied Sciences"};
        facultyComboBox = new JComboBox<>(faculties);
        contentPanel.add(facultyComboBox, gbc);

        // ==== Course Field ====
        row++;
        gbc.gridx = 0; gbc.gridy = row; contentPanel.add(createStyledLabel("Course:", labelFont), gbc);
        gbc.gridx = 1;
        courseField = new JTextField();
        courseField.setText("Enter your course");
        contentPanel.add(courseField, gbc);

        // ==== Destination Field ====
        row++;
        gbc.gridx = 0; gbc.gridy = row; contentPanel.add(createStyledLabel("Destination:", labelFont), gbc);
        gbc.gridx = 1;
        destinationField = new JTextField();
        destinationField.setText("Enter your destination");
        contentPanel.add(destinationField, gbc);

        // ==== Top Menu Buttons (excluding Map and Home) ====
        JPanel topMenuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topMenuPanel.setBackground(new Color(245, 247, 250));
        String[] topOptions = {"Schedule", "Notifications", "Settings"};
        for (String option : topOptions) {
            JButton btn = createMenuButton(option);
            topMenuPanel.add(btn);
        }

        // ==== Bottom Menu Buttons (Map and Home) ====
        JPanel bottomMenuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomMenuPanel.setBackground(new Color(245, 247, 250));
        String[] bottomOptions = {"Home", "Map"};
        for (String option : bottomOptions) {
            JButton btn = new JButton(option);
            btn.setFocusPainted(false);
            btn.setBackground(new Color(52, 73, 94));
            btn.setForeground(Color.WHITE);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            if (option.equals("Map")) {
                btn.addActionListener(e -> {
                    // Open CampusMap window
                    CampusMap mapWindow = new CampusMap();
                    mapWindow.setVisible(true);
                });
            } else {
                btn.addActionListener(e -> JOptionPane.showMessageDialog(this, option + " clicked!"));
            }

            bottomMenuPanel.add(btn);
        }

        // ==== Logout Button ====
        JPanel logoutPanel = new JPanel();
        logoutPanel.setBackground(new Color(245, 247, 250));
        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setPreferredSize(new Dimension(120, 40));
        logoutButton.addActionListener(e -> logout());
        logoutPanel.add(logoutButton);

        // ==== Add Panels to Frame ====
        add(topMenuPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(bottomMenuPanel, BorderLayout.NORTH);
        bottomPanel.add(logoutPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(52, 73, 94));
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> JOptionPane.showMessageDialog(this, text + " clicked!"));
        return btn;
    }

    private JLabel createStyledLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(new Color(52, 73, 94));
        return label;
    }

    private JLabel createValueLabel(Font font) {
        JLabel label = new JLabel("N/A");
        label.setFont(font);
        label.setForeground(new Color(33, 37, 41));
        return label;
    }

    public void setUserData(User user) {
        nameLabel.setText(user.getName());
        surnameLabel.setText(user.getSurname());
        studentNumberLabel.setText(user.getStudentNumber());
        ageLabel.setText(String.valueOf(user.getAge()));
        genderLabel.setText(user.getGender());
        emailLabel.setText(user.getEmail());
    }

    private void logout() {
        this.dispose();
        Login login = new Login();
        login.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Dashboard dashboard = new Dashboard();
            dashboard.setVisible(true);
        });
    }
}