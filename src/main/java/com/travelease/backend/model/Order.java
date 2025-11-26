package com.travelease.backend.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", unique = true, nullable = false)
    private String orderId;

    @Column(name = "vehicle_name", nullable = false)
    private String vehicleName;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "payment_amount", nullable = false)
    private Double paymentAmount;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "booking_date")
    private LocalDateTime bookingDate;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "order_status")
    private String orderStatus; // BOOKING_CONFIRMED, VEHICLE_PREPARED, ON_THE_WAY, DELIVERED

    public Order() {
        this.bookingDate = LocalDateTime.now();
        this.orderStatus = "BOOKING_CONFIRMED"; // Default status
    }

    public Order(String orderId, String vehicleName, String username, String address, 
                 String mobileNumber, Double paymentAmount, String paymentStatus, 
                 LocalDate startDate, LocalDate endDate) {
        this.orderId = orderId;
        this.vehicleName = vehicleName;
        this.username = username;
        this.address = address;
        this.mobileNumber = mobileNumber;
        this.paymentAmount = paymentAmount;
        this.paymentStatus = paymentStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bookingDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
