package com.study.geekshop.service.mapper;

import com.study.geekshop.model.dto.request.OrderItemRequestDTO;
import com.study.geekshop.model.dto.response.OrderItemResponseDTO;
import com.study.geekshop.model.entity.OrderItem;
import com.study.geekshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderItemMapper {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    public OrderItem toEntity(OrderItemRequestDTO dto) {
        var product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setPrice(dto.getPrice());
        return orderItem;
    }

    public OrderItemResponseDTO toDTO(OrderItem orderItem) {
        return new OrderItemResponseDTO(
                orderItem.getId(),
                productMapper.toDTO(orderItem.getProduct()),
                orderItem.getQuantity(),
                orderItem.getPrice()
        );
    }
    public void updateEntity(OrderItem orderItem, OrderItemRequestDTO dto) {
        orderItem.setQuantity(dto.getQuantity());
        orderItem.setPrice(dto.getPrice());
    }
}
