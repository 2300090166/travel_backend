package com.travelease.backend.controller;

import com.travelease.backend.dto.OrderRequest;
import com.travelease.backend.model.Order;
import com.travelease.backend.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Public endpoint for users to create orders
    @PostMapping("/api/orders")
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request) {
        Order order = new Order();
        order.setVehicleName(request.getVehicleName());
        order.setUsername(request.getUsername());
        order.setAddress(request.getAddress());
        order.setMobileNumber(request.getMobileNumber());
        order.setPaymentAmount(request.getPaymentAmount());
        order.setPaymentStatus(request.getPaymentStatus());
        order.setPaymentMethod(request.getPaymentMethod());
        
        // Parse dates
        if (request.getStartDate() != null && !request.getStartDate().isEmpty()) {
            order.setStartDate(parseDate(request.getStartDate()));
        }
        if (request.getEndDate() != null && !request.getEndDate().isEmpty()) {
            order.setEndDate(parseDate(request.getEndDate()));
        }
        
        Order saved = orderService.createOrder(order);
        return ResponseEntity.created(URI.create("/api/orders/" + saved.getId())).body(saved);
    }

    // Public endpoint for users to get their own orders
    @GetMapping("/api/orders/my-orders")
    public ResponseEntity<List<Order>> getMyOrders(@RequestParam String username) {
        List<Order> orders = orderService.getOrdersByUsername(username);
        return ResponseEntity.ok(orders);
    }

    // Public endpoint for tracking orders by orderId
    @GetMapping("/api/orders/{orderId}")
    public ResponseEntity<Order> trackOrder(@PathVariable String orderId) {
        Optional<Order> order = orderService.getOrderByOrderId(orderId);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Admin endpoints
    @GetMapping("/api/admin/orders")
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/api/admin/orders/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/api/admin/orders/order/{orderId}")
    public ResponseEntity<Order> getOrderByOrderId(@PathVariable String orderId) {
        Optional<Order> order = orderService.getOrderByOrderId(orderId);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/api/admin/orders/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody OrderRequest request) {
        Order orderDetails = new Order();
        orderDetails.setVehicleName(request.getVehicleName());
        orderDetails.setUsername(request.getUsername());
        orderDetails.setAddress(request.getAddress());
        orderDetails.setMobileNumber(request.getMobileNumber());
        orderDetails.setPaymentAmount(request.getPaymentAmount());
        orderDetails.setPaymentStatus(request.getPaymentStatus());
        orderDetails.setPaymentMethod(request.getPaymentMethod());
        orderDetails.setOrderStatus(request.getOrderStatus());
        
        if (request.getStartDate() != null && !request.getStartDate().isEmpty()) {
            orderDetails.setStartDate(parseDate(request.getStartDate()));
        }
        if (request.getEndDate() != null && !request.getEndDate().isEmpty()) {
            orderDetails.setEndDate(parseDate(request.getEndDate()));
        }
        
        Order updated = orderService.updateOrder(id, orderDetails);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/api/admin/orders/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        boolean deleted = orderService.deleteOrder(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    private LocalDate parseDate(String dateStr) {
        try {
            // Try ISO format first (YYYY-MM-DD)
            return LocalDate.parse(dateStr);
        } catch (Exception e) {
            try {
                // Try dd/MM/yyyy format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                return LocalDate.parse(dateStr, formatter);
            } catch (Exception ex) {
                // Return null if parsing fails
                return null;
            }
        }
    }
}
