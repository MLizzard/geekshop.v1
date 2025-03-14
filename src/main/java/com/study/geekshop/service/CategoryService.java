package com.study.geekshop.service;

import com.study.geekshop.model.dto.request.CategoryRequestDTO;
import com.study.geekshop.model.dto.response.CategoryResponseDTO;
import com.study.geekshop.model.entity.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryResponseDTO> findAll();
    CategoryResponseDTO findById(Long id);
    CategoryResponseDTO create(CategoryRequestDTO dto);
    CategoryResponseDTO update(Long id, CategoryRequestDTO dto);
    void delete(Long id);
}

