package com.andromda.shop.controller;

import com.andromda.shop.dto.OrderRequest;
import com.andromda.shop.dto.OrderResponse;
import com.andromda.shop.model.Order;
import com.andromda.shop.model.Usuario;
import com.andromda.shop.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Crear pedido (cliente autenticado)
    @PostMapping
    public ResponseEntity<?> createOrder(
            @RequestBody OrderRequest request,
            Authentication authentication
    ) {
        Long userId = extractUserIdFromAuth(authentication);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Usuario no autenticado");
        }

        Order created = orderService.createOrder(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    // Obtener todos (solo admin)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllOrders() {
        var list = orderService.findAllOrders()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // Obtener detalle (admin o dueño del pedido)
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        var opt = orderService.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        Order order = opt.get();

        Long userId = extractUserIdFromAuth(authentication);
        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !order.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("No autorizado");
        }

        return ResponseEntity.ok(toResponse(order));
    }

    // --- Helpers ---

    private Long extractUserIdFromAuth(Authentication authentication) {
        if (authentication == null
                || !(authentication.getPrincipal() instanceof Usuario usuario)) {
            return null;
        }
        return usuario.getId();
    }

    private OrderResponse toResponse(Order order) {
        OrderResponse resp = new OrderResponse();
        resp.setId(order.getId());
        resp.setUserId(order.getUserId());
        resp.setTotal(order.getTotal());
        resp.setCreatedAt(order.getCreatedAt());

        resp.setItems(order.getItems().stream().map(i -> {
            OrderResponse.OrderItemResponse ir = new OrderResponse.OrderItemResponse();
            ir.setProductId(i.getProductId());
            ir.setQuantity(i.getQuantity());
            ir.setUnitPrice(i.getUnitPrice());
            return ir;
        }).collect(Collectors.toList()));

        return resp;
    }
}
