package com.study.geekshop.service.mapper;

import com.study.geekshop.model.dto.request.ProductRequestDto;
import com.study.geekshop.model.dto.response.ProductResponseDto;
import com.study.geekshop.model.dto.response.CategoryResponseDto;
import com.study.geekshop.model.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequestDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setInStock(dto.getInStock());
        return product;
    }

    public ProductResponseDto toDto(Product product) {
        CategoryResponseDto categoryDto = (product.getCategory() != null)
                ? new CategoryResponseDto(product.getCategory().getId(),
                                            product.getCategory().getName())
                : null;

        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.isInStock(),
                categoryDto
        );
    }
}

