package employeemanagementUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EditEmployeeDialog extends JDialog {

    private JTextField fullNameField;
    private JTextField employeeIdField;
    private JTextField jobTitleField;
    private JTextField departmentField;
    private JTextField hireDateField;
    private JTextField employmentStatusField;
    private JTextField contactInfoField;
    private JTextField addressField;
    private ApiService apiService;
    private EmployeeModel employee;

    public EditEmployeeDialog(JFrame parent, EmployeeModel employee) {
        super(parent, "Edit Employee", true);
        this.apiService = new ApiService(null);  // Assuming ApiService constructor doesn't need any argument
        this.employee = employee;

        setLayout(new BorderLayout());
        setSize(400, 400);

        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        fullNameField = new JTextField(employee.getFullName());
        employeeIdField = new JTextField(employee.getEmployeeId());
        jobTitleField = new JTextField(employee.getJobTitle());
        departmentField = new JTextField(employee.getDepartment());
        hireDateField = new JTextField(employee.getHireDate());
        employmentStatusField = new JTextField(employee.getEmploymentStatus());
        contactInfoField = new JTextField(employee.getContactInfo());
        addressField = new JTextField(employee.getAddress());

        formPanel.add(new JLabel("Full Name:"));
        formPanel.add(fullNameField);
        formPanel.add(new JLabel("Employee ID:"));
        formPanel.add(employeeIdField);
        formPanel.add(new JLabel("Job Title:"));
        formPanel.add(jobTitleField);
        formPanel.add(new JLabel("Department:"));
        formPanel.add(departmentField);
        formPanel.add(new JLabel("Hire Date (YYYY-MM-DD):"));
        formPanel.add(hireDateField);
        formPanel.add(new JLabel("Employment Status:"));
        formPanel.add(employmentStatusField);
        formPanel.add(new JLabel("Contact Information:"));
        formPanel.add(contactInfoField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        saveButton.addActionListener(this::handleSaveAction);
        cancelButton.addActionListener(e -> dispose());

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(parent);
    }

    private void handleSaveAction(ActionEvent e) {
        try {
            // Update employee with new data from the form fields
            employee.setFullName(fullNameField.getText());
            employee.setEmployeeId(employeeIdField.getText());
            employee.setJobTitle(jobTitleField.getText());
            employee.setDepartment(departmentField.getText());
            employee.setHireDate(hireDateField.getText());
            employee.setEmploymentStatus(employmentStatusField.getText());
            employee.setContactInfo(contactInfoField.getText());
            employee.setAddress(addressField.getText());

            // Call ApiService to update the employee on the backend
            apiService.updateEmployee(employee);

            JOptionPane.showMessageDialog(this, "Employee updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
