package com.study.geekshop.service.impl;

import com.study.geekshop.exceptions.OrderNotFoundException;
import com.study.geekshop.model.dto.request.OrderItemRequestDTO;
import com.study.geekshop.model.dto.request.OrderRequestDTO;
import com.study.geekshop.model.dto.response.OrderItemResponseDTO;
import com.study.geekshop.model.dto.response.OrderResponseDTO;
import com.study.geekshop.model.entity.Order;
import com.study.geekshop.model.entity.OrderItem;
import com.study.geekshop.model.entity.User;
import com.study.geekshop.repository.OrderItemRepository;
import com.study.geekshop.repository.OrderRepository;
import com.study.geekshop.repository.UserRepository;
import com.study.geekshop.service.OrderService;
import com.study.geekshop.service.mapper.OrderItemMapper;
import com.study.geekshop.service.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public List<OrderResponseDTO> findAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDTO)
                .toList();
    }

    @Override
    public OrderResponseDTO findOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order  not found"));
        return orderMapper.toDTO(order);
    }

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        Order order = orderMapper.toEntity(orderRequestDTO);
        User user = userRepository.findById(orderRequestDTO.getUserId())
                .orElseThrow(() -> new OrderNotFoundException("User not found"));
        order.setUser(user);

        List<OrderItem> orderItems = orderRequestDTO.getItems().stream()
                .map(orderItemMapper::toEntity)
                .peek(orderItem -> orderItem.setOrder(order))
                .toList();
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDTO(savedOrder);
    }

    @Override
    public OrderResponseDTO updateOrder(Long orderId, OrderRequestDTO orderRequestDTO) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        orderMapper.updateEntity(existingOrder, orderRequestDTO);

        List<OrderItem> updatedItems = orderRequestDTO.getItems().stream()
                .map(orderItemMapper::toEntity)
                .peek(orderItem -> orderItem.setOrder(existingOrder))
                .toList();
        existingOrder.getItems().clear();
        existingOrder.getItems().addAll(updatedItems);

        Order updatedOrder = orderRepository.save(existingOrder);
        return orderMapper.toDTO(updatedOrder);
    }

    @Override
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    // CRUD для OrderItem

    @Override
    public List<OrderItemResponseDTO> getAllOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId).stream()
                .map(orderItemMapper::toDTO)
                .toList();
    }

    @Override
    public OrderItemResponseDTO getOrderItemById(Long orderId, Long itemId) {
        OrderItem orderItem = orderItemRepository.findByIdAndOrderId(itemId, orderId)
                .orElseThrow(() -> new OrderNotFoundException("OrderItem  not found"));
        return orderItemMapper.toDTO(orderItem);
    }

    @Override
    public OrderItemResponseDTO createOrderItem(Long orderId, OrderItemRequestDTO orderItemRequestDTO) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        OrderItem orderItem = orderItemMapper.toEntity(orderItemRequestDTO);
        orderItem.setOrder(order);

        OrderItem savedOrderItem = orderItemRepository.save(orderItem);
        return orderItemMapper.toDTO(savedOrderItem);
    }

    @Override
    public OrderItemResponseDTO updateOrderItem(Long orderId, Long itemId, OrderItemRequestDTO orderItemRequestDTO) {
        OrderItem orderItem = orderItemRepository.findByIdAndOrderId(itemId, orderId)
                .orElseThrow(() -> new OrderNotFoundException("OrderItem not found"));

        orderItemMapper.updateEntity(orderItem, orderItemRequestDTO);

        OrderItem updatedOrderItem = orderItemRepository.save(orderItem);
        return orderItemMapper.toDTO(updatedOrderItem);
    }

    @Override
    public void deleteOrderItem(Long orderId, Long itemId) {
        OrderItem orderItem = orderItemRepository.findByIdAndOrderId(itemId, orderId)
                .orElseThrow(() -> new OrderNotFoundException("OrderItem not found"));
        orderItemRepository.delete(orderItem);
    }
}

