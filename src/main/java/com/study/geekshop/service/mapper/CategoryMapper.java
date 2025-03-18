package com.study.geekshop.service.mapper;

import com.study.geekshop.model.dto.request.CategoryRequestDto;
import com.study.geekshop.model.dto.response.CategoryResponseDto;
import com.study.geekshop.model.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequestDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        return category;
    }

    public CategoryResponseDto toDto(Category category) {
        return new CategoryResponseDto(category.getId(), category.getName());
    }
}
