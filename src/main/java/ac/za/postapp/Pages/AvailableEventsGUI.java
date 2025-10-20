package ac.za.postapp.Pages;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AvailableEventsGUI extends JFrame implements ActionListener {
    private JComboBox<String> cmbBuilding, cmbAccessibility;
    private JTextField txtDate;
    private JButton btnClose, btnSearch, btnShowAll, btnManageEvents;
    private JTable tbl;
    private DefaultTableModel tblModel;
    private JLabel statusLabel;

    public AvailableEventsGUI() {
        super("Available Accessible Events");
        this.setLayout(new BorderLayout());

        // Filter Panel (2-column layout)
        JPanel pnlTop = new JPanel(new GridLayout(3, 2, 10, 10));
        pnlTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Row 1: Building + Date
        JPanel pnlBuilding = new JPanel(new BorderLayout());
        pnlBuilding.add(new JLabel("Building:"), BorderLayout.WEST);
        cmbBuilding = new JComboBox<>(new String[]{
                "Select Building", "Engineering", "Commerce", "Design", "Science", "Education", "Informatics and Design"});
        pnlBuilding.add(cmbBuilding, BorderLayout.CENTER);

        JPanel pnlDate = new JPanel(new BorderLayout());
        pnlDate.add(new JLabel("Date (YYYY-MM-DD):"), BorderLayout.WEST);
        txtDate = new JTextField(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
        pnlDate.add(txtDate, BorderLayout.CENTER);

        pnlTop.add(pnlBuilding);
        pnlTop.add(pnlDate);

        // Row 2: Accessibility + Search Button
        JPanel pnlAccessibility = new JPanel(new BorderLayout());
        pnlAccessibility.add(new JLabel("Accessibility:"), BorderLayout.WEST);
        cmbAccessibility = new JComboBox<>(new String[]{
                "Select Accessibility",
                "Wheelchair accessible routes",
                "Elevator access required",
                "Ground floor venue",
                "Accessible parking nearby",
                "Sign language interpreter available",
                "Audio induction loop available",
                "Braille signage available"
        });
        pnlAccessibility.add(cmbAccessibility, BorderLayout.CENTER);

        JPanel pnlButtons = new JPanel(new FlowLayout());
        btnSearch = new JButton("Search Events");
        btnShowAll = new JButton("Show All");
        styleButton(btnSearch, new Color(40, 167, 69));
        styleButton(btnShowAll, new Color(0, 123, 255));
        pnlButtons.add(btnSearch);
        pnlButtons.add(btnShowAll);

        pnlTop.add(pnlAccessibility);
        pnlTop.add(pnlButtons);

        // Row 3: Status + Manage Events Button
        JPanel pnlStatus = new JPanel(new BorderLayout());
        statusLabel = new JLabel("Ready to search events");
        statusLabel.setForeground(Color.BLUE);
        pnlStatus.add(statusLabel, BorderLayout.WEST);

        JPanel pnlManage = new JPanel(new FlowLayout());
        btnManageEvents = new JButton("Manage Events (Staff)");
        styleButton(btnManageEvents, new Color(255, 193, 7));
        btnManageEvents.setForeground(Color.BLACK);
        pnlManage.add(btnManageEvents);

        pnlTop.add(pnlStatus);
        pnlTop.add(pnlManage);

        this.add(pnlTop, BorderLayout.NORTH);

        // Table with better column widths
        String[] cols = {"ID", "Title", "Building", "Room", "Date", "Time", "Accessibility"};
        tblModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        tbl = new JTable(tblModel);
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbl.setRowHeight(25);
        tbl.getColumnModel().getColumn(0).setPreferredWidth(50);
        tbl.getColumnModel().getColumn(1).setPreferredWidth(150);
        tbl.getColumnModel().getColumn(2).setPreferredWidth(100);
        tbl.getColumnModel().getColumn(3).setPreferredWidth(80);
        tbl.getColumnModel().getColumn(4).setPreferredWidth(100);
        tbl.getColumnModel().getColumn(5).setPreferredWidth(100);
        tbl.getColumnModel().getColumn(6).setPreferredWidth(150);

        // Add double-click listener for event details
        tbl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    showEventDetails();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tbl);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Available Events"));
        this.add(scrollPane, BorderLayout.CENTER);

        // Bottom buttons
        JPanel pnlBottom = new JPanel(new FlowLayout());
        btnClose = new JButton("Close");
        styleButton(btnClose, new Color(108, 117, 125));
        pnlBottom.add(btnClose);
        this.add(pnlBottom, BorderLayout.SOUTH);

        btnClose.addActionListener(this);
        btnSearch.addActionListener(this);
        btnShowAll.addActionListener(this);
        btnManageEvents.addActionListener(this);

        this.setSize(900, 600);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // Load all events initially
        loadAllEvents();
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE); // Set text to white
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
                button.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
                button.setForeground(Color.WHITE);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==btnClose){
            this.dispose();
        } else if(e.getSource()==btnSearch){
            searchEvents();
        } else if(e.getSource()==btnShowAll){
            loadAllEvents();
        } else if(e.getSource()==btnManageEvents){
            openEventManager();
        }
    }

    private void searchEvents() {
        // Validation
        if(cmbBuilding.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a building!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(cmbAccessibility.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select accessibility option!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(txtDate.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a date!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validate date format
        if(!isValidDate(txtDate.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Invalid date format! Use: YYYY-MM-DD", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String building = cmbBuilding.getSelectedItem().toString();
        String date = txtDate.getText().trim();
        String accessibility = cmbAccessibility.getSelectedItem().toString();

        statusLabel.setText("Searching events...");
        statusLabel.setForeground(Color.ORANGE);

        // Query DB with filter - Use mock data if database not available
        ArrayList<Event> events = getMockEvents(building, date, accessibility);
        displayEvents(events);

        if(events.isEmpty()){
            statusLabel.setText("No events found matching your criteria.");
            statusLabel.setForeground(Color.RED);
            JOptionPane.showMessageDialog(this, "No events found matching your criteria.", "No Results", JOptionPane.INFORMATION_MESSAGE);
        } else {
            statusLabel.setText("Found " + events.size() + " event(s)");
            statusLabel.setForeground(Color.GREEN);
        }
    }

    private void loadAllEvents() {
        statusLabel.setText("Loading all events...");
        statusLabel.setForeground(Color.ORANGE);

        // Use mock data if database not available
        ArrayList<Event> events = getAllMockEvents();
        displayEvents(events);

        if(events.isEmpty()){
            statusLabel.setText("No events available in the system.");
            statusLabel.setForeground(Color.RED);
            JOptionPane.showMessageDialog(this, "No events available in the system.", "No Events", JOptionPane.INFORMATION_MESSAGE);
        } else {
            statusLabel.setText("Loaded " + events.size() + " event(s)");
            statusLabel.setForeground(Color.GREEN);
        }
    }

    private void displayEvents(ArrayList<Event> events) {
        tblModel.setRowCount(0); // Clear old results

        for(Event ev: events){
            tblModel.addRow(new Object[]{
                    ev.getEventId(),
                    ev.getTitle(),
                    ev.getBuilding(),
                    ev.getRoom(),
                    formatDate(ev.getStartTime()),
                    formatTimeRange(ev.getStartTime(), ev.getEndTime()),
                    ev.getAccessibility()
            });
        }
    }

    private String formatDate(java.time.LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }

    private String formatTimeRange(java.time.LocalDateTime start, java.time.LocalDateTime end) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return start.format(timeFormatter) + " - " + end.format(timeFormatter);
    }

    private void showEventDetails() {
        int selectedRow = tbl.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an event first!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int eventId = (Integer) tblModel.getValueAt(selectedRow, 0);
        String title = (String) tblModel.getValueAt(selectedRow, 1);

        // Get full event details from mock data
        ArrayList<Event> allEvents = getAllMockEvents();
        Event selectedEvent = null;
        for (Event event : allEvents) {
            if (event.getEventId() == eventId) {
                selectedEvent = event;
                break;
            }
        }

        if (selectedEvent != null) {
            String details = "<html><body style='width: 350px'>" +
                    "<h2 style='color: #2E86AB;'>Event Details</h2>" +
                    "<table border='0' cellpadding='5'>" +
                    "<tr><td><b>ID:</b></td><td>" + selectedEvent.getEventId() + "</td></tr>" +
                    "<tr><td><b>Title:</b></td><td>" + selectedEvent.getTitle() + "</td></tr>" +
                    "<tr><td><b>Description:</b></td><td>" + selectedEvent.getDescription() + "</td></tr>" +
                    "<tr><td><b>Building:</b></td><td>" + selectedEvent.getBuilding() + "</td></tr>" +
                    "<tr><td><b>Room:</b></td><td>" + selectedEvent.getRoom() + "</td></tr>" +
                    "<tr><td><b>Start:</b></td><td>" + selectedEvent.getStartTimeString() + "</td></tr>" +
                    "<tr><td><b>End:</b></td><td>" + selectedEvent.getEndTimeString() + "</td></tr>" +
                    "<tr><td><b>Accessibility:</b></td><td style='color: green;'>" + selectedEvent.getAccessibility() + "</td></tr>" +
                    "</table><br>" +
                    "<i>Would you like to navigate to this event?</i>" +
                    "</body></html>";

            int option = JOptionPane.showConfirmDialog(this, details,
                    "Event Information - " + selectedEvent.getTitle(),
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

            if (option == JOptionPane.YES_OPTION) {
                navigateToEvent(selectedEvent);
            }
        }
    }

    private void navigateToEvent(Event event) {
        String navigationInfo = "<html><body style='width: 300px'>" +
                "<h3 style='color: #2E86AB;'>🎯 Navigation to Event</h3>" +
                "<b>Event:</b> " + event.getTitle() + "<br>" +
                "<b>Location:</b> " + event.getBuilding() + " - Room " + event.getRoom() + "<br>" +
                "<b>Time:</b> " + event.getStartTimeString() + "<br><br>" +
                "<b style='color: green;'>Accessibility Features:</b><br>" +
                "• " + event.getAccessibility() + "<br><br>" +
                "<i>Opening campus map with accessible route...</i>" +
                "</body></html>";

        JOptionPane.showMessageDialog(this, navigationInfo,
                "Event Navigation - " + event.getTitle(),
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void openEventManager() {
        int response = JOptionPane.showConfirmDialog(this,
                "This will open the Event Management interface for staff.\n" +
                        "Do you want to continue?",
                "Open Event Manager",
                JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            this.dispose();
            new ObstacleReportGUI().setVisible(true);
        }
    }

    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Mock data methods to replace database calls
    private ArrayList<Event> getAllMockEvents() {
        ArrayList<Event> events = new ArrayList<>();

        // Add sample events
        events.add(new Event(1, "Computer Science Lecture",
                "Introduction to Java Programming for beginners", "Engineering", "Room 101",
                java.time.LocalDateTime.now().plusDays(1).withHour(10).withMinute(0),
                java.time.LocalDateTime.now().plusDays(1).withHour(11).withMinute(30),
                "Wheelchair accessible routes"));

        events.add(new Event(2, "Art Exhibition Opening",
                "Annual student art showcase featuring various mediums", "Design", "Gallery A",
                java.time.LocalDateTime.now().plusDays(2).withHour(14).withMinute(0),
                java.time.LocalDateTime.now().plusDays(2).withHour(16).withMinute(0),
                "Ground floor venue"));

        events.add(new Event(3, "Business Seminar",
                "Entrepreneurship and startup funding strategies", "Commerce", "Auditorium",
                java.time.LocalDateTime.now().plusDays(3).withHour(9).withMinute(0),
                java.time.LocalDateTime.now().plusDays(3).withHour(12).withMinute(0),
                "Elevator access required"));

        events.add(new Event(4, "Science Fair",
                "Student research projects and demonstrations", "Science", "Main Hall",
                java.time.LocalDateTime.now().plusDays(5).withHour(10).withMinute(0),
                java.time.LocalDateTime.now().plusDays(5).withHour(15).withMinute(0),
                "Accessible parking nearby"));

        events.add(new Event(5, "Education Workshop",
                "Teaching strategies for inclusive classrooms", "Education", "Room 205",
                java.time.LocalDateTime.now().plusDays(7).withHour(13).withMinute(0),
                java.time.LocalDateTime.now().plusDays(7).withHour(16).withMinute(0),
                "Sign language interpreter available"));

        return events;
    }

    private ArrayList<Event> getMockEvents(String building, String date, String accessibility) {
        ArrayList<Event> allEvents = getAllMockEvents();
        ArrayList<Event> filteredEvents = new ArrayList<>();

        LocalDate searchDate = LocalDate.parse(date);

        for (Event event : allEvents) {
            LocalDate eventDate = event.getStartTime().toLocalDate();
            boolean buildingMatch = event.getBuilding().equals(building);
            boolean dateMatch = eventDate.equals(searchDate);
            boolean accessibilityMatch = event.getAccessibility().equals(accessibility);

            if (buildingMatch && dateMatch && accessibilityMatch) {
                filteredEvents.add(event);
            }
        }

        return filteredEvents;
    }

    public static void main(String[] args) {
        // Initialize database if available, otherwise use mock data
        try {
            DatabaseConnection.initializeDatabase();
        } catch (Exception e) {
            System.out.println("Using mock data - Database not available");
        }

        SwingUtilities.invokeLater(() -> {
            new AvailableEventsGUI();
        });
    }
}
