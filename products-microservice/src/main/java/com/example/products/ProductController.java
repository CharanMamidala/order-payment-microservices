package com.example.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@CrossOrigin
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    // Get all active products (for users to browse)
    @GetMapping
    public List<Product> getAllActiveProducts() {
        return productRepository.findByIsActive(true);
    }

    // Get products by specific admin/business
    @GetMapping("/admin/{adminId}")
    public List<Product> getProductsByAdmin(@PathVariable Long adminId) {
        return productRepository.findByAdminIdAndIsActive(adminId, true);
    }

    // Get products by specific store
    @GetMapping("/store/{storeId}")
    public List<Product> getProductsByStore(@PathVariable Long storeId) {
        return productRepository.findByStoreIdAndIsActive(storeId, true);
    }

    // Get products by admin and store
    @GetMapping("/admin/{adminId}/store/{storeId}")
    public List<Product> getProductsByAdminAndStore(@PathVariable Long adminId, @PathVariable Long storeId) {
        return productRepository.findByAdminIdAndStoreIdAndIsActive(adminId, storeId, true);
    }

    // Get product by id
    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id) {
        return productRepository.findById(id);
    }

    // ADMIN: Add new product
    @PostMapping
    public Product addProduct(@RequestBody Product product, @RequestParam Long adminId, @RequestParam(required = false) Long storeId) {
        // Role check: ensure adminId belongs to an active ADMIN
        User admin = userRepository.findById(adminId)
                .filter(u -> Boolean.TRUE.equals(u.getIsActive()) && "ADMIN".equals(u.getUserType()))
                .orElseThrow(() -> new RuntimeException("Only active ADMINs can add products. Invalid adminId."));
        // If storeId is not provided, infer from admin
        if (storeId == null) {
            storeId = admin.getStoreId();
        }
        if (storeId == null) {
            throw new RuntimeException("Store ID is required and could not be inferred from admin.");
        }
        // Ensure admin is linked to the given store
        if (!storeId.equals(admin.getStoreId())) {
            throw new RuntimeException("Admin is not linked to the given store. Cannot add product.");
        }
        product.setAdminId(adminId);
        product.setStoreId(storeId);
        product.setIsActive(true);
        if (product.getStockQuantity() == null) {
            product.setStockQuantity(0);
        }
        return productRepository.save(product);
    }

    // ADMIN: Update product
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            
            if (updatedProduct.getName() != null) {
                product.setName(updatedProduct.getName());
            }
            if (updatedProduct.getCategory() != null) {
                product.setCategory(updatedProduct.getCategory());
            }
            if (updatedProduct.getPrice() != null) {
                product.setPrice(updatedProduct.getPrice());
            }
            if (updatedProduct.getImageUrl() != null) {
                product.setImageUrl(updatedProduct.getImageUrl());
            }
            if (updatedProduct.getDescription() != null) {
                product.setDescription(updatedProduct.getDescription());
            }
            if (updatedProduct.getStockQuantity() != null) {
                product.setStockQuantity(updatedProduct.getStockQuantity());
            }
            
            return productRepository.save(product);
        }
        throw new RuntimeException("Product not found");
    }

    // ADMIN: Deactivate product
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivateProduct(@PathVariable Long id) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setIsActive(false);
            productRepository.save(product);
            return ResponseEntity.ok("Product deactivated successfully");
        }
        return ResponseEntity.notFound().build();
    }

    // ADMIN: Activate product
    @PutMapping("/{id}/activate")
    public ResponseEntity<String> activateProduct(@PathVariable Long id) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setIsActive(true);
            productRepository.save(product);
            return ResponseEntity.ok("Product activated successfully");
        }
        return ResponseEntity.notFound().build();
    }

    // ADMIN: Delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.ok("Product deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Product Service is healthy! Status: UP");
    }
} 