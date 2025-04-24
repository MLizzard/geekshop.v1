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
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderResponseDto findOrderById(Long orderId) {
        Order orderFromCache = orderCache.get(orderId);
        if (orderFromCache != null) {
            return orderMapper.toDto(orderFromCache);
        }
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order  not found"));
        return orderMapper.toDto(order);
    }

    @Override
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        Order order = orderMapper.toEntity(orderRequestDto);
        User user = userRepository.findById(orderRequestDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        order.setUser(user);

        List<OrderItem> orderItems = orderRequestDto.getItems().stream()
                .map(orderItemMapper::toEntity)
                .toList();
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        orderCache.put(savedOrder.getId(), savedOrder);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public OrderResponseDto updateOrder(Long orderId, OrderRequestDto orderRequestDto) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        orderMapper.updateEntity(existingOrder, orderRequestDto);

        List<OrderItem> updatedItems = orderRequestDto.getItems().stream()
                .map(orderItemMapper::toEntity)
                .toList();

        existingOrder.setItems(updatedItems);


        Order updatedOrder = orderRepository.save(existingOrder);
        orderCache.put(updatedOrder.getId(), updatedOrder);
        return orderMapper.toDto(updatedOrder);
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
    public OrderItemResponseDto createOrderItem(Long orderId,
                                                OrderItemRequestDto orderItemRequestDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        OrderItem orderItem = orderItemMapper.toEntity(orderItemRequestDto);
        orderItem.setOrder(order);

        OrderItem savedOrderItem = orderItemRepository.save(orderItem);
        return orderItemMapper.toDto(savedOrderItem);
    }

    @Override
    public OrderItemResponseDto updateOrderItem(Long orderId,
                                                Long itemId,
                                                OrderItemRequestDto orderItemRequestDto) {
        OrderItem orderItem = orderItemRepository.findByIdAndOrderId(itemId, orderId)
                .orElseThrow(() -> new OrderNotFoundException("OrderItem not found"));

        orderItemMapper.updateEntity(orderItem, orderItemRequestDto);

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

