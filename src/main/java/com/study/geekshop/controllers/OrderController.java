package com.study.geekshop.controllers;

import com.study.geekshop.model.dto.request.OrderItemRequestDto;
import com.study.geekshop.model.dto.request.OrderRequestDto;
import com.study.geekshop.model.dto.response.OrderItemResponseDto;
import com.study.geekshop.model.dto.response.OrderResponseDto;
import com.study.geekshop.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "API for managing customer orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Get all orders", description = "Returns a list of all existing orders.")
    public List<OrderResponseDto> getAllOrders() {
        return orderService.findAllOrders();
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get order by ID",
            description = "Returns an order based on the provided identifier.")
    public OrderResponseDto getOrderById(@PathVariable Long orderId) {
        return orderService.findOrderById(orderId);
    }

    @PostMapping
    @Operation(summary = "Create a new order",
            description = "Creates a new order based on the provided data.")
    public OrderResponseDto createOrder(@Valid @RequestBody OrderRequestDto orderRequestDto) {
        return orderService.createOrder(orderRequestDto);
    }

    @PutMapping("/{orderId}")
    @Operation(summary = "Update an existing order",
            description = "Updates the data of an existing order based on the provided identifier.")
    public OrderResponseDto updateOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderRequestDto orderRequestDto) {
        return orderService.updateOrder(orderId, orderRequestDto);
    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "Delete an order",
            description = "Deletes an order based on the provided identifier.")
    public void deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
    }

    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get all order items for an order",
            description = "Returns a list of all items within a specific order.")
    public List<OrderItemResponseDto> getAllOrderItemsByOrderId(@PathVariable Long orderId) {
        return orderService.getAllOrderItemsByOrderId(orderId);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get a specific order item",
            description = "Returns a specific item within an order based on the provided identifiers.")
    public OrderItemResponseDto getOrderItemById(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        return orderService.getOrderItemById(orderId, itemId);
    }

    @PostMapping("/{orderId}/items")
    @Operation(summary = "Add a new item to an order",
            description = "Adds a new item to a specific order.")
    public OrderItemResponseDto createOrderItem(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderItemRequestDto orderItemRequestDto) {
        return orderService.createOrderItem(orderId, orderItemRequestDto);
    }

    @PutMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Update an order item",
            description = "Updates a specific item within an order based on the provided identifiers.")
    public OrderItemResponseDto updateOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId,
            @Valid @RequestBody OrderItemRequestDto orderItemRequestDto) {
        return orderService.updateOrderItem(orderId, itemId, orderItemRequestDto);
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Delete an order item",
            description = "Deletes a specific item from an order based on the provided identifiers.")
    public void deleteOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        orderService.deleteOrderItem(orderId, itemId);
    }
}