package com.example.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    @Autowired
    private UserRepository userRepository;

    // Admin creates a new user
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        // In production, hash the password!
        return userRepository.save(user);
    }

    // Login endpoint
    @PostMapping("/login")
    public User login(@RequestBody User loginRequest) {
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(loginRequest.getPassword())) {
            User user = userOpt.get();
            user.setPassword(null); // Don't return password
            return user;
        }
        throw new RuntimeException("Invalid credentials");
    }

    // List all users (admin only, but no auth check here)
    @GetMapping
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        // Hide passwords in the response
        users.forEach(u -> u.setPassword(null));
        return users;
    }

    // Delete user by id
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    // Get user profile by id
    @GetMapping("/profile/{id}")
    public User getUserProfile(@PathVariable Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(null); // Don't return password
            return user;
        }
        throw new RuntimeException("User not found");
    }

    // Update user profile
    @PutMapping("/profile/{id}")
    public User updateUserProfile(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            // Update fields if provided
            if (updatedUser.getUsername() != null) {
                user.setUsername(updatedUser.getUsername());
            }
            if (updatedUser.getEmail() != null) {
                user.setEmail(updatedUser.getEmail());
            }
            if (updatedUser.getPhoneNumber() != null) {
                user.setPhoneNumber(updatedUser.getPhoneNumber());
            }
            if (updatedUser.getPassword() != null) {
                user.setPassword(updatedUser.getPassword());
            }
            
            User savedUser = userRepository.save(user);
            savedUser.setPassword(null); // Don't return password
            return savedUser;
        }
        throw new RuntimeException("User not found");
    }
} 