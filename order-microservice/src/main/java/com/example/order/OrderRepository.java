package com.example.order;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByAdminId(Long adminId);
    List<Order> findByUserIdAndAdminId(Long userId, Long adminId);
    List<Order> findByStoreId(Long storeId);
    List<Order> findByStoreIdAndAdminId(Long storeId, Long adminId);
    List<Order> findByStoreIdAndUserId(Long storeId, Long userId);
} 