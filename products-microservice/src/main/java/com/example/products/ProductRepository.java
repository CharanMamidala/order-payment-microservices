package com.example.products;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByIsActive(Boolean isActive);
    List<Product> findByAdminId(Long adminId);
    List<Product> findByAdminIdAndIsActive(Long adminId, Boolean isActive);
    List<Product> findByStoreId(Long storeId);
    List<Product> findByStoreIdAndIsActive(Long storeId, Boolean isActive);
    List<Product> findByAdminIdAndStoreId(Long adminId, Long storeId);
    List<Product> findByAdminIdAndStoreIdAndIsActive(Long adminId, Long storeId, Boolean isActive);
} 