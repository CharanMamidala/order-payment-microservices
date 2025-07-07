package com.example.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUserId(Long userId);
    List<Payment> findByAdminId(Long adminId);
    List<Payment> findByUserIdAndAdminId(Long userId, Long adminId);
    List<Payment> findByOrderId(Long orderId);
    List<Payment> findByStoreId(Long storeId);
    List<Payment> findByStoreIdAndAdminId(Long storeId, Long adminId);
    List<Payment> findByStoreIdAndUserId(Long storeId, Long userId);
} 