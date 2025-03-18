package com.study.geekshop.service.impl;

import com.study.geekshop.exceptions.CategoryNotFoundException;
import com.study.geekshop.model.dto.request.CategoryRequestDto;
import com.study.geekshop.model.dto.response.CategoryResponseDto;
import com.study.geekshop.model.entity.Category;
import com.study.geekshop.repository.CategoryRepository;
import com.study.geekshop.service.CategoryService;
import com.study.geekshop.service.mapper.CategoryMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponseDto> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryResponseDto findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена"));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryResponseDto create(CategoryRequestDto dto) {
        Category category = categoryMapper.toEntity(dto);
        category = categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryResponseDto update(Long id, CategoryRequestDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена"));

        category.setName(dto.getName());
        category = categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}