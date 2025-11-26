package com.andromda.shop.dto;

import java.math.BigDecimal;
import java.util.List;

public class OrderRequest {
    // No confiamos en que el cliente nos diga userId; lo obtendremos del token en el backend.
    private BigDecimal total;
    private List<OrderItemRequest> items;
    private String shippingAddress; // opcional

    public OrderRequest() {}

    // getters/setters
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public List<OrderItemRequest> getItems() { return items; }
    public void setItems(List<OrderItemRequest> items) { this.items = items; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
}
