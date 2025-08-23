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
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(true);

        // Create main panel with responsive layoutthtt
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // User information labels
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameLabel = new JLabel("N/A");
        mainPanel.add(nameLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Surname:"), gbc);
        gbc.gridx = 1;
        surnameLabel = new JLabel("N/A");
        mainPanel.add(surnameLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Student Number:"), gbc);
        gbc.gridx = 1;
        studentNumberLabel = new JLabel("N/A");
        mainPanel.add(studentNumberLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        ageLabel = new JLabel("N/A");
        mainPanel.add(ageLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        genderLabel = new JLabel("N/A");
        mainPanel.add(genderLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailLabel = new JLabel("N/A");
        mainPanel.add(emailLabel, gbc);

        // Logout button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        logoutButton = new JButton("Logout");

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        buttonPanel.add(logoutButton);

        // Add components to main panel
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
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
