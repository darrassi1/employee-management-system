package com.hahnTest.ems.repository;



import com.hahnTest.ems.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmployeeId(String employeeId);
    List<Employee> findByFullName(String fullName);
	List<Employee> findByHireDateBetween(LocalDate startDate, LocalDate endDate);
}
