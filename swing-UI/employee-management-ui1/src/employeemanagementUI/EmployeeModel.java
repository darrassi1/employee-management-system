package employeemanagementUI;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EmployeeModel {
    private Long id;
    private String employeeId;
    private String fullName;
    private String jobTitle;
    private String department;
    private String hireDate;
    private String employmentStatus;
    private String contactInfo;
    private String address;

    // Default constructor
    public EmployeeModel() {
    }

    // Parameterized constructor
    @JsonCreator
    public EmployeeModel(
        @JsonProperty("id") Long id,
        @JsonProperty("employeeId") String employeeId,
        @JsonProperty("fullName") String fullName,
        @JsonProperty("jobTitle") String jobTitle,
        @JsonProperty("department") String department,
        @JsonProperty("hireDate") String hireDate,
        @JsonProperty("employmentStatus") String employmentStatus,
        @JsonProperty("contactInfo") String contactInfo,
        @JsonProperty("address") String address) {
        this.id = id;
        this.employeeId = employeeId;
        this.fullName = fullName;
        this.jobTitle = jobTitle;
        this.department = department;
        this.hireDate = hireDate;
        this.employmentStatus = employmentStatus;
        this.contactInfo = contactInfo;
        this.address = address;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getHireDate() {
        return hireDate;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}