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
        setSize(450, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // ====== Main Panel ======
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        mainPanel.setBackground(new Color(245, 247, 250));

        // ====== Title ======
        JLabel title = new JLabel("📝 Campus Registration", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(52, 73, 94));
        mainPanel.add(title, BorderLayout.NORTH);

        // ====== Form Panel ======
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);

        // ====== Name ======
        addLabelAndField(formPanel, gbc, 0, "Name:", nameField = new JTextField(20));

        // ====== Surname ======
        addLabelAndField(formPanel, gbc, 1, "Surname:", surnameField = new JTextField(20));

        // ====== Student Number ======
        addLabelAndField(formPanel, gbc, 2, "Student Number:", studentNumberField = new JTextField(20));

        // ====== Age ======
        addLabelAndField(formPanel, gbc, 3, "Age:", ageField = new JTextField(20));

        // ====== Gender ======
        addLabelAndField(formPanel, gbc, 4, "Gender:", genderField = new JTextField(20));

        // ====== Email ======
        addLabelAndField(formPanel, gbc, 5, "Email:", emailField = new JTextField(20));

        // ====== Buttons ======
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(245, 247, 250));

        registerButton = new JButton("Register");
        styleButton(registerButton, new Color(40, 167, 69)); // Green

        backButton = new JButton("Back");
        styleButton(backButton, new Color(108, 117, 125)); // Grey

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

        // ====== Add panels to frame ======
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    // Helper: Adds label and styled field to form panel
    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        label.setForeground(new Color(52, 73, 94));

        gbc.gridx = 0; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(label, gbc);

        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        gbc.gridx = 1; gbc.gridy = row;
        panel.add(field, gbc);
    }

    // Helper: Styles buttons with hover effect
    private void styleButton(JButton button, Color baseColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(120, 40));
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor);
            }
        });
    }

    private void register() {
        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String studentNumber = studentNumberField.getText().trim();
        String ageStr = ageField.getText().trim();
        String gender = genderField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || surname.isEmpty() || studentNumber.isEmpty() || ageStr.isEmpty() || gender.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

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

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        openLogin();
    }

    private void openLogin() {
        this.dispose();
        Login login = new Login();
        login.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Register register = new Register();
            register.setVisible(true);
        });
    }
}
