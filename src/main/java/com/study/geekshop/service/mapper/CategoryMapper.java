package com.study.geekshop.service.mapper;

import com.study.geekshop.model.dto.request.CategoryRequestDTO;
import com.study.geekshop.model.dto.response.CategoryResponseDTO;
import com.study.geekshop.model.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequestDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        return category;
    }

    public CategoryResponseDTO toDTO(Category category) {
        return new CategoryResponseDTO(category.getId(), category.getName());
    }
}
