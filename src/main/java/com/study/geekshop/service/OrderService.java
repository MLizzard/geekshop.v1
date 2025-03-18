package com.study.geekshop.service;

import com.study.geekshop.model.dto.request.OrderItemRequestDto;
import com.study.geekshop.model.dto.request.OrderRequestDto;
import com.study.geekshop.model.dto.response.OrderItemResponseDto;
import com.study.geekshop.model.dto.response.OrderResponseDto;

import java.util.List;

public interface OrderService {

    List<OrderResponseDto> findAllOrders();
    OrderResponseDto findOrderById(Long orderId);
    OrderResponseDto createOrder(OrderRequestDto orderRequestDTO);
    OrderResponseDto updateOrder(Long orderId, OrderRequestDto orderRequestDTO);
    void deleteOrder(Long orderId);
    List<OrderItemResponseDto> getAllOrderItemsByOrderId(Long orderId);

    OrderItemResponseDto getOrderItemById(Long orderId, Long itemId);
    OrderItemResponseDto createOrderItem(Long orderId, OrderItemRequestDto orderItemRequestDTO);
    OrderItemResponseDto updateOrderItem(Long orderId, Long itemId, OrderItemRequestDto orderItemRequestDTO);
    void deleteOrderItem(Long orderId, Long itemId);
}
