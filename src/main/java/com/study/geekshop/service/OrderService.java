package com.study.geekshop.service;

import com.study.geekshop.model.dto.request.OrderItemRequestDTO;
import com.study.geekshop.model.dto.request.OrderRequestDTO;
import com.study.geekshop.model.dto.response.OrderItemResponseDTO;
import com.study.geekshop.model.dto.response.OrderResponseDTO;
import com.study.geekshop.model.entity.Order;
import java.util.List;

public interface OrderService {

    List<OrderResponseDTO> findAllOrders();
    OrderResponseDTO findOrderById(Long orderId);
    OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO);
    OrderResponseDTO updateOrder(Long orderId, OrderRequestDTO orderRequestDTO);
    void deleteOrder(Long orderId);
    List<OrderItemResponseDTO> getAllOrderItemsByOrderId(Long orderId);

    OrderItemResponseDTO getOrderItemById(Long orderId, Long itemId);
    OrderItemResponseDTO createOrderItem(Long orderId, OrderItemRequestDTO orderItemRequestDTO);
    OrderItemResponseDTO updateOrderItem(Long orderId, Long itemId, OrderItemRequestDTO orderItemRequestDTO);
    void deleteOrderItem(Long orderId, Long itemId);
}
