package ac.za.postapp.Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FeedbackPage extends JDialog {
    private JComboBox<String> categoryCombo;
    private JTextArea feedbackArea;
    private JTextField emailField;
    private JCheckBox urgentCheckBox;

    public FeedbackPage(JFrame parent) {
        super(parent, "Feedback & Issues", true);
        setSize(600, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JLabel headerLabel = new JLabel("💬 Feedback & Issue Reporting", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(new Color(33, 64, 98));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Category
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        categoryCombo = new JComboBox<>(new String[]{
                "General Feedback", "Accessibility Issue", "Technical Problem",
                "Feature Request", "Map Inaccuracy", "Other"
        });
        formPanel.add(categoryCombo, gbc);

        // Email
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Email (optional):"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField();
        formPanel.add(emailField, gbc);

        // Urgent checkbox
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        urgentCheckBox = new JCheckBox("This is an urgent accessibility issue");
        formPanel.add(urgentCheckBox, gbc);

        // Feedback
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Feedback/Issue:"), gbc);
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        feedbackArea = new JTextArea(8, 30);
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(feedbackArea);
        formPanel.add(scrollPane, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("Cancel");
        JButton submitButton = new JButton("Submit Feedback");

        cancelButton.addActionListener(e -> dispose());
        submitButton.addActionListener(e -> submitFeedback());

        buttonPanel.add(cancelButton);
        buttonPanel.add(submitButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private void submitFeedback() {
        String category = (String) categoryCombo.getSelectedItem();
        String feedback = feedbackArea.getText().trim();
        String email = emailField.getText().trim();
        boolean urgent = urgentCheckBox.isSelected();

        if (feedback.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please describe your feedback or issue.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (urgent && feedback.toLowerCase().contains("elevator")) {
            // Handle urgent elevator issues
            JOptionPane.showMessageDialog(this,
                    "<html><b>🚨 Urgent Elevator Issue Reported</b><br>" +
                            "Campus maintenance has been notified immediately.<br>" +
                            "Thank you for reporting this critical issue.</html>",
                    "Urgent Issue Reported",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            // Regular feedback submission
            String message = "<html><b>✅ Feedback Submitted Successfully</b><br>" +
                    "Category: " + category + "<br>" +
                    (urgent ? "<span style='color: red;'>⚠️ Marked as Urgent</span><br>" : "") +
                    "Thank you for helping us improve!</html>";

            JOptionPane.showMessageDialog(this, message, "Feedback Submitted", JOptionPane.INFORMATION_MESSAGE);
        }

        // In a real application, you would save to database here
        System.out.println("Feedback submitted: " + category + " - " + feedback);

        dispose();
    }
}