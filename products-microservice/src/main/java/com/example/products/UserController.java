package com.example.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    @Autowired
    private UserRepository userRepository;

    // Login endpoint
    @PostMapping("/login")
    public User login(@RequestBody User loginRequest) {
        Optional<User> userOpt = userRepository.findByUsernameAndIsActive(loginRequest.getUsername(), true);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(loginRequest.getPassword())) {
            User user = userOpt.get();
            user.setPassword(null); // Don't return password
            return user;
        }
        throw new RuntimeException("Invalid credentials");
    }

    // SUPER_ADMIN: Create new admin for a specific store
    @PostMapping("/admin")
    public User createAdmin(@RequestBody User adminUser) {
        // Validate that the request is from a SUPER_ADMIN (in real app, check session/token)
        if (adminUser.getStoreId() == null) {
            throw new RuntimeException("Store ID is required");
        }
        adminUser.setUserType("ADMIN");
        adminUser.setIsActive(true);
        User savedAdmin = userRepository.save(adminUser);
        savedAdmin.setPassword(null);
        return savedAdmin;
    }

    // SUPER_ADMIN: Create new user under a specific admin in a store
    @PostMapping("/user")
    public User createUser(@RequestBody User user, @RequestParam Long adminId) {
        // Validate that adminId exists and is an ADMIN
        Optional<User> adminOpt = userRepository.findById(adminId);
        if (!adminOpt.isPresent() || !"ADMIN".equals(adminOpt.get().getUserType())) {
            throw new RuntimeException("Invalid admin ID");
        }
        
        if (user.getStoreId() == null) {
            throw new RuntimeException("Store ID is required");
        }
        
        // Validate that admin is in the same store
        if (!user.getStoreId().equals(adminOpt.get().getStoreId())) {
            throw new RuntimeException("Store mismatch between user and admin");
        }
        
        user.setUserType("USER");
        user.setAdminId(adminId);
        user.setIsActive(true);
        User savedUser = userRepository.save(user);
        savedUser.setPassword(null);
        return savedUser;
    }

    // SUPER_ADMIN: List all admins
    @GetMapping("/admins")
    public List<User> getAllAdmins() {
        List<User> admins = userRepository.findByUserTypeAndIsActive("ADMIN", true);
        admins.forEach(admin -> admin.setPassword(null));
        return admins;
    }

    // SUPER_ADMIN: List all admins for a specific store
    @GetMapping("/store/{storeId}/admins")
    public List<User> getAdminsByStore(@PathVariable Long storeId) {
        List<User> admins = userRepository.findByStoreIdAndUserTypeAndIsActive(storeId, "ADMIN", true);
        admins.forEach(admin -> admin.setPassword(null));
        return admins;
    }

    // ADMIN: List all users under this admin
    @GetMapping("/admin/{adminId}/users")
    public List<User> getUsersByAdmin(@PathVariable Long adminId) {
        List<User> users = userRepository.findByAdminIdAndIsActive(adminId, true);
        users.forEach(user -> user.setPassword(null));
        return users;
    }

    // SUPER_ADMIN: List all users (across all stores)
    @GetMapping("/all")
    public List<User> getAllUsers() {
        List<User> users = userRepository.findByUserTypeAndIsActive("USER", true);
        users.forEach(user -> user.setPassword(null));
        return users;
    }

    // SUPER_ADMIN: List all users for a specific store
    @GetMapping("/store/{storeId}/users")
    public List<User> getUsersByStore(@PathVariable Long storeId) {
        List<User> users = userRepository.findByStoreIdAndUserTypeAndIsActive(storeId, "USER", true);
        users.forEach(user -> user.setPassword(null));
        return users;
    }

    // Get user profile by id
    @GetMapping("/profile/{id}")
    public User getUserProfile(@PathVariable Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(null);
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
            if (updatedUser.getAddress() != null) {
                user.setAddress(updatedUser.getAddress());
            }
            if (updatedUser.getBusinessName() != null) {
                user.setBusinessName(updatedUser.getBusinessName());
            }
            if (updatedUser.getBusinessType() != null) {
                user.setBusinessType(updatedUser.getBusinessType());
            }
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().trim().isEmpty()) {
                user.setPassword(updatedUser.getPassword());
            }
            
            User savedUser = userRepository.save(user);
            savedUser.setPassword(null);
            return savedUser;
        }
        throw new RuntimeException("User not found");
    }

    // Deactivate user (soft delete)
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivateUser(@PathVariable Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setIsActive(false);
            userRepository.save(user);
            return ResponseEntity.ok("User deactivated successfully");
        }
        return ResponseEntity.notFound().build();
    }

    // Activate user
    @PutMapping("/{id}/activate")
    public ResponseEntity<String> activateUser(@PathVariable Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setIsActive(true);
            userRepository.save(user);
            return ResponseEntity.ok("User activated successfully");
        }
        return ResponseEntity.notFound().build();
    }

    // Delete user (hard delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok("User deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }

    // Get admin details by ID
    @GetMapping("/admin/{id}")
    public User getAdminById(@PathVariable Long id) {
        Optional<User> adminOpt = userRepository.findById(id);
        if (adminOpt.isPresent() && "ADMIN".equals(adminOpt.get().getUserType())) {
            User admin = adminOpt.get();
            admin.setPassword(null);
            return admin;
        }
        throw new RuntimeException("Admin not found");
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("User Service is healthy! Status: UP");
    }
} 