package com.study.geekshop.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderItemRequestDto {
    @NotNull(message = "Product ID must not be null")
    @Positive(message = "Product ID must be positive")
    private Long productId;

    @NotNull(message = "Quantity must not be null")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;

    @NotNull(message = "Price must not be null")
    @Positive(message = "Price must be positive")
    private Double price;
}
