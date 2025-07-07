package com.example.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/payments")
@CrossOrigin
public class PaymentController {
    @Autowired
    private PaymentRepository paymentRepository;

    // Get all payments (for SUPER_ADMIN)
    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // Get payments by user ID (for users to see their payments)
    @GetMapping("/user/{userId}")
    public List<Payment> getPaymentsByUser(@PathVariable Long userId) {
        return paymentRepository.findByUserId(userId);
    }

    // Get payments by admin ID (for admins to see payments for their business)
    @GetMapping("/admin/{adminId}")
    public List<Payment> getPaymentsByAdmin(@PathVariable Long adminId) {
        return paymentRepository.findByAdminId(adminId);
    }

    // Get payments by store ID (for SUPER_ADMIN to see payments for a specific store)
    @GetMapping("/store/{storeId}")
    public List<Payment> getPaymentsByStore(@PathVariable Long storeId) {
        return paymentRepository.findByStoreId(storeId);
    }

    // Get payments by admin and store (for admins to see payments for their store)
    @GetMapping("/admin/{adminId}/store/{storeId}")
    public List<Payment> getPaymentsByAdminAndStore(@PathVariable Long adminId, @PathVariable Long storeId) {
        return paymentRepository.findByStoreIdAndAdminId(storeId, adminId);
    }

    // Get payments by user and store (for users to see their payments in a specific store)
    @GetMapping("/user/{userId}/store/{storeId}")
    public List<Payment> getPaymentsByUserAndStore(@PathVariable Long userId, @PathVariable Long storeId) {
        return paymentRepository.findByStoreIdAndUserId(storeId, userId);
    }

    // Get payments by order ID
    @GetMapping("/order/{orderId}")
    public List<Payment> getPaymentsByOrder(@PathVariable Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    // Get payment by ID
    @GetMapping("/{id}")
    public Optional<Payment> getPaymentById(@PathVariable Long id) {
        return paymentRepository.findById(id);
    }

    // Create new payment
    @PostMapping
    public Payment createPayment(@RequestBody Payment payment) {
        payment.setPaymentDate(LocalDateTime.now());
        if (payment.getStatus() == null) {
            payment.setStatus("PENDING");
        }
        return paymentRepository.save(payment);
    }

    // Update payment status
    @PutMapping("/{id}/status")
    public ResponseEntity<Payment> updatePaymentStatus(@PathVariable Long id, @RequestParam String status) {
        Optional<Payment> paymentOpt = paymentRepository.findById(id);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setStatus(status);
            Payment savedPayment = paymentRepository.save(payment);
            return ResponseEntity.ok(savedPayment);
        }
        return ResponseEntity.notFound().build();
    }

    // Update payment details
    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Long id, @RequestBody Payment paymentDetails) {
        Optional<Payment> paymentOpt = paymentRepository.findById(id);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            
            if (paymentDetails.getAmount() != null) {
                payment.setAmount(paymentDetails.getAmount());
            }
            if (paymentDetails.getPaymentMethod() != null) {
                payment.setPaymentMethod(paymentDetails.getPaymentMethod());
            }
            if (paymentDetails.getTransactionId() != null) {
                payment.setTransactionId(paymentDetails.getTransactionId());
            }
            if (paymentDetails.getNotes() != null) {
                payment.setNotes(paymentDetails.getNotes());
            }
            
            Payment savedPayment = paymentRepository.save(payment);
            return ResponseEntity.ok(savedPayment);
        }
        return ResponseEntity.notFound().build();
    }

    // Process payment (simulate payment processing)
    @PostMapping("/{id}/process")
    public ResponseEntity<Payment> processPayment(@PathVariable Long id, @RequestParam String paymentMethod) {
        Optional<Payment> paymentOpt = paymentRepository.findById(id);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setPaymentMethod(paymentMethod);
            payment.setStatus("PAID");
            payment.setTransactionId("TXN_" + System.currentTimeMillis()); // Generate transaction ID
            payment.setPaymentDate(LocalDateTime.now());
            
            Payment savedPayment = paymentRepository.save(payment);
            return ResponseEntity.ok(savedPayment);
        }
        return ResponseEntity.notFound().build();
    }

    // Refund payment
    @PostMapping("/{id}/refund")
    public ResponseEntity<Payment> refundPayment(@PathVariable Long id, @RequestParam String reason) {
        Optional<Payment> paymentOpt = paymentRepository.findById(id);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setStatus("REFUNDED");
            payment.setNotes("Refund reason: " + reason);
            
            Payment savedPayment = paymentRepository.save(payment);
            return ResponseEntity.ok(savedPayment);
        }
        return ResponseEntity.notFound().build();
    }

    // Delete payment
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePayment(@PathVariable Long id) {
        if (paymentRepository.existsById(id)) {
            paymentRepository.deleteById(id);
            return ResponseEntity.ok("Payment deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Payment Service is healthy! Status: UP");
    }
} 