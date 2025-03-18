package com.study.geekshop.service;

import com.study.geekshop.model.dto.request.ProductRequestDto;
import com.study.geekshop.model.dto.response.ProductResponseDto;

import java.util.List;

public interface ProductService {
    List<ProductResponseDto> findAll();

    ProductResponseDto findById(Long id);

    ProductResponseDto create(ProductRequestDto dto);

    ProductResponseDto update(Long id, ProductRequestDto dto);

    void delete(Long id);
}
