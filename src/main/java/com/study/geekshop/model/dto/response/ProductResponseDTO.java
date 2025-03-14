package com.study.geekshop.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String name;
    private double price;
    private String description;
    private boolean inStock;
    private CategoryResponseDTO category;
}