package ac.za.postapp.Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard extends JFrame {
    private JLabel nameLabel;
    private JLabel surnameLabel;
    private JLabel studentNumberLabel;
    private JLabel ageLabel;
    private JLabel genderLabel;
    private JLabel emailLabel;
    private JButton logoutButton;

    public Dashboard() {
        setTitle("Dashboard - Campus Accessibility");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        // ==== Main Background ====
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(new Color(245, 247, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font valueFont = new Font("Segoe UI", Font.PLAIN, 14);

        // ==== Title ====
        JLabel title = new JLabel("📋 Student Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(52, 73, 94));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(title, gbc);
        gbc.gridwidth = 1;

        // ==== Info Rows ====
        int row = 1;
        gbc.gridx = 0; gbc.gridy = row; mainPanel.add(createStyledLabel("Name:", labelFont), gbc);
        gbc.gridx = 1; nameLabel = createValueLabel(valueFont); mainPanel.add(nameLabel, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; mainPanel.add(createStyledLabel("Surname:", labelFont), gbc);
        gbc.gridx = 1; surnameLabel = createValueLabel(valueFont); mainPanel.add(surnameLabel, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; mainPanel.add(createStyledLabel("Student Number:", labelFont), gbc);
        gbc.gridx = 1; studentNumberLabel = createValueLabel(valueFont); mainPanel.add(studentNumberLabel, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; mainPanel.add(createStyledLabel("Age:", labelFont), gbc);
        gbc.gridx = 1; ageLabel = createValueLabel(valueFont); mainPanel.add(ageLabel, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; mainPanel.add(createStyledLabel("Gender:", labelFont), gbc);
        gbc.gridx = 1; genderLabel = createValueLabel(valueFont); mainPanel.add(genderLabel, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; mainPanel.add(createStyledLabel("Email:", labelFont), gbc);
        gbc.gridx = 1; emailLabel = createValueLabel(valueFont); mainPanel.add(emailLabel, gbc);

        // ==== Logout Button ====
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 247, 250));

        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setPreferredSize(new Dimension(120, 40));
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        // Hover effect
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(200, 35, 51));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(220, 53, 69));
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        buttonPanel.add(logoutButton);

        // ==== Layout ====
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
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
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Dashboard dashboard = new Dashboard();
                dashboard.setVisible(true);
            }
        });
    }
}
