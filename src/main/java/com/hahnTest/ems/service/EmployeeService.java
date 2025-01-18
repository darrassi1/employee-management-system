package com.hahnTest.ems.service;

import com.hahnTest.ems.exception.ResourceNotFoundException;
import com.hahnTest.ems.model.AuditLog;
import com.hahnTest.ems.model.Employee;
import com.hahnTest.ems.model.Role;
import com.hahnTest.ems.model.User;
import com.hahnTest.ems.repository.EmployeeRepository;
import com.hahnTest.ems.repository.RoleRepository;
import com.hahnTest.ems.repository.UserRepository;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;

import com.hahnTest.ems.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    // Fetch all employees
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Search employees by query (e.g., name, job title, etc.)
    public List<Employee> searchEmployees(String query) {
        return employeeRepository.findByFullName(query); // Example search method
    }
    public List<Employee> searchEmployees(String query, String department, String employmentStatus, LocalDate fromDate, LocalDate toDate) {
        Specification<Employee> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (query != null && !query.isEmpty()) {
                String likePattern = "%" + query.toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("employeeId")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("jobTitle")), likePattern)
                ));
            }

            if (department != null && !department.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("department"), department));
            }

            if (employmentStatus != null && !employmentStatus.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("employmentStatus"), employmentStatus));
            }

            if (fromDate != null && toDate != null) {
                predicates.add(criteriaBuilder.between(root.get("hireDate"), fromDate, toDate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return employeeRepository.findAll(spec);
    }

    // Add new employee
    @Transactional
    public Employee addEmployee(Employee employee, User user) {
        // Save employee
        Employee savedEmployee = employeeRepository.save(employee);

        // Log the action in audit log
        auditLogRepository.save(new AuditLog("Employee added", user.getUsername(), savedEmployee.getId()));

        return savedEmployee;
    }


    public Employee updateEmployee(String employeeId, Employee updatedEmployee, User user) {
        // Fetch the employee by employeeId (String) instead of id (Long)
        Employee existingEmployee = employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        // Update the employee fields
        existingEmployee.setFullName(updatedEmployee.getFullName());
        existingEmployee.setJobTitle(updatedEmployee.getJobTitle());
        existingEmployee.setDepartment(updatedEmployee.getDepartment());
        existingEmployee.setHireDate(updatedEmployee.getHireDate());
        existingEmployee.setEmploymentStatus(updatedEmployee.getEmploymentStatus());
        existingEmployee.setContactInfo(updatedEmployee.getContactInfo());
        existingEmployee.setAddress(updatedEmployee.getAddress());
        
        // Save the updated employee back to the repository
        return employeeRepository.save(existingEmployee);
    }
    // Delete employee
    @Transactional
    public void deleteEmployee(Long id, User user) {
        Optional<Employee> employeeOpt = employeeRepository.findById(id);
        if (employeeOpt.isPresent()) {
            employeeRepository.deleteById(id);

            // Log the action in audit log
            auditLogRepository.save(new AuditLog("Employee deleted", user.getUsername(), id));
        } else {
            throw new RuntimeException("Employee not found with id " + id);
        }
    }

    // Assign role to a user
    public User assignRoleToUser(Long userId, Long roleId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Role> roleOpt = roleRepository.findById(roleId);

        if (userOpt.isPresent() && roleOpt.isPresent()) {
            User user = userOpt.get();
            Role role = roleOpt.get();

            user.getRoles().add(role); // Add the role to the user
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User or Role not found");
        }
    }

    // Get user by username
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username " + username));
    }
}
