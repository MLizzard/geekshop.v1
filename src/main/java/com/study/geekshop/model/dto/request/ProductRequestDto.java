package com.study.geekshop.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {

    @NotBlank(message = "Name must not be blank")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @NotNull(message = "Price must not be null")
    @Positive(message = "Price must be positive")
    private Double price;

    @Size(max = 255, message = "Description must be up to 255 characters")
    private String description;

    @NotNull(message = "Stock status must not be null")
    private Boolean inStock;

    @NotNull(message = "Category must not be null")
    @Positive(message = "Category ID must be positive")
    private Long categoryId;
}