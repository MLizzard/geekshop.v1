package com.study.geekshop.service.mapper;

import com.study.geekshop.model.dto.request.ProductRequestDTO;
import com.study.geekshop.model.dto.response.ProductResponseDTO;
import com.study.geekshop.model.dto.response.CategoryResponseDTO;
import com.study.geekshop.model.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequestDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setInStock(dto.isInStock());
        return product;
    }

    public ProductResponseDTO toDTO(Product product) {
        CategoryResponseDTO categoryDTO = (product.getCategory() != null)
                ? new CategoryResponseDTO(product.getCategory().getId(), product.getCategory().getName())
                : null;

        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.isInStock(),
                categoryDTO
        );
    }
}

