package com.example.products;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByIsActive(Boolean isActive);
    Optional<Store> findByStoreName(String storeName);
    List<Store> findByStoreType(String storeType);
} 