package ac.za.postapp.Pages;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SchedulePage extends JDialog {
    private JTable scheduleTable;
    private DefaultTableModel tableModel;
    private List<ScheduleItem> scheduleItems;
    private JComboBox<String> dayComboBox;
    private JComboBox<String> timeComboBox;
    private JTextField subjectField;
    private JTextField locationField;

    public SchedulePage(JFrame parent) {
        super(parent, "Class Schedule", true);
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        scheduleItems = new ArrayList<>();
        initializeSampleData();

        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 247, 250));

        // Title
        JLabel titleLabel = new JLabel("📅 Class Schedule", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(52, 73, 94));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Schedule table
        String[] columns = {"Day", "Time", "Subject", "Location", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only actions column is editable
            }
        };
        scheduleTable = new JTable(tableModel);
        scheduleTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        scheduleTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        scheduleTable.setRowHeight(30);

        // Add action buttons to table
        scheduleTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        scheduleTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Form panel for adding new items
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadScheduleData();
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Add New Class"));
        panel.setBackground(Color.WHITE);

        // Day selection
        panel.add(new JLabel("Day:"));
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        dayComboBox = new JComboBox<>(days);
        panel.add(dayComboBox);

        // Time selection
        panel.add(new JLabel("Time:"));
        String[] times = {"08:00-09:00", "09:00-10:00", "10:00-11:00", "11:00-12:00",
                "12:00-13:00", "13:00-14:00", "14:00-15:00", "15:00-16:00"};
        timeComboBox = new JComboBox<>(times);
        panel.add(timeComboBox);

        // Subject
        panel.add(new JLabel("Subject:"));
        subjectField = new JTextField();
        panel.add(subjectField);

        // Location
        panel.add(new JLabel("Location:"));
        locationField = new JTextField();
        panel.add(locationField);

        // Add button
        JButton addButton = new JButton("Add Class");
        styleButton(addButton, new Color(40, 167, 69));
        addButton.addActionListener(e -> addScheduleItem());
        panel.add(addButton);

        return panel;
    }

    private void initializeSampleData() {
        scheduleItems.add(new ScheduleItem("Monday", "08:00-09:00", "Applications Development Practice 2", "Lab 1.11"));
        scheduleItems.add(new ScheduleItem("Monday", "10:00-11:00", "Project 2", "Lab 1.35"));
        scheduleItems.add(new ScheduleItem("Wednesday", "09:00-10:00", "Programming 1", "Lab 1.19"));
        scheduleItems.add(new ScheduleItem("Thursday", "09:00-10:00", "Professional Communications 2", "Lab 1.19"));
        scheduleItems.add(new ScheduleItem("Friday", "14:00-15:00", "Communication Network fundamentals 2", "Labb 1.3"));
    }

    private void loadScheduleData() {
        tableModel.setRowCount(0);
        for (ScheduleItem item : scheduleItems) {
            tableModel.addRow(new Object[]{
                    item.getDay(),
                    item.getTime(),
                    item.getSubject(),
                    item.getLocation(),
                    "Delete"
            });
        }
    }

    private void addScheduleItem() {
        String day = (String) dayComboBox.getSelectedItem();
        String time = (String) timeComboBox.getSelectedItem();
        String subject = subjectField.getText().trim();
        String location = locationField.getText().trim();

        if (subject.isEmpty() || location.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ScheduleItem newItem = new ScheduleItem(day, time, subject, location);
        scheduleItems.add(newItem);
        loadScheduleData();

        // Clear fields
        subjectField.setText("");
        locationField.setText("");
    }

    private void deleteScheduleItem(int row) {
        if (row >= 0 && row < scheduleItems.size()) {
            scheduleItems.remove(row);
            loadScheduleData();
        }
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Schedule item class
    private class ScheduleItem {
        private String day;
        private String time;
        private String subject;
        private String location;

        public ScheduleItem(String day, String time, String subject, String location) {
            this.day = day;
            this.time = time;
            this.subject = subject;
            this.location = location;
        }

        public String getDay() { return day; }
        public String getTime() { return time; }
        public String getSubject() { return subject; }
        public String getLocation() { return location; }
    }

    // Button renderer and editor for table actions
    private class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setText("Delete");
            setBackground(new Color(220, 53, 69));
            setForeground(Color.WHITE);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Delete");
            button.setOpaque(true);
            button.setBackground(new Color(220, 53, 69));
            button.setForeground(Color.WHITE);
            button.addActionListener(e -> fireEditingStopped());
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.row = row;
            return button;
        }

        public Object getCellEditorValue() {
            deleteScheduleItem(row);
            return "Delete";
        }
    }
}