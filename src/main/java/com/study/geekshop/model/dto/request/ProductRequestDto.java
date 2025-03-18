package com.study.geekshop.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {
    private String name;
    private double price;
    private String description;
    private boolean inStock;
    private Long categoryId; // Только ID категории для запроса
}