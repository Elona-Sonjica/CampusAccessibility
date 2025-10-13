package ac.za.postapp.Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class Login extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JCheckBox showPasswordCheckBox;
    private JPanel loadingPanel;
    private Timer loadingTimer;
    private int loadingAngle = 0;
    private ImageIcon logoIcon;

    public Login() {
        // Load the logo image
        loadLogo();

        setTitle("Campus Access Guide - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(false);

        // ==== Main Background with Gradient ====
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
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // ==== Logo/Header Section ====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        // Logo display - Using your actual logo file
        JLabel logoLabel = new JLabel();
        if (logoIcon != null) {
            logoLabel.setIcon(logoIcon);
        } else {
            // Fallback to text if logo fails to load
            logoLabel.setText("🏛️");
            logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
            logoLabel.setForeground(Color.WHITE);
        }
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setVerticalAlignment(SwingConstants.CENTER);

        JLabel title = new JLabel("Campus Access Guide", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Accessible Navigation for All", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(200, 220, 240));

        headerPanel.add(logoLabel, BorderLayout.NORTH);
        headerPanel.add(title, BorderLayout.CENTER);
        headerPanel.add(subtitle, BorderLayout.SOUTH);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // ==== Form Panel with Glass Effect ====
        JPanel formPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Semi-transparent background
                g2d.setColor(new Color(255, 255, 255, 230));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                // Border
                g2d.setColor(new Color(255, 255, 255, 150));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 25, 25);

                g2d.dispose();
            }
        };
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Color labelColor = new Color(52, 73, 94);

        // ==== Email Field ====
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel emailLabel = new JLabel("📧 Email Address:");
        emailLabel.setFont(labelFont);
        emailLabel.setForeground(labelColor);
        formPanel.add(emailLabel, gbc);

        gbc.gridx = 1;
        emailField = createStyledTextField();
        emailField.setToolTipText("Enter your registered email address");
        formPanel.add(emailField, gbc);

        // ==== Password Field ====
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel passwordLabel = new JLabel("🔒 Password:");
        passwordLabel.setFont(labelFont);
        passwordLabel.setForeground(labelColor);
        formPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField = createStyledPasswordField();
        passwordField.setToolTipText("Enter your password");
        formPanel.add(passwordField, gbc);

        // ==== Show Password Checkbox ====
        gbc.gridx = 1; gbc.gridy = 2;
        showPasswordCheckBox = new JCheckBox("👁️ Show Password");
        showPasswordCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showPasswordCheckBox.setBackground(new Color(255, 255, 255, 0));
        showPasswordCheckBox.setForeground(labelColor);
        showPasswordCheckBox.setFocusPainted(false);
        showPasswordCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPasswordCheckBox.isSelected()) {
                    passwordField.setEchoChar((char) 0);
                } else {
                    passwordField.setEchoChar('•');
                }
            }
        });
        formPanel.add(showPasswordCheckBox, gbc);

        // ==== Buttons Panel ====
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        loginButton = createModernButton("🚀 Login", new Color(40, 167, 69));
        registerButton = createModernButton("📝 Register", new Color(0, 123, 255));

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegister();
            }
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // ==== Loading Panel (initially hidden) ====
        loadingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                int radius = 20;

                // Animated loading circle
                g2d.setColor(new Color(40, 167, 69));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawArc(centerX - radius, centerY - radius, radius * 2, radius * 2, loadingAngle, 120);

                g2d.setColor(new Color(0, 123, 255));
                g2d.drawArc(centerX - radius, centerY - radius, radius * 2, radius * 2, loadingAngle + 180, 120);
            }
        };
        loadingPanel.setOpaque(false);
        loadingPanel.setPreferredSize(new Dimension(100, 100));
        loadingPanel.setVisible(false);

        // Loading animation timer
        loadingTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadingAngle = (loadingAngle + 10) % 360;
                loadingPanel.repaint();
            }
        });

        // ==== Layout Assembly ====
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setOpaque(false);
        centerPanel.add(formPanel, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(loadingPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Set enter key to trigger login
        getRootPane().setDefaultButton(loginButton);

        // Set window icon
        if (logoIcon != null) {
            setIconImage(logoIcon.getImage());
        }
    }

    private void loadLogo() {
        try {
            // Method 1: Load from project resources folder
            logoIcon = new ImageIcon("resources/20251013_0428_Campus Access Logo_simple_compose_01k7dp8zxzftq9kghhg6typvaz.png");

            // If Method 1 fails, try Method 2: Load from absolute path
            if (logoIcon.getImageLoadStatus() != MediaTracker.COMPLETE) {
                logoIcon = new ImageIcon("C:/path/to/your/logo/20251013_0428_Campus Access Logo_simple_compose_01k7dp8zxzftq9kghhg6typvaz.png");
            }

            // If Method 2 fails, try Method 3: Load from classpath
            if (logoIcon.getImageLoadStatus() != MediaTracker.COMPLETE) {
                java.net.URL imageURL = getClass().getResource("/images/20251013_0428_Campus Access Logo_simple_compose_01k7dp8zxzftq9kghhg6typvaz.png");
                if (imageURL != null) {
                    logoIcon = new ImageIcon(imageURL);
                }
            }

            // Resize logo if loaded successfully
            if (logoIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                Image image = logoIcon.getImage();
                Image resizedImage = image.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                logoIcon = new ImageIcon(resizedImage);
                System.out.println("✅ Logo loaded successfully!");
            } else {
                System.out.println("❌ Logo not found. Using fallback emoji.");
                logoIcon = null;
            }
        } catch (Exception e) {
            System.err.println("❌ Error loading logo: " + e.getMessage());
            logoIcon = null;
        }
    }

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
                    g2d.setColor(new Color(200, 200, 200));
                }
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);

                super.paintComponent(g);
                g2d.dispose();
            }
        };
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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
                    g2d.setColor(new Color(200, 200, 200));
                }
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);

                super.paintComponent(g);
                g2d.dispose();
            }
        };
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        field.setEchoChar('•');
        field.setOpaque(false);
        return field;
    }

    private JButton createModernButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(baseColor.darker().darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(baseColor.brighter());
                } else {
                    g2d.setColor(baseColor);
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

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private void login() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            showErrorDialog("Please fill in all fields");
            return;
        }

        if (!isValidEmail(email)) {
            showErrorDialog("Please enter a valid email address");
            return;
        }

        // Show loading animation
        showLoading(true);

        // Use SwingWorker for non-blocking database operations
        SwingWorker<User, Void> worker = new SwingWorker<User, Void>() {
            @Override
            protected User doInBackground() throws Exception {
                // Try database authentication
                try {
                    return UserDAO.loginUser(email, password);
                } catch (Exception e) {
                    System.err.println("Database login error: " + e.getMessage());
                    return null;
                }
            }

            @Override
            protected void done() {
                showLoading(false);
                try {
                    User user = get();

                    if (user != null) {
                        showSuccessDialog("Login successful! Welcome back, " + user.getName() + "!");
                        openDashboard(user);
                    } else {
                        // Database login failed - offer demo mode
                        offerDemoMode(email);
                    }
                } catch (Exception e) {
                    showLoading(false);
                    offerDemoMode(email);
                }
            }
        };

        worker.execute();
    }

    private void offerDemoMode(String email) {
        int result = JOptionPane.showConfirmDialog(this,
                "<html><div style='text-align: center; width: 300px;'>"
                        + "<b>Database Connection Issue</b><br><br>"
                        + "Unable to connect to the database server.<br>"
                        + "Would you like to continue with a <b>demo account</b>?"
                        + "</div></html>",
                "Demo Mode",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            User demoUser = new User("Demo", "User", "ST000001", 25, "Other",
                    email, "None", false, false, 90);
            openDashboard(demoUser);
        } else {
            showErrorDialog("Login failed. Please check your database connection.");
        }
    }

    private void showLoading(boolean show) {
        loadingPanel.setVisible(show);
        loginButton.setEnabled(!show);
        registerButton.setEnabled(!show);

        if (show) {
            loadingTimer.start();
        } else {
            loadingTimer.stop();
        }
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this,
                "<html><div style='text-align: center;'>" + message + "</div></html>",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccessDialog(String message) {
        JOptionPane.showMessageDialog(this,
                "<html><div style='text-align: center;'>" + message + "</div></html>",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private void openDashboard(User user) {
        this.dispose();
        Dashboard dashboard = new Dashboard();
        dashboard.setUserData(user);
        dashboard.setVisible(true);
    }

    private void openRegister() {
        this.dispose();
        Register register = new Register();
        register.setVisible(true);
    }

    public static void main(String[] args) {
        // Initialize database quietly - don't crash if it fails
        initializeDatabaseQuietly();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // Set modern UI defaults
            UIManager.put("Button.arc", 20);
            UIManager.put("Component.arc", 20);
            UIManager.put("TextComponent.arc", 15);
        } catch (Exception e) {
            System.err.println("Error setting look and feel: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.setVisible(true);
        });
    }

    private static void initializeDatabaseQuietly() {
        try {
            DatabaseConnection.initializeDatabase();
            UserDAO.createUsersTable();
            System.out.println("✅ Database initialized successfully");
        } catch (Exception e) {
            System.err.println("❌ Database initialization failed: " + e.getMessage());
            System.err.println("💡 Application will run in demo mode");
            // Don't crash - just continue without database
        }
    }
}