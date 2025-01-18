package employeemanagementUI;

import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;
import java.time.*;
import java.time.format.*;
import java.util.Timer;
import java.util.TimerTask;

public class EmployeeManagementUI extends JFrame {

    private ApiService apiService;
    private JTextArea employeeListArea;
    private AuthService authService;
    private JPanel mainPanel;
    private JTextField searchField;
    private JLabel timeLabel;
    private JLabel userLabel;

    private Timer timer;

    // UI Constants
    private static final Color PRIMARY_COLOR = new Color(25, 118, 210);
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color HEADER_COLOR = new Color(33, 33, 33);
    private static final Color SECONDARY_TEXT_COLOR = new Color(97, 97, 97);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    public EmployeeManagementUI() {
        authService = new AuthService();
        showLoginDialog();
        apiService = new ApiService(authService);
        initializeFrame();
        createComponents();
        startTimeUpdater();
        fetchEmployees();
    }

    private void initializeFrame() {
        setTitle("Employee Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 800));
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);
    }

    private void createComponents() {
        mainPanel = new JPanel(new MigLayout("fill, insets 20", "[grow]", "[][grow][]"));
        mainPanel.setBackground(BACKGROUND_COLOR);

        createHeaderPanel();
        createContentPanel();
        createFooterPanel();

        add(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void startTimeUpdater() {
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    timeLabel.setText(LocalDateTime.now(ZoneOffset.UTC)
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                });
            }
        }, 0, 1000);
    }

    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel(new MigLayout("fillx, insets 0 0 20 0", "[grow][]"));
        headerPanel.setBackground(BACKGROUND_COLOR);

        // Left side - Title and user info
        JPanel titlePanel = new JPanel(new MigLayout("insets 0"));
        titlePanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Employee Management System");
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(HEADER_COLOR);

        // User info panel
        JPanel userInfoPanel = new JPanel(new MigLayout("insets 0"));
        userInfoPanel.setBackground(BACKGROUND_COLOR);

        userLabel = new JLabel("User: " + authService.getCurrentUser().getUsername());
        timeLabel = new JLabel("UTC: " + LocalDateTime.now(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        userLabel.setFont(SMALL_FONT);
        timeLabel.setFont(SMALL_FONT);
        userLabel.setForeground(SECONDARY_TEXT_COLOR);
        timeLabel.setForeground(SECONDARY_TEXT_COLOR);

        userInfoPanel.add(userLabel, "split 2");
        userInfoPanel.add(timeLabel, "gapleft 20");

        titlePanel.add(titleLabel, "wrap");
        titlePanel.add(userInfoPanel, "gaptop 5");

        // Right side - Search
        JPanel searchPanel = new JPanel(new MigLayout("insets 0"));
        searchPanel.setBackground(BACKGROUND_COLOR);

        searchField = createStyledTextField(25);
        searchField.setToolTipText("Search employees...");
        JButton searchButton = createStyledButton("Search", PRIMARY_COLOR);
        searchButton.addActionListener(e -> searchEmployees(searchField.getText()));

        searchPanel.add(searchField);
        searchPanel.add(searchButton, "gapleft 10");

        headerPanel.add(titlePanel);
        headerPanel.add(searchPanel, "right");

        mainPanel.add(headerPanel, "grow, wrap");
    }

    private void createContentPanel() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(218, 220, 224), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        employeeListArea = new JTextArea();
        employeeListArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        employeeListArea.setEditable(false);
        employeeListArea.setBackground(Color.WHITE);
        employeeListArea.setLineWrap(true);
        employeeListArea.setWrapStyleWord(true);
        employeeListArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(employeeListArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        contentPanel.add(scrollPane, gbc);

        mainPanel.add(contentPanel, "grow, wrap");
    }

    private void createFooterPanel() {
        JPanel footerPanel = new JPanel(new MigLayout("insets 20 0 0 0", "[grow][]"));
        footerPanel.setBackground(BACKGROUND_COLOR);

        // Action buttons
        JPanel buttonPanel = new JPanel(new MigLayout("insets 0"));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton addButton = createStyledButton("Add Employee", PRIMARY_COLOR);
        JButton editButton = createStyledButton("Edit Employee", PRIMARY_COLOR);
        JButton deleteButton = createStyledButton("Delete Employee", new Color(211, 47, 47));
        JButton auditLogButton = createStyledButton("Audit Log", PRIMARY_COLOR);
        JButton logoutButton = createStyledButton("Logout", new Color(97, 97, 97));

        buttonPanel.add(addButton);
        buttonPanel.add(editButton, "gapleft 10");
        buttonPanel.add(deleteButton, "gapleft 10");
        buttonPanel.add(auditLogButton, "gapleft 10");
        buttonPanel.add(logoutButton, "gapleft 20");

        // Add action listeners
        addButton.addActionListener(e -> addEmployee());
        editButton.addActionListener(e -> editEmployee());
        deleteButton.addActionListener(e -> deleteEmployee());
        logoutButton.addActionListener(e -> performLogout());
        auditLogButton.addActionListener(e -> viewAuditLog());

        footerPanel.add(buttonPanel, "right");
        mainPanel.add(footerPanel, "grow");
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(REGULAR_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(130, 35));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });

        return button;
    }

    private JTextField createStyledTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(REGULAR_FONT);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(218, 220, 224)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return textField;
    }

    private void showLoginDialog() {
        LoginDialog loginDialog = new LoginDialog(this);
        loginDialog.setVisible(true);

        if (!loginDialog.isAuthenticated()) {
            System.exit(0);
        }
    }

    private void performLogout() {
        try {
            if (authService.logout()) {
                timer.cancel(); // Stop the timer before disposing
                dispose();
                new EmployeeManagementUI();
            }
        } catch (Exception ex) {
            showMessage("Logout failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fetchEmployees() {
        employeeListArea.setText("Loading...");

        SwingWorker<List<EmployeeModel>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<EmployeeModel> doInBackground() throws Exception {
                return apiService.getAllEmployees();
            }

            @Override
            protected void done() {
                try {
                    List<EmployeeModel> employees = get();
                    if (employees.isEmpty()) {
                        employeeListArea.setText("No employees found.");
                    } else {
                        displayEmployeeList(employees);
                    }
                } catch (Exception e) {
                    String errorMessage = "Error fetching employee list: " +
                        (e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
                    employeeListArea.setText("Error: Unable to load employees");
                    System.out.println(errorMessage);
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private void searchEmployees(String query) {
        SwingWorker<List<EmployeeModel>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<EmployeeModel> doInBackground() throws Exception {
                return apiService.searchEmployees(query);
            }

            @Override
            protected void done() {
                try {
                    List<EmployeeModel> employees = get();
                    displayEmployeeList(employees);
                } catch (Exception e) {
                    e.printStackTrace();
                    showMessage("Error searching employees", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void displayEmployeeList(List<EmployeeModel> employees) {
        JPanel employeeListPanel = new JPanel();
        employeeListPanel.setLayout(new BoxLayout(employeeListPanel, BoxLayout.Y_AXIS));
        employeeListPanel.setBackground(Color.WHITE);

        for (EmployeeModel employee : employees) {
            JPanel employeeCard = createEmployeeCard(employee);
            employeeListPanel.add(employeeCard);
            employeeListPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing between cards
        }

        // Replace the text area with the new panel
        JScrollPane scrollPane = (JScrollPane) employeeListArea.getParent().getParent();
        scrollPane.setViewportView(employeeListPanel);
    }

    private JPanel createEmployeeCard(EmployeeModel employee) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Employee info panel
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 5, 2, 5);

        // Add employee details with styled labels
        addCardDetail(infoPanel, "ID: ", employee.getEmployeeId(), gbc, 0);
        addCardDetail(infoPanel, "Name: ", employee.getFullName(), gbc, 1);
        addCardDetail(infoPanel, "Job Title: ", employee.getJobTitle(), gbc, 2);
        addCardDetail(infoPanel, "Department: ", employee.getDepartment(), gbc, 3);
        addCardDetail(infoPanel, "Status: ", employee.getEmploymentStatus(), gbc, 4);
        addCardDetail(infoPanel, "Contact: ", employee.getContactInfo(), gbc, 5);

        // Action buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton editButton = createActionButton("Edit", new Color(25, 118, 210));
        JButton deleteButton = createActionButton("Delete", new Color(211, 47, 47));

        editButton.addActionListener(e -> openEditDialog(employee));
        deleteButton.addActionListener(e -> confirmAndDelete(employee));

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    private void addCardDetail(JPanel panel, String label, String value, GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        gbc.gridx = 0;
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.BOLD, 12));
        labelComp.setForeground(SECONDARY_TEXT_COLOR);
        panel.add(labelComp, gbc);

        gbc.gridx = 1;
        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(valueComp, gbc);
    }

    private JButton createActionButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(80, 30));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });

        return button;
    }

    private void openEditDialog(EmployeeModel employee) {
        EditEmployeeDialog dialog = new EditEmployeeDialog(this, employee);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        // Refresh the list after editing
        fetchEmployees();
    }

    private void confirmAndDelete(EmployeeModel employee) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete employee: " + employee.getFullName() + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                apiService.deleteEmployee(employee.getEmployeeId());
                fetchEmployees();
                showMessage("Employee deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                showMessage("Error deleting employee.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addEmployee() {
        AddEmployeeDialog dialog = new AddEmployeeDialog(this);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void editEmployee() {
        String id = JOptionPane.showInputDialog(this,
            "Enter employee ID to edit:",
            "Edit Employee",
            JOptionPane.PLAIN_MESSAGE);

        if (id != null && !id.isEmpty()) {
            try {
                EmployeeModel employee = apiService.getEmployeeById(id);
                EditEmployeeDialog dialog = new EditEmployeeDialog(this, employee);
                dialog.setLocationRelativeTo(this);
                dialog.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                showMessage("Error fetching employee details.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteEmployee() {
        String id = JOptionPane.showInputDialog(this,
            "Enter employee ID to delete:",
            "Delete Employee",
            JOptionPane.WARNING_MESSAGE);

        if (id != null && !id.isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete employee with ID: " + id + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    apiService.deleteEmployee(id);
                    fetchEmployees();
                    showMessage("Employee deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    e.printStackTrace();
                    showMessage("Error deleting employee.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void viewAuditLog() {
        try {
            List<AuditLog> auditLogs = apiService.getAuditLogs();
            new AuditLogView(auditLogs).setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            showMessage("Error fetching audit logs.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    // Main method for testing
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new EmployeeManagementUI());
    }
}