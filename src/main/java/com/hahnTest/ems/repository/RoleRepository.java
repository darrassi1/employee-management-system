package com.hahnTest.ems.repository;



import com.hahnTest.ems.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    // Additional queries for roles if needed
	  // Find a role by name
    Role findByName(String name);
}
