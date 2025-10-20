package ac.za.postapp.Pages;

import javax.swing.*;
import java.awt.*;

/**
 * Elona Sonjica
 */
public class ProfilePage extends JDialog {
    private User currentUser;
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField studentNumberField;
    private JTextField ageField;
    private JComboBox<String> genderComboBox;
    private JTextField emailField;
    private JTextField phoneField;
    private JComboBox<String> facultyComboBox;
    private JTextField courseField;
    private JTextArea bioArea;

    public ProfilePage(JFrame parent, User user) {
        super(parent, "User Profile", true);
        this.currentUser = user;
        setSize(760, 680);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(8, 8));
        setResizable(false);

        JPanel main = new JPanel(new BorderLayout());
        main.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        main.add(createHeaderPanel(), BorderLayout.NORTH);
        main.add(createFormPanel(), BorderLayout.CENTER);
        main.add(createButtonPanel(), BorderLayout.SOUTH);

        add(main);
        loadUserData();
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel title = new JLabel("👤 User Profile");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.add(title);
        return header;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Personal Information"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        panel.add(nameField, gbc);

        row++; gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Surname:"), gbc);
        gbc.gridx = 1;
        surnameField = new JTextField(20);
        panel.add(surnameField, gbc);

        row++; gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Student Number:"), gbc);
        gbc.gridx = 1;
        studentNumberField = new JTextField(15);
        studentNumberField.setEditable(false);
        panel.add(studentNumberField, gbc);

        row++; gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        ageField = new JTextField(4);
        panel.add(ageField, gbc);

        row++; gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        genderComboBox = new JComboBox<>(new String[]{"Male", "Female", "Other", "Prefer not to say"});
        panel.add(genderComboBox, gbc);

        row++; gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        panel.add(emailField, gbc);

        row++; gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(15);
        panel.add(phoneField, gbc);

        row++; gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Faculty:"), gbc);
        gbc.gridx = 1;
        facultyComboBox = new JComboBox<>(new String[]{"Informatics & Design","Engineering","Health Sciences","Business","Education","Other"});
        panel.add(facultyComboBox, gbc);

        row++; gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Course:"), gbc);
        gbc.gridx = 1;
        courseField = new JTextField(20);
        panel.add(courseField, gbc);

        row++; gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Bio:"), gbc);
        gbc.gridx = 1;
        bioArea = new JTextArea(4, 20);
        bioArea.setLineWrap(true);
        bioArea.setWrapStyleWord(true);
        panel.add(new JScrollPane(bioArea), gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelBtn = new JButton("Cancel");
        JButton saveBtn = new JButton("Save Profile");

        cancelBtn.addActionListener(e -> dispose());
        saveBtn.addActionListener(e -> saveProfile());

        panel.add(cancelBtn);
        panel.add(saveBtn);
        return panel;
    }

    private void loadUserData() {
        if (currentUser == null) return;

        nameField.setText(currentUser.getName());
        surnameField.setText(currentUser.getSurname());
        studentNumberField.setText(currentUser.getStudentNumber() == null ? "" : currentUser.getStudentNumber());
        ageField.setText(String.valueOf(currentUser.getAge()));
        genderComboBox.setSelectedItem(currentUser.getGender() == null ? "Other" : currentUser.getGender());
        emailField.setText(currentUser.getEmail() == null ? "" : currentUser.getEmail());
        phoneField.setText(currentUser.getPhone() == null ? "" : currentUser.getPhone());
        facultyComboBox.setSelectedItem(currentUser.getFaculty() == null ? "Informatics & Design" : currentUser.getFaculty());
        courseField.setText(currentUser.getCourse() == null ? "" : currentUser.getCourse());
        bioArea.setText(currentUser.getBio() == null ? "" : currentUser.getBio());
    }

    private void saveProfile() {
        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String ageStr = ageField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || surname.isEmpty() || ageStr.isEmpty() || email.isEmpty()) {
            showErrorDialog("Please fill in all required fields (Name, Surname, Age, Email).");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
            if (age < 16 || age > 100) {
                showErrorDialog("Please enter a valid age (16-100).");
                return;
            }
        } catch (NumberFormatException e) {
            showErrorDialog("Age must be a valid number.");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            showErrorDialog("Please enter a valid email address.");
            return;
        }

        // Update currentUser
        currentUser.setName(name);
        currentUser.setSurname(surname);
        currentUser.setAge(age);
        currentUser.setGender((String) genderComboBox.getSelectedItem());
        currentUser.setEmail(email);
        currentUser.setPhone(phoneField.getText().trim());
        currentUser.setFaculty((String) facultyComboBox.getSelectedItem());
        currentUser.setCourse(courseField.getText().trim());
        currentUser.setBio(bioArea.getText().trim());

        JOptionPane.showMessageDialog(this, "Profile updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}