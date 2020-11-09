package com.example.flexyuser.ModelClasses;

import java.util.List;

public class OrderItem {

    private String id;
    private List<Product> products;
    private Long itemPrice;

    public OrderItem(){}

    public OrderItem(String id, List<Product> products, Long itemPrice) {
        this.id = id;
        this.products = products;
        this.itemPrice = itemPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Long getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Long itemPrice) {
        this.itemPrice = itemPrice;
    }
}
