package com.study.geekshop.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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

    @Size(max = 511, message = "Description must be up to 255 characters")
    private String description;

    @NotNull(message = "Stock status must not be null")
    private Boolean inStock;

    @NotNull(message = "Category must not be null")
    @Positive(message = "Category ID must be positive")
    private Long categoryId;

    @NotNull(message = "Image must not be null")
    private MultipartFile image;
}