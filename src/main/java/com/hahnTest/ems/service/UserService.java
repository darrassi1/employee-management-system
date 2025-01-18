package com.hahnTest.ems.service;

import com.hahnTest.ems.model.User;
import com.hahnTest.ems.repository.UserRepository;
import com.hahnTest.ems.security.CustomUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new CustomUserDetails(user);
    }

    // Get user by username and throw an exception if not found
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username " + username));
    }

    // Save a new user or update an existing user
    public User save(User user) {
        return userRepository.save(user);
    }

    // Delete a user by ID
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Check if a user exists by username
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }

    // Fetch all users
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }
}
