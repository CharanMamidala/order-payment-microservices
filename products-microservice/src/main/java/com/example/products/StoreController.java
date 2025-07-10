package com.example.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stores")
@CrossOrigin
public class StoreController {
    @Autowired
    private StoreRepository storeRepository;
    
    @Autowired
    private UserRepository userRepository;

    // SUPER_ADMIN: Create new store
    @PostMapping
    public Store createStore(@RequestBody Store store) {
        // Set default values for optional fields
        if (store.getStoreType() == null || store.getStoreType().trim().isEmpty()) {
            store.setStoreType("General");
        }
        if (store.getDescription() == null || store.getDescription().trim().isEmpty()) {
            store.setDescription("Store created by super admin");
        }
        if (store.getPhoneNumber() == null || store.getPhoneNumber().trim().isEmpty()) {
            store.setPhoneNumber("N/A");
        }
        if (store.getEmail() == null || store.getEmail().trim().isEmpty()) {
            store.setEmail("store@example.com");
        }
        
        store.setCreatedAt(LocalDateTime.now());
        store.setUpdatedAt(LocalDateTime.now());
        store.setIsActive(true);
        return storeRepository.save(store);
    }

    // Get all active stores
    @GetMapping
    public List<Store> getAllActiveStores() {
        return storeRepository.findByIsActive(true);
    }

    // Get all stores (including inactive)
    @GetMapping("/all")
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    // Get store by ID
    @GetMapping("/{id}")
    public Optional<Store> getStoreById(@PathVariable Long id) {
        return storeRepository.findById(id);
    }

    // Update store
    @PutMapping("/{id}")
    public Store updateStore(@PathVariable Long id, @RequestBody Store updatedStore) {
        Optional<Store> storeOpt = storeRepository.findById(id);
        if (storeOpt.isPresent()) {
            Store store = storeOpt.get();
            
            if (updatedStore.getStoreName() != null) {
                store.setStoreName(updatedStore.getStoreName());
            }
            if (updatedStore.getStoreType() != null) {
                store.setStoreType(updatedStore.getStoreType());
            }
            if (updatedStore.getDescription() != null) {
                store.setDescription(updatedStore.getDescription());
            }
            if (updatedStore.getAddress() != null) {
                store.setAddress(updatedStore.getAddress());
            }
            if (updatedStore.getPhoneNumber() != null) {
                store.setPhoneNumber(updatedStore.getPhoneNumber());
            }
            if (updatedStore.getEmail() != null) {
                store.setEmail(updatedStore.getEmail());
            }
            
            store.setUpdatedAt(LocalDateTime.now());
            return storeRepository.save(store);
        }
        throw new RuntimeException("Store not found");
    }

    // Deactivate store
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivateStore(@PathVariable Long id) {
        Optional<Store> storeOpt = storeRepository.findById(id);
        if (storeOpt.isPresent()) {
            Store store = storeOpt.get();
            store.setIsActive(false);
            store.setUpdatedAt(LocalDateTime.now());
            storeRepository.save(store);
            return ResponseEntity.ok("Store deactivated successfully");
        }
        return ResponseEntity.notFound().build();
    }

    // Activate store
    @PutMapping("/{id}/activate")
    public ResponseEntity<String> activateStore(@PathVariable Long id) {
        Optional<Store> storeOpt = storeRepository.findById(id);
        if (storeOpt.isPresent()) {
            Store store = storeOpt.get();
            store.setIsActive(true);
            store.setUpdatedAt(LocalDateTime.now());
            storeRepository.save(store);
            return ResponseEntity.ok("Store activated successfully");
        }
        return ResponseEntity.notFound().build();
    }

    // Delete store
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStore(@PathVariable Long id) {
        if (storeRepository.existsById(id)) {
            storeRepository.deleteById(id);
            return ResponseEntity.ok("Store deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }

    // Get users by store ID
    @GetMapping("/{storeId}/users")
    public List<User> getUsersByStore(@PathVariable Long storeId) {
        List<User> users = userRepository.findByStoreIdAndIsActive(storeId, true);
        users.forEach(user -> user.setPassword(null));
        return users;
    }

    // Get admins by store ID
    @GetMapping("/{storeId}/admins")
    public List<User> getAdminsByStore(@PathVariable Long storeId) {
        List<User> admins = userRepository.findByStoreIdAndUserTypeAndIsActive(storeId, "ADMIN", true);
        admins.forEach(admin -> admin.setPassword(null));
        return admins;
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Store Service is healthy! Status: UP");
    }

    // SUPER_ADMIN: Create new store and assign admin in one step
    @PostMapping("/with-admin")
    public ResponseEntity<?> createStoreWithAdmin(@RequestBody Store store, @RequestParam String adminUsername, @RequestParam String adminPassword, @RequestParam String adminEmail) {
        // Create the store
        if (store.getStoreType() == null || store.getStoreType().trim().isEmpty()) {
            store.setStoreType("General");
        }
        if (store.getDescription() == null || store.getDescription().trim().isEmpty()) {
            store.setDescription("Store created by super admin");
        }
        if (store.getPhoneNumber() == null || store.getPhoneNumber().trim().isEmpty()) {
            store.setPhoneNumber("N/A");
        }
        if (store.getEmail() == null || store.getEmail().trim().isEmpty()) {
            store.setEmail("store@example.com");
        }
        store.setCreatedAt(java.time.LocalDateTime.now());
        store.setUpdatedAt(java.time.LocalDateTime.now());
        store.setIsActive(true);
        Store savedStore = storeRepository.save(store);

        // Create the admin for this store
        User admin = new User();
        admin.setUsername(adminUsername);
        admin.setPassword(adminPassword);
        admin.setUserType("ADMIN");
        admin.setEmail(adminEmail);
        admin.setIsActive(true);
        admin.setStoreId(savedStore.getId());
        User savedAdmin = userRepository.save(admin);
        savedAdmin.setPassword(null); // Don't return password

        // Return both store and admin
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("store", savedStore);
        response.put("admin", savedAdmin);
        return ResponseEntity.ok(response);
    }
} 