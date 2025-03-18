package com.study.geekshop.model.dto.request;

import lombok.Data;

@Data
public class OrderItemRequestDto {
    private Long productId;
    private int quantity;
    private double price;
}
