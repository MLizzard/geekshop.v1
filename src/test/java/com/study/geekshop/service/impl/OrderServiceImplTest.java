package com.study.geekshop.service.impl;

import com.study.geekshop.cache.OrderCache;
import com.study.geekshop.model.dto.request.OrderItemRequestDto;
import com.study.geekshop.model.dto.request.OrderRequestDto;
import com.study.geekshop.model.dto.response.OrderItemResponseDto;
import com.study.geekshop.model.dto.response.OrderResponseDto;
import com.study.geekshop.model.dto.response.ProductResponseDto;
import com.study.geekshop.model.entity.Order;
import com.study.geekshop.model.entity.OrderItem;
import com.study.geekshop.model.entity.User;
import com.study.geekshop.repository.OrderItemRepository;
import com.study.geekshop.repository.OrderRepository;
import com.study.geekshop.repository.UserRepository;
import com.study.geekshop.service.mapper.OrderItemMapper;
import com.study.geekshop.service.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderItemMapper orderItemMapper;

    @Mock
    private OrderCache orderCache;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void shouldReturnAllOrders() {
        Order order = new Order();
        OrderResponseDto dto = new OrderResponseDto(1L, "user", LocalDateTime.now(), "NEW", List.of());
        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(orderMapper.toDto(order)).thenReturn(dto);

        List<OrderResponseDto> result = orderService.findAllOrders();

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void shouldReturnOrderFromCache() {
        Long orderId = 1L;
        Order order = new Order();
        OrderResponseDto dto = new OrderResponseDto(orderId, "user", LocalDateTime.now(), "NEW", List.of());
        when(orderCache.get(orderId)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(dto);

        OrderResponseDto result = orderService.findOrderById(orderId);

        assertEquals(dto, result);
    }

    @Test
    void shouldReturnOrderFromDbIfNotInCache() {
        Long orderId = 1L;
        Order order = new Order();
        OrderResponseDto dto = new OrderResponseDto(orderId, "user", LocalDateTime.now(), "NEW", List.of());

        when(orderCache.get(orderId)).thenReturn(null);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(dto);

        OrderResponseDto result = orderService.findOrderById(orderId);

        assertEquals(dto, result);
    }

    @Test
    void shouldThrowWhenOrderNotFound() {
        when(orderCache.get(1L)).thenReturn(null);
        when(orderRepository.findById(1L)).thenReturn(null);

        assertThrows(PotentialStubbingProblem.class, () -> orderService.findOrderById(1L));
    }

    @Test
    void shouldCreateOrder() {
        OrderRequestDto requestDto = new OrderRequestDto();
        requestDto.setUserId(1L);
        OrderItemRequestDto itemDto = new OrderItemRequestDto();
        itemDto.setProductId(2L); itemDto.setPrice(100.0); itemDto.setQuantity(1);
        requestDto.setItems(List.of(itemDto));

        User user = new User(); user.setId(1L);
        Order order = new Order();
        OrderItem orderItem = new OrderItem();

        Order savedOrder = new Order(); savedOrder.setId(1L);
        OrderResponseDto responseDto = new OrderResponseDto(1L, "user", LocalDateTime.now(), "NEW", List.of());

        when(orderMapper.toEntity(requestDto)).thenReturn(order);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(orderItemMapper.toEntity(itemDto)).thenReturn(orderItem);
        when(orderRepository.save(order)).thenReturn(savedOrder);
        when(orderMapper.toDto(savedOrder)).thenReturn(responseDto);

        OrderResponseDto result = orderService.createOrder(requestDto);

        assertEquals(responseDto, result);
    }

    @Test
    void shouldUpdateOrder() {
        Long orderId = 1L;
        Order existing = new Order();
        OrderRequestDto requestDto = new OrderRequestDto();
        OrderItemRequestDto itemDto = new OrderItemRequestDto();
        itemDto.setProductId(1L); itemDto.setQuantity(1); itemDto.setPrice(10.0);
        requestDto.setItems(List.of(itemDto));

        OrderItem item = new OrderItem();
        Order updated = new Order(); updated.setId(orderId);
        OrderResponseDto dto = new OrderResponseDto(orderId, "user", LocalDateTime.now(), "NEW", List.of(new OrderItemResponseDto(1L, new ProductResponseDto(), 5, 69)));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existing));
        doNothing().when(orderMapper).updateEntity(existing, requestDto);
        when(orderItemMapper.toEntity(itemDto)).thenReturn(item);
        when(orderRepository.save(existing)).thenReturn(updated);
        when(orderMapper.toDto(updated)).thenReturn(dto);

        OrderResponseDto result = orderService.updateOrder(orderId, requestDto);

        assertEquals(dto, result);
    }

    @Test
    void shouldDeleteOrder() {
        Long id = 1L;

        orderService.deleteOrder(id);

        verify(orderRepository).deleteById(id);
        verify(orderCache).remove(id);
    }

    @Test
    void shouldReturnOrderItemById() {
        OrderItem item = new OrderItem();
        OrderItemResponseDto dto = new OrderItemResponseDto(1L, null, 1, 10.0);

        when(orderItemRepository.findByIdAndOrderId(2L, 1L)).thenReturn(Optional.of(item));
        when(orderItemMapper.toDto(item)).thenReturn(dto);

        OrderItemResponseDto result = orderService.getOrderItemById(1L, 2L);

        assertEquals(dto, result);
    }

    @Test
    void shouldCreateOrderItem() {
        Long orderId = 1L;
        Order order = new Order(); order.setId(orderId);
        OrderItemRequestDto requestDto = new OrderItemRequestDto();
        requestDto.setProductId(1L); requestDto.setQuantity(1); requestDto.setPrice(10.0);

        OrderItem item = new OrderItem();
        OrderItem savedItem = new OrderItem();
        OrderItemResponseDto dto = new OrderItemResponseDto(1L, null, 1, 10.0);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderItemMapper.toEntity(requestDto)).thenReturn(item);
        when(orderItemRepository.save(item)).thenReturn(savedItem);
        when(orderItemMapper.toDto(savedItem)).thenReturn(dto);

        OrderItemResponseDto result = orderService.createOrderItem(orderId, requestDto);

        assertEquals(dto, result);
    }

    @Test
    void shouldUpdateOrderItem() {
        Long orderId = 1L, itemId = 2L;
        OrderItem item = new OrderItem();
        OrderItem updated = new OrderItem();
        OrderItemRequestDto requestDto = new OrderItemRequestDto();
        requestDto.setProductId(1L); requestDto.setPrice(10.0); requestDto.setQuantity(2);
        OrderItemResponseDto dto = new OrderItemResponseDto(2L, null, 2, 10.0);

        when(orderItemRepository.findByIdAndOrderId(itemId, orderId)).thenReturn(Optional.of(item));
        doNothing().when(orderItemMapper).updateEntity(item, requestDto);
        when(orderItemRepository.save(item)).thenReturn(updated);
        when(orderItemMapper.toDto(updated)).thenReturn(dto);

        OrderItemResponseDto result = orderService.updateOrderItem(orderId, itemId, requestDto);

        assertEquals(dto, result);
    }

    @Test
    void shouldDeleteOrderItem() {
        Long orderId = 1L, itemId = 2L;
        OrderItem item = new OrderItem();

        when(orderItemRepository.findByIdAndOrderId(itemId, orderId)).thenReturn(Optional.of(item));

        orderService.deleteOrderItem(orderId, itemId);

        verify(orderItemRepository).delete(item);
    }
}