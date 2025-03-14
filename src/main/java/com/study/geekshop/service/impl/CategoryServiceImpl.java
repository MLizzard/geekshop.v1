package com.study.geekshop.service.impl;

import com.study.geekshop.model.dto.request.CategoryRequestDTO;
import com.study.geekshop.model.dto.response.CategoryResponseDTO;
import com.study.geekshop.model.entity.Category;
import com.study.geekshop.repository.CategoryRepository;
import com.study.geekshop.service.CategoryService;
import com.study.geekshop.service.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponseDTO> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDTO)
                .toList();
    }

    @Override
    public CategoryResponseDTO findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Категория не найдена"));
        return categoryMapper.toDTO(category);
    }

    @Override
    public CategoryResponseDTO create(CategoryRequestDTO dto) {
        Category category = categoryMapper.toEntity(dto);
        category = categoryRepository.save(category);
        return categoryMapper.toDTO(category);
    }

    @Override
    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Категория не найдена"));

        category.setName(dto.getName());
        category = categoryRepository.save(category);
        return categoryMapper.toDTO(category);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}