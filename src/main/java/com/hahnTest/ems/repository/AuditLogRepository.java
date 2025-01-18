package com.hahnTest.ems.repository;



import com.hahnTest.ems.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    // Add custom queries for audit logs if needed
}
