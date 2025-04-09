package com.study.geekshop.service.impl;

import com.study.geekshop.cache.OrderCache;
import com.study.geekshop.exceptions.OrderNotFoundException;
import com.study.geekshop.exceptions.UserNotFoundException;
import com.study.geekshop.model.dto.request.OrderItemRequestDto;
import com.study.geekshop.model.dto.request.OrderRequestDto;
import com.study.geekshop.model.dto.response.OrderItemResponseDto;
import com.study.geekshop.model.dto.response.OrderResponseDto;
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
    private final OrderCache orderCache;

    @Override
    public List<OrderResponseDto> findAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDTO)
                .toList();
    }

    @Override
    public OrderResponseDto findOrderById(Long orderId) {
        Order orderFromCache = orderCache.get(orderId);
        if (orderFromCache != null){
            return orderMapper.toDTO(orderFromCache);
        }
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order  not found"));
        return orderMapper.toDTO(order);
    }

    @Override
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDTO) {
        Order order = orderMapper.toEntity(orderRequestDTO);
        User user = userRepository.findById(orderRequestDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        order.setUser(user);

        List<OrderItem> orderItems = orderRequestDTO.getItems().stream()
                .map(orderItemMapper::toEntity)
                .toList();
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        orderCache.put(savedOrder.getId(), savedOrder);
        return orderMapper.toDTO(savedOrder);
    }

    @Override
    public OrderResponseDto updateOrder(Long orderId, OrderRequestDto orderRequestDTO) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        orderMapper.updateEntity(existingOrder, orderRequestDTO);

        List<OrderItem> updatedItems = orderRequestDTO.getItems().stream()
                .map(orderItemMapper::toEntity)
                .toList();
        existingOrder.getItems().clear();
        existingOrder.getItems().addAll(updatedItems);

        Order updatedOrder = orderRepository.save(existingOrder);
        orderCache.put(updatedOrder.getId(), updatedOrder);
        return orderMapper.toDTO(updatedOrder);
    }

    @Override
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
        orderCache.remove(orderId);
    }

    // CRUD для OrderItem

    @Override
    public List<OrderItemResponseDto> getAllOrderItemsByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId).stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemResponseDto getOrderItemById(Long orderId, Long itemId) {
        OrderItem orderItem = orderItemRepository.findByIdAndOrderId(itemId, orderId)
                .orElseThrow(() -> new OrderNotFoundException("OrderItem  not found"));
        return orderItemMapper.toDto(orderItem);
    }

    @Override
    public OrderItemResponseDto createOrderItem(Long orderId, OrderItemRequestDto orderItemRequestDTO) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        OrderItem orderItem = orderItemMapper.toEntity(orderItemRequestDTO);
        orderItem.setOrder(order);

        OrderItem savedOrderItem = orderItemRepository.save(orderItem);
        return orderItemMapper.toDto(savedOrderItem);
    }

    @Override
    public OrderItemResponseDto updateOrderItem(Long orderId, Long itemId, OrderItemRequestDto orderItemRequestDTO) {
        OrderItem orderItem = orderItemRepository.findByIdAndOrderId(itemId, orderId)
                .orElseThrow(() -> new OrderNotFoundException("OrderItem not found"));

        orderItemMapper.updateEntity(orderItem, orderItemRequestDTO);

        OrderItem updatedOrderItem = orderItemRepository.save(orderItem);
        return orderItemMapper.toDto(updatedOrderItem);
    }

    @Override
    public void deleteOrderItem(Long orderId, Long itemId) {
        OrderItem orderItem = orderItemRepository.findByIdAndOrderId(itemId, orderId)
                .orElseThrow(() -> new OrderNotFoundException("OrderItem not found"));
        orderItemRepository.delete(orderItem);
    }
}

