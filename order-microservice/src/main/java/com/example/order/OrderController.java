package com.example.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Order> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Order Service is healthy! Status: UP");
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        // Save the order first
        Order savedOrder = orderRepository.save(order);
        
        // Automatically create a payment for this order
        try {
            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setOrderId(savedOrder.getId());
            paymentRequest.setAmount(savedOrder.getAmount());
            paymentRequest.setStatus("PENDING");
            
            ResponseEntity<PaymentResponse> response = restTemplate.postForEntity(
                "http://localhost:8080/payments",
                paymentRequest,
                PaymentResponse.class
            );
            
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Payment created for order: " + savedOrder.getId());
            }
        } catch (Exception e) {
            System.err.println("Failed to create payment for order: " + savedOrder.getId());
            e.printStackTrace();
        }
        
        return savedOrder;
    }
    
    // Inner classes for payment communication
    public static class PaymentRequest {
        private Long orderId;
        private Double amount;
        private String status;
        
        // Getters and setters
        public Long getOrderId() { return orderId; }
        public void setOrderId(Long orderId) { this.orderId = orderId; }
        public Double getAmount() { return amount; }
        public void setAmount(Double amount) { this.amount = amount; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
    
    public static class PaymentResponse {
        private Long id;
        private Long orderId;
        private Double amount;
        private String status;
        
        // Getters and setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getOrderId() { return orderId; }
        public void setOrderId(Long orderId) { this.orderId = orderId; }
        public Double getAmount() { return amount; }
        public void setAmount(Double amount) { this.amount = amount; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
} 