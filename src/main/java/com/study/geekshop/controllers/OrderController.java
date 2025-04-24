package com.study.geekshop.controllers;

import com.study.geekshop.model.dto.request.OrderItemRequestDto;
import com.study.geekshop.model.dto.request.OrderRequestDto;
import com.study.geekshop.model.dto.response.OrderItemResponseDto;
import com.study.geekshop.model.dto.response.OrderResponseDto;
import com.study.geekshop.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "API for managing customer orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Get all orders", description = "Returns a list of all existing orders.")
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAllOrders());
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get order by ID",
            description = "Returns an order based on the provided identifier.")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.findOrderById(orderId));
    }

    @PostMapping
    @Operation(summary = "Create a new order",
            description = "Creates a new order based on the provided data.")
    public ResponseEntity<OrderResponseDto>
        createOrder(@Valid @RequestBody OrderRequestDto orderRequestDto) {
        OrderResponseDto createdOrder = orderService.createOrder(orderRequestDto);
        return ResponseEntity.status(201).body(createdOrder);
    }

    @PutMapping("/{orderId}")
    @Operation(summary = "Update an existing order",
            description = "Updates the data of an existing order.")
    public ResponseEntity<OrderResponseDto> updateOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderRequestDto orderRequestDto) {
        return ResponseEntity.ok(orderService.updateOrder(orderId, orderRequestDto));
    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "Delete an order",
            description = "Deletes an order based on the provided identifier.")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get all order items for an order",
            description = "Returns items within a specific order.")
    public ResponseEntity<List<OrderItemResponseDto>>
        getAllOrderItemsByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getAllOrderItemsByOrderId(orderId));
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get a specific order item",
            description = "Returns a specific item within an order.")
    public ResponseEntity<OrderItemResponseDto> getOrderItemById(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        return ResponseEntity.ok(orderService.getOrderItemById(orderId, itemId));
    }

    @PostMapping("/{orderId}/items")
    @Operation(summary = "Add a new item to an order",
            description = "Adds a new item to a specific order.")
    public ResponseEntity<OrderItemResponseDto> createOrderItem(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderItemRequestDto orderItemRequestDto) {
        OrderItemResponseDto createdItem = orderService.createOrderItem(orderId,
                orderItemRequestDto);
        return ResponseEntity.status(201).body(createdItem);
    }

    @PutMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Update an order item",
            description = "Updates a specific item within an order.")
    public ResponseEntity<OrderItemResponseDto> updateOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId,
            @Valid @RequestBody OrderItemRequestDto orderItemRequestDto) {
        return ResponseEntity.ok(orderService.updateOrderItem(orderId,
                itemId,
                orderItemRequestDto));
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Delete an order item",
            description = "Deletes a specific item from an order.")
    public ResponseEntity<Void> deleteOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        orderService.deleteOrderItem(orderId, itemId);
        return ResponseEntity.noContent().build();
    }
}