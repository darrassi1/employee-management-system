package employeemanagementUI;


import javax.swing.SwingUtilities;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmployeeManagementUI::new);
    }
}
