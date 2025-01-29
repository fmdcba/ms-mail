package com.mindhub.ms_mail.dtos;

import com.mindhub.ms_mail.utils.OrderStatus;

import java.util.List;

public class OrderEntityDTO {

    private Long id;
    private Long userId;
    private List<OrderItemDTO> products; // Will be empty or null
    private OrderStatus status;

    // Constructors
    public OrderEntityDTO() {}

    public OrderEntityDTO(Long id, Long userId, List<OrderItemDTO> products, OrderStatus status) {
        this.id = id;
        this.userId = userId;
        this.products = products;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public List<OrderItemDTO> getProducts() { return products; }
    public OrderStatus getStatus() { return status; }

    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setProducts(List<OrderItemDTO> products) { this.products = products; }
    public void setStatus(OrderStatus status) { this.status = status; }
}