package com.study.geekshop.service.mapper;

import com.study.geekshop.exceptions.ProductNotFoundException;
import com.study.geekshop.model.dto.request.OrderItemRequestDto;
import com.study.geekshop.model.dto.response.OrderItemResponseDto;
import com.study.geekshop.model.entity.OrderItem;
import com.study.geekshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderItemMapper {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    public OrderItem toEntity(OrderItemRequestDto dto) {
        var product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setPrice(dto.getPrice());
        return orderItem;
    }

    public OrderItemResponseDto toDto(OrderItem orderItem) {
        return new OrderItemResponseDto(
                orderItem.getId(),
                productMapper.toDto(orderItem.getProduct()),
                orderItem.getQuantity(),
                orderItem.getPrice()
        );
    }

    public void updateEntity(OrderItem orderItem, OrderItemRequestDto dto) {
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setPrice(dto.getPrice());
    }
}
