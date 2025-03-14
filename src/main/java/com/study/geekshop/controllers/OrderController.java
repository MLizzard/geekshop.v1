package com.study.geekshop.controllers;

import com.study.geekshop.model.dto.request.OrderItemRequestDTO;
import com.study.geekshop.model.dto.request.OrderRequestDTO;
import com.study.geekshop.model.dto.response.OrderItemResponseDTO;
import com.study.geekshop.model.dto.response.OrderResponseDTO;
import com.study.geekshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public List<OrderResponseDTO> getAllOrders() {
        return orderService.findAllOrders();
    }

    @GetMapping("/{orderId}")
    public OrderResponseDTO getOrderById(@PathVariable Long orderId) {
        return orderService.findOrderById(orderId);
    }

    @PostMapping
    public OrderResponseDTO createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        return orderService.createOrder(orderRequestDTO);
    }

    @PutMapping("/{orderId}")
    public OrderResponseDTO updateOrder(
            @PathVariable Long orderId,
            @RequestBody OrderRequestDTO orderRequestDTO) {
        return orderService.updateOrder(orderId, orderRequestDTO);
    }

    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
    }

    // CRUD для OrderItem (в контексте Order)

    @GetMapping("/{orderId}/items")
    public List<OrderItemResponseDTO> getAllOrderItemsByOrderId(@PathVariable Long orderId) {
        return orderService.getAllOrderItemsByOrderId(orderId);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemResponseDTO getOrderItemById(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        return orderService.getOrderItemById(orderId, itemId);
    }

    @PostMapping("/{orderId}/items")
    public OrderItemResponseDTO createOrderItem(
            @PathVariable Long orderId,
            @RequestBody OrderItemRequestDTO orderItemRequestDTO) {
        return orderService.createOrderItem(orderId, orderItemRequestDTO);
    }

    @PutMapping("/{orderId}/items/{itemId}")
    public OrderItemResponseDTO updateOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId,
            @RequestBody OrderItemRequestDTO orderItemRequestDTO) {
        return orderService.updateOrderItem(orderId, itemId, orderItemRequestDTO);
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    public void deleteOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        orderService.deleteOrderItem(orderId, itemId);
    }
}