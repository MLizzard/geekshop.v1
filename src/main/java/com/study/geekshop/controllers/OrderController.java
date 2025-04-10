package com.study.geekshop.controllers;

import com.study.geekshop.model.dto.request.OrderItemRequestDto;
import com.study.geekshop.model.dto.request.OrderRequestDto;
import com.study.geekshop.model.dto.response.OrderItemResponseDto;
import com.study.geekshop.model.dto.response.OrderResponseDto;
import com.study.geekshop.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public List<OrderResponseDto> getAllOrders() {
        return orderService.findAllOrders();
    }

    @GetMapping("/{orderId}")
    public OrderResponseDto getOrderById(@PathVariable Long orderId) {
        return orderService.findOrderById(orderId);
    }

    @PostMapping
    public OrderResponseDto createOrder(@Valid @RequestBody OrderRequestDto orderRequestDto) {
        return orderService.createOrder(orderRequestDto);
    }

    @PutMapping("/{orderId}")
    public OrderResponseDto updateOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderRequestDto orderRequestDto) {
        return orderService.updateOrder(orderId, orderRequestDto);
    }

    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
    }

    // CRUD для OrderItem (в контексте Order)

    @GetMapping("/{orderId}/items")
    public List<OrderItemResponseDto> getAllOrderItemsByOrderId(@PathVariable Long orderId) {
        return orderService.getAllOrderItemsByOrderId(orderId);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemResponseDto getOrderItemById(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        return orderService.getOrderItemById(orderId, itemId);
    }

    @PostMapping("/{orderId}/items")
    public OrderItemResponseDto createOrderItem(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderItemRequestDto orderItemRequestDto) {
        return orderService.createOrderItem(orderId, orderItemRequestDto);
    }

    @PutMapping("/{orderId}/items/{itemId}")
    public OrderItemResponseDto updateOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId,
            @Valid @RequestBody OrderItemRequestDto orderItemRequestDto) {
        return orderService.updateOrderItem(orderId, itemId, orderItemRequestDto);
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    public void deleteOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        orderService.deleteOrderItem(orderId, itemId);
    }
}