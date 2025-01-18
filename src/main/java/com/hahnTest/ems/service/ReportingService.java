package com.hahnTest.ems.service;

import com.hahnTest.ems.model.Employee;
import com.hahnTest.ems.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportingService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Map<String, Long> generateDepartmentSummary() {
        return employeeRepository.findAll().stream()
            .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()));
    }

    public Map<String, Long> generateEmployeeStatusSummary() {
        return employeeRepository.findAll().stream()
            .collect(Collectors.groupingBy(Employee::getEmploymentStatus, Collectors.counting()));
    }

    public List<Employee> generateHiringTrendsReport(LocalDate startDate, LocalDate endDate) {
        return employeeRepository.findByHireDateBetween(startDate, endDate);
    }
}