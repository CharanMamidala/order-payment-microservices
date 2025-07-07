package com.example.payment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import java.time.LocalDateTime;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long orderId;
    private Double amount;
    private String status;
    
    @Column(nullable = false)
    private Long userId; // User who made the payment
    
    @Column(nullable = false)
    private Long adminId; // Admin/business receiving the payment
    
    @Column(nullable = false)
    private Long storeId; // Store the payment is for
    
    @Column
    private String paymentMethod; // CASH, CARD, ONLINE, etc.
    
    @Column
    private String transactionId; // External payment gateway transaction ID
    
    @Column
    private LocalDateTime paymentDate = LocalDateTime.now();
    
    @Column
    private String notes; // Payment notes

    public Payment() {}

    public Payment(Long id, Long orderId, Double amount, String status) {
        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
    }

    public Payment(Long id, Long orderId, Double amount, String status, Long userId, Long adminId, 
                   String paymentMethod, String transactionId) {
        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.userId = userId;
        this.adminId = adminId;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
    }

    public Payment(Long id, Long orderId, Double amount, String status, Long userId, Long adminId, Long storeId,
                   String paymentMethod, String transactionId) {
        this.id = id;
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.userId = userId;
        this.adminId = adminId;
        this.storeId = storeId;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }
    
    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
} 