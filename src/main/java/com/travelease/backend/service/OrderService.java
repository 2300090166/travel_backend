package com.travelease.backend.service;

import com.travelease.backend.model.Order;
import com.travelease.backend.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByBookingDateDesc();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Optional<Order> getOrderByOrderId(String orderId) {
        return orderRepository.findByOrderId(orderId);
    }

    public List<Order> getOrdersByUsername(String username) {
        return orderRepository.findByUsername(username);
    }

    public Order createOrder(Order order) {
        if (order.getOrderId() == null || order.getOrderId().isEmpty()) {
            order.setOrderId(generateOrderId());
        }
        if (order.getBookingDate() == null) {
            order.setBookingDate(LocalDateTime.now());
        }
        if (order.getPaymentStatus() == null || order.getPaymentStatus().isEmpty()) {
            order.setPaymentStatus("PENDING");
        }
        return orderRepository.save(order);
    }

    public Order updateOrder(Long id, Order orderDetails) {
        Optional<Order> existingOrder = orderRepository.findById(id);
        if (existingOrder.isPresent()) {
            Order order = existingOrder.get();
            if (orderDetails.getVehicleName() != null) {
                order.setVehicleName(orderDetails.getVehicleName());
            }
            if (orderDetails.getUsername() != null) {
                order.setUsername(orderDetails.getUsername());
            }
            if (orderDetails.getAddress() != null) {
                order.setAddress(orderDetails.getAddress());
            }
            if (orderDetails.getMobileNumber() != null) {
                order.setMobileNumber(orderDetails.getMobileNumber());
            }
            if (orderDetails.getPaymentAmount() != null) {
                order.setPaymentAmount(orderDetails.getPaymentAmount());
            }
            if (orderDetails.getPaymentStatus() != null) {
                order.setPaymentStatus(orderDetails.getPaymentStatus());
            }
            if (orderDetails.getStartDate() != null) {
                order.setStartDate(orderDetails.getStartDate());
            }
            if (orderDetails.getEndDate() != null) {
                order.setEndDate(orderDetails.getEndDate());
            }
            if (orderDetails.getPaymentMethod() != null) {
                order.setPaymentMethod(orderDetails.getPaymentMethod());
            }
            if (orderDetails.getOrderStatus() != null) {
                order.setOrderStatus(orderDetails.getOrderStatus());
            }
            return orderRepository.save(order);
        }
        return null;
    }

    public boolean deleteOrder(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private String generateOrderId() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        long count = orderRepository.count();
        return "ORD" + timestamp + String.format("%04d", count + 1);
    }
}
