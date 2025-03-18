package com.study.geekshop.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemResponseDto {
    private Long id;
    private ProductResponseDto product;
    private int quantity;
    private double price;
}
