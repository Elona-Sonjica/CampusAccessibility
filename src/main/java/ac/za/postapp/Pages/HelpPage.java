package ac.za.postapp.Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HelpPage extends JDialog {
    public HelpPage(JFrame parent) {
        super(parent, "Help & Support", true);
        setSize(700, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JLabel headerLabel = new JLabel("💁 Help & Support Center", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(new Color(33, 64, 98));
        mainPanel.add(headerLabel, BorderLayout.NORTH);

        // Content with tabs
        JTabbedPane tabbedPane = new JTabbedPane();

        // FAQ Tab
        JPanel faqPanel = createFAQPanel();
        tabbedPane.addTab("❓ FAQ", faqPanel);

        // Contact Tab
        JPanel contactPanel = createContactPanel();
        tabbedPane.addTab("📞 Contact", contactPanel);

        // Tutorial Tab
        JPanel tutorialPanel = createTutorialPanel();
        tabbedPane.addTab("🎓 Tutorial", tutorialPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Close button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    private JPanel createFAQPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] questions = {
                "How do I navigate to a specific building?",
                "What accessibility features are available?",
                "How do I check elevator status?",
                "Can I get notifications about campus events?",
                "How do I update my profile information?",
                "What should I do if I find an accessibility issue?"
        };

        String[] answers = {
                "Use the Interactive Map feature from the dashboard to get step-by-step directions.",
                "The app provides wheelchair-accessible routes, elevator status, and facility information.",
                "Check the Live Updates section for real-time elevator and facility status.",
                "Yes! Enable notifications in Settings and you'll receive campus alerts.",
                "Go to your Profile page from the dashboard sidebar to update your information.",
                "Report it immediately through the Feedback section or contact campus support."
        };

        JPanel faqContent = new JPanel();
        faqContent.setLayout(new BoxLayout(faqContent, BoxLayout.Y_AXIS));
        faqContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int i = 0; i < questions.length; i++) {
            JPanel qaPanel = new JPanel(new BorderLayout());
            qaPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

            JLabel question = new JLabel("<html><b>Q: " + questions[i] + "</b></html>");
            JLabel answer = new JLabel("<html>A: " + answers[i] + "</html>");

            qaPanel.add(question, BorderLayout.NORTH);
            qaPanel.add(answer, BorderLayout.CENTER);
            faqContent.add(qaPanel);
            faqContent.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane scrollPane = new JScrollPane(faqContent);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createContactPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] contacts = {
                "📞 Emergency: (021) 959-1234",
                "🛠️ Technical Support: support@campusaccess.edu",
                "♿ Accessibility Office: accessibility@campusaccess.edu",
                "🏛️ Campus Security: security@campusaccess.edu",
                "💼 General Inquiries: info@campusaccess.edu"
        };

        for (String contact : contacts) {
            JLabel contactLabel = new JLabel(contact);
            contactLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            panel.add(contactLabel);
        }

        return panel;
    }

    private JPanel createTutorialPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String tutorialText = "<html><body style='padding: 20px;'>" +
                "<h2>Getting Started with Campus Access Guide</h2>" +
                "<h3>1. Navigation</h3>" +
                "<p>• Use the Interactive Map to find buildings and get directions<br>" +
                "• Check accessibility features before navigating<br>" +
                "• Save frequently visited locations</p>" +

                "<h3>2. Notifications</h3>" +
                "<p>• Enable notifications for campus alerts<br>" +
                "• Get real-time updates on elevator status<br>" +
                "• Receive event reminders</p>" +

                "<h3>3. Facilities</h3>" +
                "<p>• Explore campus facilities and their features<br>" +
                "• Check operating hours and availability<br>" +
                "• View accessibility information</p>" +

                "<h3>4. Events</h3>" +
                "<p>• Browse upcoming campus events<br>" +
                "• Filter by accessibility requirements<br>" +
                "• Get directions to event locations</p>" +

                "<h3>5. Profile & Settings</h3>" +
                "<p>• Update your personal information<br>" +
                "• Customize accessibility preferences<br>" +
                "• Manage notification settings</p>" +
                "</body></html>";

        JLabel tutorialLabel = new JLabel(tutorialText);
        tutorialLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(tutorialLabel);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
}
