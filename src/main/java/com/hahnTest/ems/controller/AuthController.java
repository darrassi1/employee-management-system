package com.hahnTest.ems.controller;

import com.hahnTest.ems.model.Role;
import com.hahnTest.ems.model.User;
import com.hahnTest.ems.service.RoleService;
import com.hahnTest.ems.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Registration endpoint
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        try {
            if (userService.getUserByUsername(user.getUsername()) != null) {
                return new ResponseEntity<>("Username is already taken.", HttpStatus.BAD_REQUEST);
            }
        } catch (RuntimeException e) {
            // This means the user does not exist, which is expected
        }

        // Encrypt password
        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);

        // Assign default role
        Role userRole = roleService.getRoleByName("ROLE_HR");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("USER");
            roleService.save(userRole);
        }
        user.setRoles(Collections.singleton(userRole));

        userService.save(user);
        return new ResponseEntity<>("User registered successfully.", HttpStatus.CREATED);
    }

    // Login endpoint handled by Spring Security
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        User existingUser = userService.getUserByUsername(user.getUsername());
        if (existingUser == null || !passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            return new ResponseEntity<>("Invalid username or password.", HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>("Login successful", HttpStatus.OK);
    }

    // Logout endpoint
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        // Invalidate the session
        request.getSession().invalidate();

        // Clear authentication from SecurityContext
        SecurityContextHolder.clearContext();

        return new ResponseEntity<>("Logout successful.", HttpStatus.OK);
    }
}