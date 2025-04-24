package com.study.geekshop.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

@Data
public class OrderRequestDto {

    @NotNull(message = "User ID must not be null")
    @Positive(message = "User ID must be positive")
    private Long userId;

    @NotEmpty(message = "List of items must not be empty")
    @Size(min = 1, message = "Order must contain at least one item")
    private List<OrderItemRequestDto> items;
}
