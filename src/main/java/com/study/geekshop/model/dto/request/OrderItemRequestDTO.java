package com.study.geekshop.model.dto.request;

import lombok.Data;

@Data
public class OrderItemRequestDTO {
    private Long productId;
    private int quantity;
    private double price;
}
