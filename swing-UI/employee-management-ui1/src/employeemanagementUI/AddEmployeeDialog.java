package employeemanagementUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddEmployeeDialog extends JDialog {
    private static final Color PRIMARY_COLOR = new Color(51, 122, 183);
    private static final Color SECONDARY_COLOR = new Color(238, 238, 238);
    private static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 12);

    private final JTextField fullNameField;
    private final JTextField employeeIdField;
    private final JTextField jobTitleField;
    private final JComboBox<String> departmentField;
    private final JTextField hireDateField;
    private final JComboBox<String> employmentStatusField;
    private final JTextField contactInfoField;
    private final JTextArea addressField;
    private final ApiService apiService;
    private final AuthService authService;

    public AddEmployeeDialog(JFrame parent) {
        super(parent, "Add Employee", true);
        authService = new AuthService();
        apiService = new ApiService(authService);

        setLayout(new BorderLayout(10, 10));
        setSize(500, 600);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel("Add New Employee");
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Initialize components
        fullNameField = createStyledTextField();
        employeeIdField = createStyledTextField();
        jobTitleField = createStyledTextField();
        String[] departments = {"IT", "HR", "Finance", "Marketing", "Operations"};
        departmentField = new JComboBox<>(departments);
        hireDateField = createStyledTextField();
        String[] statuses = {"Active", "Inactive", "On Leave", "Terminated"};
        employmentStatusField = new JComboBox<>(statuses);
        contactInfoField = createStyledTextField();
        addressField = new JTextArea(3, 20);
        addressField.setLineWrap(true);
        
        // Set today's date as default
        hireDateField.setText(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));

        // Add components to form
        addFormRow(formPanel, "Full Name *:", fullNameField, gbc, 0);
        addFormRow(formPanel, "Employee ID *:", employeeIdField, gbc, 1);
        addFormRow(formPanel, "Job Title *:", jobTitleField, gbc, 2);
        addFormRow(formPanel, "Department *:", departmentField, gbc, 3);
        addFormRow(formPanel, "Hire Date (YYYY-MM-DD) *:", hireDateField, gbc, 4);
        addFormRow(formPanel, "Employment Status *:", employmentStatusField, gbc, 5);
        addFormRow(formPanel, "Contact Information *:", contactInfoField, gbc, 6);
        
        // Address needs special handling
        gbc.gridy = 7;
        gbc.gridx = 0;
        formPanel.add(createStyledLabel("Address *:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(addressField), gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(SECONDARY_COLOR);
        
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        styleButton(saveButton);
        styleButton(cancelButton);

        saveButton.addActionListener(this::handleSaveAction);
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add all panels to dialog
        add(headerPanel, BorderLayout.NORTH);
        add(new JScrollPane(formPanel), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(parent);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(200, 25));
        return field;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        return label;
    }

    private void addFormRow(JPanel panel, String labelText, JComponent field, 
                          GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        gbc.gridx = 0;
        panel.add(createStyledLabel(labelText), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void styleButton(JButton button) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFont(LABEL_FONT);
    }

    private boolean validateInput() {
        if (fullNameField.getText().trim().isEmpty() ||
            employeeIdField.getText().trim().isEmpty() ||
            jobTitleField.getText().trim().isEmpty() ||
            contactInfoField.getText().trim().isEmpty() ||
            addressField.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(this,
                "All fields marked with * are required.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate employee ID format (e.g., EMP123)
        if (!employeeIdField.getText().matches("^EMP\\d{3,}$")) {
            JOptionPane.showMessageDialog(this,
                "Employee ID must start with 'EMP' followed by at least 3 digits.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate date format
        try {
            LocalDate.parse(hireDateField.getText().trim(), DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                "Invalid date format. Please use YYYY-MM-DD",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void handleSaveAction(ActionEvent e) {
        if (!validateInput()) {
            return;
        }

        try {
            EmployeeModel newEmployee = new EmployeeModel();
            newEmployee.setFullName(fullNameField.getText().trim());
            newEmployee.setEmployeeId(employeeIdField.getText().trim());
            newEmployee.setJobTitle(jobTitleField.getText().trim());
            newEmployee.setDepartment(departmentField.getSelectedItem().toString());
            newEmployee.setHireDate(hireDateField.getText().trim());
            newEmployee.setEmploymentStatus(employmentStatusField.getSelectedItem().toString());
            newEmployee.setContactInfo(contactInfoField.getText().trim());
            newEmployee.setAddress(addressField.getText().trim());

            apiService.addEmployee(newEmployee);

            JOptionPane.showMessageDialog(this,
                "Employee added successfully.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}