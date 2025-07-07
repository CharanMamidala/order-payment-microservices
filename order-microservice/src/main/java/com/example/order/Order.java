package com.example.order;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String description;
    private Double amount;
    
    @Column(nullable = false)
    private Long userId; // User who placed the order
    
    @Column(nullable = false)
    private Long adminId; // Admin/business the order is for
    
    @Column(nullable = false)
    private Long storeId; // Store the order is for
    
    @Column
    private String userAddress; // Shipping address
    
    @Column
    private String userPhone; // User's phone for delivery
    
    @Column
    private String orderStatus = "PENDING"; // PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    
    @Column
    private String paymentStatus = "PENDING"; // PENDING, PAID, FAILED
    
    @Column
    private LocalDateTime orderDate = LocalDateTime.now();
    
    @Column
    private LocalDateTime deliveryDate; // Expected delivery date
    
    @Column
    private String specialInstructions; // Special delivery instructions

    public Order() {}

    public Order(Long id, String description, Double amount) {
        this.id = id;
        this.description = description;
        this.amount = amount;
    }

    public Order(Long id, String description, Double amount, Long userId, Long adminId, 
                 String userAddress, String userPhone) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.userId = userId;
        this.adminId = adminId;
        this.userAddress = userAddress;
        this.userPhone = userPhone;
    }

    public Order(Long id, String description, Double amount, Long userId, Long adminId, Long storeId,
                 String userAddress, String userPhone) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.userId = userId;
        this.adminId = adminId;
        this.storeId = storeId;
        this.userAddress = userAddress;
        this.userPhone = userPhone;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }
    
    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }
    
    public String getUserAddress() { return userAddress; }
    public void setUserAddress(String userAddress) { this.userAddress = userAddress; }
    
    public String getUserPhone() { return userPhone; }
    public void setUserPhone(String userPhone) { this.userPhone = userPhone; }
    
    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }
    
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    
    public LocalDateTime getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(LocalDateTime deliveryDate) { this.deliveryDate = deliveryDate; }
    
    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }
} 