package com.study.geekshop.service.mapper;

import com.study.geekshop.model.dto.request.OrderRequestDto;
import com.study.geekshop.model.dto.response.OrderItemResponseDto;
import com.study.geekshop.model.dto.response.OrderResponseDto;
import com.study.geekshop.model.dto.response.ProductResponseDto;
import com.study.geekshop.model.entity.Order;
import com.study.geekshop.model.entity.OrderItem;
import com.study.geekshop.model.enums.OrderStatus;
import com.study.geekshop.model.entity.User;
import com.study.geekshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final OrderItemMapper orderItemMapper;
    private final CategoryMapper categoryMapper; // Добавил маппер продуктов
    private final UserRepository userRepository;

    public Order toEntity(OrderRequestDto orderRequestDto) {
        if (orderRequestDto == null) {
            throw new IllegalArgumentException("OrderRequestDTO cannot be null");
        }

        // Находим пользователя
        User user = userRepository.findById(orderRequestDto.getUserId())
                .orElseThrow(() -> new RuntimeException(
                        "User not found with id: " + orderRequestDto.getUserId()));

        // Создаем заказ
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.NEW); // Пример статуса по умолчанию

        // Преобразуем OrderItemRequestDTO в OrderItem
        List<OrderItem> orderItems = orderRequestDto.getItems().stream()
                .map(orderItemMapper::toEntity)
                .peek(orderItem -> orderItem.setOrder(order)) // Устанавливаем связь с заказом
                .toList();

        order.setItems(orderItems);

        return order;
    }

    public OrderResponseDto toDto(Order order) {
        List<OrderItemResponseDto> items = order.getItems().stream()
                .map(orderItem -> new OrderItemResponseDto(
                        orderItem.getId(),
                        new ProductResponseDto(
                                orderItem.getProduct().getId(),
                                orderItem.getProduct().getName(),
                                orderItem.getProduct().getPrice(),
                                orderItem.getProduct().getDescription(),
                                orderItem.getProduct().isInStock(),
                                categoryMapper
                                        .toDto(orderItem.getProduct().getCategory()) // Категория
                        ),
                        orderItem.getQuantity(),
                        orderItem.getPrice()
                ))
                .toList();

        return new OrderResponseDto(
                order.getId(),
                order.getUser().getUsername(),
                order.getOrderDate(),
                order.getStatus().name(),
                items
        );
    }

    public void updateEntity(Order order, OrderRequestDto dto) {
        order.setStatus(OrderStatus.valueOf(OrderStatus.NEW.name()));
    }
}
