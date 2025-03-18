package com.study.geekshop.service;

import com.study.geekshop.model.dto.request.CategoryRequestDto;
import com.study.geekshop.model.dto.response.CategoryResponseDto;
import java.util.List;

public interface CategoryService {
    List<CategoryResponseDto> findAll();

    CategoryResponseDto findById(Long id);

    CategoryResponseDto create(CategoryRequestDto dto);

    CategoryResponseDto update(Long id, CategoryRequestDto dto);

    void delete(Long id);
}

