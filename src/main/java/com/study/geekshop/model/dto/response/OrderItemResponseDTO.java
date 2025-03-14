package com.study.geekshop.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemResponseDTO {
    private Long id;
    private ProductResponseDTO product;
    private int quantity;
    private double price;
}
