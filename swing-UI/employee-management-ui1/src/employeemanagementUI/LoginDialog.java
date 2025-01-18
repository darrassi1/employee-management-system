package employeemanagementUI;

import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean authenticated = false;
    private AuthService authService;

    public LoginDialog(Frame parent) {
        super(parent, "Login", true);
        authService = new AuthService();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username field
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(20);
        formPanel.add(usernameField, gbc);

        // Password field
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        JButton loginButton = new JButton("Login");
        JButton cancelButton = new JButton("Cancel");
        JButton registerButton = new JButton("Register"); // Register button

        loginButton.addActionListener(e -> performLogin());
        cancelButton.addActionListener(e -> dispose());
        registerButton.addActionListener(e -> showRegisterDialog()); // Show register dialog

        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(registerButton); // Add register button to panel

        // Add panels to dialog
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getParent());
    }

    private void performLogin() {
        try {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (authService.login(username, password)) {
                authenticated = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid username or password",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Login error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showRegisterDialog() {
        RegisterDialog registerDialog = new RegisterDialog(this);
        registerDialog.setVisible(true);
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}