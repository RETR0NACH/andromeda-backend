package com.andromda.shop.service;

import com.andromda.shop.dto.OrderItemRequest;
import com.andromda.shop.dto.OrderRequest;
import com.andromda.shop.model.Order;
import com.andromda.shop.model.OrderItem;
import com.andromda.shop.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order createOrder(Long userId, OrderRequest request) {

        Order order = new Order();
        order.setUserId(userId);
        order.setTotal(request.getTotal());

        if (request.getItems() != null) {
            for (OrderItemRequest itemReq : request.getItems()) {

                OrderItem item = new OrderItem();
                item.setProductId(itemReq.getProductId());
                item.setQuantity(itemReq.getQuantity());
                item.setUnitPrice(itemReq.getUnitPrice());

                order.addItem(item);
            }
        }

        return orderRepository.save(order);
    }

    public java.util.List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    public java.util.Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public java.util.List<Order> findByUserId(Long userId) {
        return orderRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
    }
}