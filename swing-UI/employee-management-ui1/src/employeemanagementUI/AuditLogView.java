package employeemanagementUI;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AuditLogView {
    private JFrame frame;
    private JTable auditLogTable;
    private JScrollPane scrollPane;

    public AuditLogView(List<AuditLog> auditLogs) {
        frame = new JFrame("Audit Logs");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columns = {"Action", "Performed By", "Date", "Employee ID"};
        Object[][] data = new Object[auditLogs.size()][4];

        for (int i = 0; i < auditLogs.size(); i++) {
            AuditLog log = auditLogs.get(i);
            data[i][0] = log.getAction();
            data[i][1] = log.getPerformedBy().getFullName();
            data[i][2] = log.getPerformedAt();
            data[i][3] = log.getEmployeeId();
        }

        auditLogTable = new JTable(data, columns);
        scrollPane = new JScrollPane(auditLogTable);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

	public void setVisible(boolean b) {
		// TODO Auto-generated method stub
		
	}
}
