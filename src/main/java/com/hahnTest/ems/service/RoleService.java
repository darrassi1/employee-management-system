package com.hahnTest.ems.service;

import com.hahnTest.ems.model.Role;
import com.hahnTest.ems.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    // Get all roles
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // Get role by name
    public Role getRoleByName(String roleName) {
        return roleRepository.findByName(roleName);
    }

    // Save a role (useful for adding new roles or updating them)
    public Role save(Role role) {
        return roleRepository.save(role);
    }
}
