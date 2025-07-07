package com.example.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
@CrossOrigin
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    // Get all orders (for SUPER_ADMIN)
    @GetMapping
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // Get orders by user ID (for users to see their orders)
    @GetMapping("/user/{userId}")
    public List<Order> getOrdersByUser(@PathVariable Long userId) {
        return orderRepository.findByUserId(userId);
    }

    // Get orders by admin ID (for admins to see orders for their business)
    @GetMapping("/admin/{adminId}")
    public List<Order> getOrdersByAdmin(@PathVariable Long adminId) {
        return orderRepository.findByAdminId(adminId);
    }

    // Get orders by store ID (for SUPER_ADMIN to see orders for a specific store)
    @GetMapping("/store/{storeId}")
    public List<Order> getOrdersByStore(@PathVariable Long storeId) {
        return orderRepository.findByStoreId(storeId);
    }

    // Get orders by admin and store (for admins to see orders for their store)
    @GetMapping("/admin/{adminId}/store/{storeId}")
    public List<Order> getOrdersByAdminAndStore(@PathVariable Long adminId, @PathVariable Long storeId) {
        return orderRepository.findByStoreIdAndAdminId(storeId, adminId);
    }

    // Get orders by user and admin (for specific user orders under specific admin)
    @GetMapping("/user/{userId}/admin/{adminId}")
    public List<Order> getOrdersByUserAndAdmin(@PathVariable Long userId, @PathVariable Long adminId) {
        return orderRepository.findByUserIdAndAdminId(userId, adminId);
    }

    // Get orders by user and store (for users to see their orders in a specific store)
    @GetMapping("/user/{userId}/store/{storeId}")
    public List<Order> getOrdersByUserAndStore(@PathVariable Long userId, @PathVariable Long storeId) {
        return orderRepository.findByStoreIdAndUserId(storeId, userId);
    }

    // Get order by ID
    @GetMapping("/{id}")
    public Optional<Order> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id);
    }

    // Create new order
    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus("PENDING");
        order.setPaymentStatus("PENDING");
        return orderRepository.save(order);
    }

    // Update order status (for admins to update order status)
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setOrderStatus(status);
            
            // Set delivery date when order is confirmed
            if ("CONFIRMED".equals(status)) {
                order.setDeliveryDate(LocalDateTime.now().plusDays(3)); // 3 days delivery
            }
            
            Order savedOrder = orderRepository.save(order);
            return ResponseEntity.ok(savedOrder);
        }
        return ResponseEntity.notFound().build();
    }

    // Update payment status
    @PutMapping("/{id}/payment-status")
    public ResponseEntity<Order> updatePaymentStatus(@PathVariable Long id, @RequestParam String paymentStatus) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setPaymentStatus(paymentStatus);
            Order savedOrder = orderRepository.save(order);
            return ResponseEntity.ok(savedOrder);
        }
        return ResponseEntity.notFound().build();
    }

    // Update order details
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order orderDetails) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            
            if (orderDetails.getDescription() != null) {
                order.setDescription(orderDetails.getDescription());
            }
            if (orderDetails.getAmount() != null) {
                order.setAmount(orderDetails.getAmount());
            }
            if (orderDetails.getUserAddress() != null) {
                order.setUserAddress(orderDetails.getUserAddress());
            }
            if (orderDetails.getUserPhone() != null) {
                order.setUserPhone(orderDetails.getUserPhone());
            }
            if (orderDetails.getSpecialInstructions() != null) {
                order.setSpecialInstructions(orderDetails.getSpecialInstructions());
            }
            
            Order savedOrder = orderRepository.save(order);
            return ResponseEntity.ok(savedOrder);
        }
        return ResponseEntity.notFound().build();
    }

    // Cancel order
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long id) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setOrderStatus("CANCELLED");
            Order savedOrder = orderRepository.save(order);
            return ResponseEntity.ok(savedOrder);
        }
        return ResponseEntity.notFound().build();
    }

    // Delete order
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return ResponseEntity.ok("Order deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Order Service is healthy! Status: UP");
    }
} 