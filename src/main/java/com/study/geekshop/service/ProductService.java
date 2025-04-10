package com.study.geekshop.service;

import com.study.geekshop.model.dto.request.ProductRequestDto;
import com.study.geekshop.model.dto.response.ProductResponseDto;
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

    ProductResponseDto update(Long id, ProductRequestDto dto);

    void delete(Long id);
}
