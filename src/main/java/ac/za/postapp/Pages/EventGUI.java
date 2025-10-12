package ac.za.postapp.Pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class EventGUI extends JFrame implements ActionListener, ItemListener {
    private JTextField txtEventId, txtTitle, txtDescription, txtRoom, txtStart, txtEnd;
    private JComboBox<String> cmbBuilding, cmbAccessibility;
    private JButton btnSave, btnClear, btnClose;
    private HashMap<String,String> buildingMap;

    public EventGUI() {
        super("Staff Event Management");
        this.setLayout(new BorderLayout());

        // Map buildings
        buildingMap = new HashMap<>();
        buildingMap.put("Engineering","9926");
        buildingMap.put("Commerce","1562");
        buildingMap.put("Design","8549");
        buildingMap.put("Science","8671");
        buildingMap.put("Education","2004");
        buildingMap.put("Informatics and Design","7351");

        // Top form panel (grid 4 rows, 4 columns for two-column layout)
        JPanel pnlForm = new JPanel(new GridLayout(4, 4, 10, 10));
        pnlForm.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        // Row 1: Building (combo) | Event ID (auto)
        pnlForm.add(new JLabel("Building:"));
        cmbBuilding = new JComboBox<>(new String[]{
                "Select Building", "Engineering", "Commerce", "Design", "Science", "Education", "Informatics and Design"});
        cmbBuilding.addItemListener(this);
        pnlForm.add(cmbBuilding);

        pnlForm.add(new JLabel("Event ID (Auto):"));
        txtEventId = new JTextField("System generated");
        txtEventId.setEditable(false);
        pnlForm.add(txtEventId);

        // Row 2: Event Title | Description
        pnlForm.add(new JLabel("Event Title:"));
        txtTitle = new JTextField();
        pnlForm.add(txtTitle);

        pnlForm.add(new JLabel("Description:"));
        txtDescription = new JTextField();
        pnlForm.add(txtDescription);

        // Row 3: Start Time | End Time
        pnlForm.add(new JLabel("Date & Start Time:"));
        txtStart = new JTextField(getCurrentTimestamp());
        pnlForm.add(txtStart);

        pnlForm.add(new JLabel("Date & End Time:"));
        txtEnd = new JTextField(getFutureTimestamp());
        pnlForm.add(txtEnd);

        // Row 4: Room | Accessibility (combo)
        pnlForm.add(new JLabel("Room:"));
        txtRoom = new JTextField();
        pnlForm.add(txtRoom);

        pnlForm.add(new JLabel("Accessibility:"));
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
        pnlForm.add(cmbAccessibility);

        this.add(pnlForm, BorderLayout.CENTER);

        // Buttons at bottom
        JPanel pnlBottom = new JPanel(new FlowLayout());
        btnSave = new JButton("Save Event");
        btnClear = new JButton("Clear");
        btnClose = new JButton("Close");

        // Style buttons
        styleButton(btnSave, new Color(40, 167, 69));
        styleButton(btnClear, new Color(255, 193, 7));
        styleButton(btnClose, new Color(108, 117, 125));

        pnlBottom.add(btnSave);
        pnlBottom.add(btnClear);
        pnlBottom.add(btnClose);
        this.add(pnlBottom, BorderLayout.SOUTH);

        btnSave.addActionListener(this);
        btnClear.addActionListener(this);
        btnClose.addActionListener(this);

        this.setSize(750,400);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private String getFutureTimestamp() {
        return LocalDateTime.now().plusHours(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
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
        if(e.getSource()==btnSave){
            saveEvent();
        } else if(e.getSource()==btnClear){
            clearFields();
        } else if(e.getSource()==btnClose){
            this.dispose();
        }
    }

    private void saveEvent() {
        // Validation
        if(cmbBuilding.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select a building!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(txtTitle.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter event title!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(txtDescription.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter event description!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(txtRoom.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter room number!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(txtStart.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter start time!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(txtEnd.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter end time!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(cmbAccessibility.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Please select accessibility option!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validate timestamp format
        if(!isValidTimestamp(txtStart.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Invalid start time format! Use: YYYY-MM-DD HH:MM:SS", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(!isValidTimestamp(txtEnd.getText().trim())) {
            JOptionPane.showMessageDialog(this, "Invalid end time format! Use: YYYY-MM-DD HH:MM:SS", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Create Event object - using string constructor for compatibility
            Event ev = new Event(
                    0, // Auto-generated ID by database
                    txtTitle.getText().trim(),
                    txtDescription.getText().trim(),
                    cmbBuilding.getSelectedItem().toString(),
                    txtRoom.getText().trim(),
                    txtStart.getText().trim(),
                    txtEnd.getText().trim(),
                    cmbAccessibility.getSelectedItem().toString()
            );

            // Save to database
            boolean success = EventDAO.save(ev);
            if (success) {
                clearFields();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving event: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // Helper method to validate timestamp format
    private boolean isValidTimestamp(String timestamp) {
        try {
            // Replace space with T for LocalDateTime parsing
            String formattedTimestamp = timestamp.replace(" ", "T");
            LocalDateTime.parse(formattedTimestamp);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getSource()==cmbBuilding && e.getStateChange()==ItemEvent.SELECTED){
            String selected = cmbBuilding.getSelectedItem().toString();
            if(buildingMap.containsKey(selected)){
                txtEventId.setText("EVT-" + buildingMap.get(selected) + "-" +
                        System.currentTimeMillis() % 10000);
            } else {
                txtEventId.setText("System generated");
            }
        }
    }

    private void clearFields(){
        txtEventId.setText("System generated");
        txtTitle.setText("");
        txtDescription.setText("");
        txtRoom.setText("");
        txtStart.setText(getCurrentTimestamp());
        txtEnd.setText(getFutureTimestamp());
        cmbBuilding.setSelectedIndex(0);
        cmbAccessibility.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        // Initialize database
        DatabaseConnection.initializeDatabase();

        SwingUtilities.invokeLater(() -> {
            new EventGUI();
        });
    }
}