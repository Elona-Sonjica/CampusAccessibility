package ac.za.postapp.Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Register extends JFrame {
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField studentNumberField;
    private JTextField ageField;
    private JTextField genderField;
    private JTextField emailField;
    private JButton registerButton;
    private JButton backButton;

    public Register() {
        setTitle("Register - Campus Accessibility");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setResizable(true);

        // Create main panel with responsive layout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Name label and field
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        mainPanel.add(nameField, gbc);

        // Surname label and field
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Surname:"), gbc);
        gbc.gridx = 1;
        surnameField = new JTextField(20);
        mainPanel.add(surnameField, gbc);

        // Student Number label and field
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Student Number:"), gbc);
        gbc.gridx = 1;
        studentNumberField = new JTextField(20);
        mainPanel.add(studentNumberField, gbc);

        // Age label and field
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        ageField = new JTextField(20);
        mainPanel.add(ageField, gbc);

        // Gender label and field
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        genderField = new JTextField(20);
        mainPanel.add(genderField, gbc);

        // Email label and field
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        mainPanel.add(emailField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        registerButton = new JButton("Register");
        backButton = new JButton("Back");

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openLogin();
            }
        });

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        // Add components to main panel
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void register() {
        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String studentNumber = studentNumberField.getText().trim();
        String ageStr = ageField.getText().trim();
        String gender = genderField.getText().trim();
        String email = emailField.getText().trim();

        // Validate all fields are filled
        if (name.isEmpty() || surname.isEmpty() || studentNumber.isEmpty() || ageStr.isEmpty() || gender.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate age is a number
        try {
            int age = Integer.parseInt(ageStr);
            if (age <= 0 || age > 120) {
                JOptionPane.showMessageDialog(this, "Please enter a valid age (1-120)", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Age must be a number", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate email format
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Here you would typically save the user data to a database
        JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        openLogin();
    }

    private void openLogin() {
        this.dispose();
        Login login = new Login();
        login.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Register register = new Register();
                register.setVisible(true);
            }
        });
    }
}
