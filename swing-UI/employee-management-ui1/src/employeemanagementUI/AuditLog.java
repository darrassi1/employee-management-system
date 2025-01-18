package employeemanagementUI;

import java.util.Date;

public class AuditLog {
    private Date timestamp;
    private String action;
    private EmployeeModel performedBy; // Assuming performedBy is an EmployeeModel
    private Date performedAt;
    private String employeeId;

    // Constructor
    public AuditLog(Date timestamp, String action, EmployeeModel performedBy, Date performedAt, String employeeId) {
        this.timestamp = timestamp;
        this.action = action;
        this.performedBy = performedBy;
        this.performedAt = performedAt;
        this.employeeId = employeeId;
    }

    // Getters and Setters
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public EmployeeModel getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(EmployeeModel performedBy) {
        this.performedBy = performedBy;
    }

    public Date getPerformedAt() {
        return performedAt;
    }

    public void setPerformedAt(Date performedAt) {
        this.performedAt = performedAt;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public String toString() {
        return "AuditLog{" +
                "timestamp=" + timestamp +
                ", action='" + action + '\'' +
                ", performedBy=" + performedBy +
                ", performedAt=" + performedAt +
                ", employeeId='" + employeeId + '\'' +
                '}';
    }
}
