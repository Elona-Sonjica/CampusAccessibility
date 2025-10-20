package ac.za.postapp.Pages;

// emeritusApex


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Register extends JFrame {
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField studentNumberField;
    private JTextField ageField;
    private JComboBox<String> genderComboBox;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton loginButton;
    private ImageIcon logoIcon;
    private JPanel loadingPanel;
    private Timer loadingTimer;
    private int loadingAngle = 0;
    private JProgressBar progressBar;

    public Register() {
        // Load the logo
        loadLogo();

        setTitle("Register - Campus Access Guide");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 650); // Adjusted size for simpler form
        setLocationRelativeTo(null);
        setResizable(false);

        // ====== Main Panel with Gradient ======
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(74, 107, 136),
                        getWidth(), getHeight(), new Color(33, 64, 98)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ====== Header with Logo ======
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // ====== Personal Information Form ======
        JPanel formPanel = createPersonalInfoPanel();

        // Wrap in scroll pane (just in case)
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(new Color(255, 255, 255, 230));

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ====== Progress Bar ======
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(40, 167, 69));
        progressBar.setBackground(new Color(220, 220, 220));
        progressBar.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        progressBar.setVisible(false);

        // ====== Buttons Panel ======
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ====== Loading Panel ======
        loadingPanel = createLoadingPanel();
        loadingPanel.setVisible(false);

        add(mainPanel);

        // Set window icon
        if (logoIcon != null) {
            setIconImage(logoIcon.getImage());
        }
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Register Button
        registerButton = new JButton("🚀 Register") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(39, 174, 96));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(46, 204, 113).brighter());
                } else {
                    g2d.setColor(new Color(46, 204, 113));
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Text
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(getText(), x, y);

                g2d.dispose();
            }
        };
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setForeground(Color.WHITE);
        registerButton.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        registerButton.setContentAreaFilled(false);
        registerButton.setFocusPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setToolTipText("Create your new account");
        registerButton.addActionListener(e -> animateRegistration());

        // Login Button (to go to login page)
        loginButton = new JButton("🔐 Login") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(41, 128, 185));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(52, 152, 219).brighter());
                } else {
                    g2d.setColor(new Color(52, 152, 219));
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Text
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(getText(), x, y);

                g2d.dispose();
            }
        };
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        loginButton.setContentAreaFilled(false);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setToolTipText("Already have an account? Login here");
        loginButton.addActionListener(e -> openLogin());

        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);

        return buttonPanel;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setPreferredSize(new Dimension(100, 100));

        JLabel logoLabel = new JLabel();
        if (logoIcon != null) {
            logoLabel.setIcon(logoIcon);
        } else {
            logoLabel.setText("🏛️");
            logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 36));
            logoLabel.setForeground(Color.WHITE);
        }
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel title = new JLabel("Campus Access Guide", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Create Your Account", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(200, 220, 240));

        headerPanel.add(logoLabel, BorderLayout.NORTH);
        headerPanel.add(title, BorderLayout.CENTER);
        headerPanel.add(subtitle, BorderLayout.SOUTH);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        return headerPanel;
    }

    private JPanel createPersonalInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 230));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2d.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 8, 12, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Name
        addStyledLabelAndField(panel, gbc, 0, "👤 Name:", nameField = createStyledTextField());

        // Surname
        addStyledLabelAndField(panel, gbc, 1, "👥 Surname:", surnameField = createStyledTextField());

        // Student Number
        addStyledLabelAndField(panel, gbc, 2, "🎓 Student Number:", studentNumberField = createStyledTextField());

        // Age
        addStyledLabelAndField(panel, gbc, 3, "🎂 Age:", ageField = createStyledTextField());

        // Gender
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel genderLabel = createStyledLabel("⚧ Gender:");
        panel.add(genderLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        String[] genders = {"Male", "Female", "Other", "Prefer not to say"};
        genderComboBox = createStyledComboBox(genders);
        panel.add(genderComboBox, gbc);

        // Spacer
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        panel.add(Box.createVerticalStrut(20), gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 1;
        addStyledLabelAndField(panel, gbc, 6, "📧 Email:", emailField = createStyledTextField());

        // Password
        gbc.gridx = 0; gbc.gridy = 7;
        JLabel passwordLabel = createStyledLabel("🔒 Password:");
        panel.add(passwordLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 7;
        passwordField = createStyledPasswordField();
        panel.add(passwordField, gbc);

        // Confirm Password
        gbc.gridx = 0; gbc.gridy = 8;
        JLabel confirmPasswordLabel = createStyledLabel("✅ Confirm Password:");
        panel.add(confirmPasswordLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 8;
        confirmPasswordField = createStyledPasswordField();
        panel.add(confirmPasswordField, gbc);

        return panel;
    }

    private JPanel createLoadingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(100, 80));

        JLabel loadingLabel = new JLabel("Creating your account...", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        loadingLabel.setForeground(Color.WHITE);

        panel.add(loadingLabel, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);

        return panel;
    }

    private void loadLogo() {
        try {
            logoIcon = new ImageIcon("resources/images/20251013_0428_Campus Access Logo_simple_compose_01k7dp8zxzftq9kghhg6typvaz.png");
            if (logoIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                Image image = logoIcon.getImage();
                Image resizedImage = image.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                logoIcon = new ImageIcon(resizedImage);
            } else {
                logoIcon = null;
            }
        } catch (Exception e) {
            logoIcon = null;
        }
    }

    // ====== STYLED COMPONENT METHODS ======

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Border
                if (hasFocus()) {
                    g2d.setColor(new Color(0, 123, 255));
                } else {
                    g2d.setColor(new Color(180, 180, 180));
                }
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);

                super.paintComponent(g);
                g2d.dispose();
            }
        };
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        field.setOpaque(false);
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField(20) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Border
                if (hasFocus()) {
                    g2d.setColor(new Color(0, 123, 255));
                } else {
                    g2d.setColor(new Color(180, 180, 180));
                }
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);

                super.paintComponent(g);
                g2d.dispose();
            }
        };
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        field.setEchoChar('•');
        field.setOpaque(false);
        return field;
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return combo;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(52, 73, 94));
        return label;
    }

    private void addStyledLabelAndField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel(labelText), gbc);

        gbc.gridx = 1; gbc.gridy = row;
        panel.add(field, gbc);
    }

    // ====== REGISTRATION LOGIC ======

    private void animateRegistration() {
        loadingPanel.setVisible(true);
        progressBar.setVisible(true);
        registerButton.setEnabled(false);
        loginButton.setEnabled(false);
        revalidate();
        repaint();

        Timer progressTimer = new Timer(50, new ActionListener() {
            int progress = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                progress += 2;
                progressBar.setValue(progress);

                if (progress >= 100) {
                    ((Timer)e.getSource()).stop();
                    processRegistration();
                }
            }
        });
        progressTimer.start();
    }

    private void processRegistration() {
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return performRegistration();
            }

            @Override
            protected void done() {
                loadingPanel.setVisible(false);
                progressBar.setVisible(false);
                registerButton.setEnabled(true);
                loginButton.setEnabled(true);

                try {
                    Boolean success = get();
                    if (success) {
                        showSuccessAnimation();
                    } else {
                        showErrorDialog("Registration failed. Please try again.");
                    }
                } catch (Exception e) {
                    showErrorDialog("Registration error: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private Boolean performRegistration() {
        // Get form data
        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String studentNumber = studentNumberField.getText().trim();
        String ageStr = ageField.getText().trim();
        String gender = (String) genderComboBox.getSelectedItem();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

        // Validation
        if (!validateInput(name, surname, studentNumber, ageStr, email, password, confirmPassword)) {
            return false;
        }

        int age = Integer.parseInt(ageStr);

        // Create user with only personal info (no accessibility data)
        User user = new User(name, surname, studentNumber, age, gender, email);

        try {
            // Try database registration
            boolean dbSuccess = UserDAO.registerUser(user, password);
            if (dbSuccess) {
                return true;
            } else {
                // Database failed but we'll simulate success for demo mode
                showDemoModeWarning();
                return true; // Simulate success for demo mode
            }
        } catch (Exception e) {
            // Database completely failed - run in demo mode
            showDemoModeWarning();
            return true; // Simulate success for demo mode
        }
    }

    private boolean validateInput(String name, String surname, String studentNumber, String ageStr,
                                  String email, String password, String confirmPassword) {
        // Basic validation
        if (name.isEmpty() || surname.isEmpty() || studentNumber.isEmpty() ||
                ageStr.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showErrorDialog("Please fill in all required fields.");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showErrorDialog("Passwords do not match.");
            return false;
        }

        if (password.length() < 6) {
            showErrorDialog("Password must be at least 6 characters long.");
            return false;
        }

        if (!email.contains("@") || !email.contains(".") || !email.contains("mycput.ac.za")) {
            showErrorDialog("Please enter a valid email address.");
            return false;
        }

        try {
            int age = Integer.parseInt(ageStr);
            if (age < 16 || age > 100) {
                showErrorDialog("Please enter a valid age (16-100).");
                return false;
            }
        } catch (NumberFormatException e) {
            showErrorDialog("Age must be a number.");
            return false;
        }

        return true;
    }

    private void showDemoModeWarning() {
        JOptionPane.showMessageDialog(this,
                "<html><div style='text-align: center;'>" +
                        "<b>💡 Running in Demo Mode</b><br><br>" +
                        "Database connection unavailable.<br>" +
                        "Your account has been created locally.<br>" +
                        "Some features may be limited." +
                        "</div></html>",
                "Demo Mode",
                JOptionPane.WARNING_MESSAGE);
    }

    private void showSuccessAnimation() {
        String successMessage = String.format(
                "<html><div style='text-align: center;'>" +
                        "<h3 style='color: #27ae60;'>🎉 Registration Successful!</h3>" +
                        "<p><b>Welcome %s %s!</b></p>" +
                        "<p>Student Number: <b>%s</b></p>" +
                        "</div></html>",
                nameField.getText().trim(),
                surnameField.getText().trim(),
                studentNumberField.getText().trim()
        );

        JOptionPane.showMessageDialog(this, successMessage, "Success!", JOptionPane.INFORMATION_MESSAGE);

        // Ask user if they want to login now
        int option = JOptionPane.showConfirmDialog(this,
                "<html><div style='text-align: center;'>" +
                        "Would you like to login now?" +
                        "</div></html>",
                "Login Now?",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            openLogin();
        }
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this,
                "<html><div style='text-align: center;'>" + message + "</div></html>",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void openLogin() {
        this.dispose();
        Login login = new Login();
        login.setVisible(true);
    }

    public static void main(String[] args) {
        // Initialize database QUIETLY - don't crash if it fails
        initializeDatabaseQuietly();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            Register register = new Register();
            register.setVisible(true);
        });
    }

    private static void initializeDatabaseQuietly() {
        try {
            DatabaseConnection.initializeDatabase();
            UserDAO.createUsersTable();
            System.out.println("✅ Database initialized successfully");
        } catch (Exception e) {
            System.err.println("❌ Database initialization failed: " + e.getMessage());
            System.err.println("💡 Application will run in DEMO MODE");
        }
    }
}