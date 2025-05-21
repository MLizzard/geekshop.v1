package com.study.geekshop.service;

import com.study.geekshop.model.dto.request.ProductRequestDto;
import com.study.geekshop.model.dto.response.ProductResponseDto;
import org.springframework.core.io.Resource;

import java.util.List;

public interface ProductService {
    List<ProductResponseDto> findAll();

    ProductResponseDto findById(Long id);

    List<ProductResponseDto> findAllByCategoryId(Long id);

    List<ProductResponseDto> findAllByCategoryName(String name);

    List<ProductResponseDto> findAllByPriceRangeAndCategoryName(Double minPrice,
                                                                Double maxPrice,
                                                                String categoryName);

    ProductResponseDto create(ProductRequestDto dto);

    List<ProductResponseDto> createAll(List<ProductRequestDto> productRequestDtos);

    ProductResponseDto update(Long id, ProductRequestDto dto);

    void delete(Long id);

    ProductResponseDto createProductWithImage(ProductRequestDto productDto);

    ProductResponseDto updateProductWithImage(Long id, ProductRequestDto productDto);

    Resource getProductImage(Long productId);
}
