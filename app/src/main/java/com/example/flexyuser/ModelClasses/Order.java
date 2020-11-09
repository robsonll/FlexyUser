package com.example.flexyuser.ModelClasses;

import com.google.firebase.Timestamp;

import java.util.List;

public class Order
{
    private String id;
    private String businessId;
    private String userId;
    private String addressId;
    private String paymentMethod;
    private Timestamp date;
    private String status;
    private List<OrderItem> items;
    private Long totalPrice;
    private String obs;

    public Order(){}

    public Order(String id, String businessId, String userId, String addressId, String paymentMethod, Timestamp date, String status, List<OrderItem> items, Long totalPrice, String obs) {
        this.id = id;
        this.businessId = businessId;
        this.userId = userId;
        this.addressId = addressId;
        this.paymentMethod = paymentMethod;
        this.date = date;
        this.status = status;
        this.items = items;
        this.totalPrice = totalPrice;
        this.obs = obs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String user) {
        this.userId = user;
    }

    public String getUserAddress() {
        return addressId;
    }

    public void setUserAddress(String addressId) {
        this.addressId = addressId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }
}
