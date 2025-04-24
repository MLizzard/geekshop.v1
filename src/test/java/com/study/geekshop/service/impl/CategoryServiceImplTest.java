package com.study.geekshop.service.impl;

import com.study.geekshop.cache.CategoryCache;
import com.study.geekshop.exceptions.CategoryNotFoundException;
import com.study.geekshop.model.dto.request.CategoryRequestDto;
import com.study.geekshop.model.dto.response.CategoryResponseDto;
import com.study.geekshop.model.entity.Category;
import com.study.geekshop.repository.CategoryRepository;
import com.study.geekshop.service.mapper.CategoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private CategoryCache categoryCache;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_shouldReturnAllCategories() {
        Category category1 = new Category();
        Category category2 = new Category();
        category1.setId(1L);
        category2.setId(2L);
        category1.setName("Phones");
        category2.setName("Laptops");
        List<Category> categories = List.of(category1, category2);
        List<CategoryResponseDto> dtos = List.of(
                new CategoryResponseDto(1L, "Phones"),
                new CategoryResponseDto(2L, "Laptops"));

        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.toDto(any())).thenReturn(dtos.get(0), dtos.get(1));

        List<CategoryResponseDto> result = categoryService.findAll();

        assertEquals(2, result.size());
        verify(categoryMapper, times(2)).toDto(any());
    }

    @Test
    void findById_shouldReturnFromCache() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Phones");
        CategoryResponseDto dto = new CategoryResponseDto(1L, "Phones");

        when(categoryCache.get(1L)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(dto);

        CategoryResponseDto result = categoryService.findById(1L);

        assertEquals(dto, result);
        verify(categoryRepository, never()).findById(any());
    }

    @Test
    void findById_shouldFetchFromDbIfNotInCache() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Phones");
        CategoryResponseDto dto = new CategoryResponseDto(1L, "Phones");

        when(categoryCache.get(1L)).thenReturn(null);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(dto);

        CategoryResponseDto result = categoryService.findById(1L);

        assertEquals(dto, result);
        verify(categoryCache).put(1L, category);
    }

    @Test
    void findById_shouldThrowIfNotFound() {
        when(categoryCache.get(1L)).thenReturn(null);
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.findById(1L));
    }

    @Test
    void create_shouldSaveAndReturnDto() {
        CategoryRequestDto request = new CategoryRequestDto("Phones");
        Category entity = new Category();
        entity.setId(1L);
        entity.setName("Phones");
        CategoryResponseDto response = new CategoryResponseDto(1L, "Phones");

        when(categoryMapper.toEntity(request)).thenReturn(entity);
        when(categoryRepository.save(entity)).thenReturn(entity);
        when(categoryMapper.toDto(entity)).thenReturn(response);

        CategoryResponseDto result = categoryService.create(request);

        assertEquals(response, result);
        verify(categoryCache).put(1L, entity);
    }

    @Test
    void update_shouldUpdateAndReturnDto() {
        CategoryRequestDto request = new CategoryRequestDto("Smartphones");
        Category existing = new Category();
        existing.setId(1L);
        existing.setName("Phones");
        CategoryResponseDto response = new CategoryResponseDto(1L, "Smartphones");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(existing)).thenReturn(existing);
        when(categoryMapper.toDto(existing)).thenReturn(response);

        CategoryResponseDto result = categoryService.update(1L, request);

        assertEquals(response, result);
        verify(categoryCache).put(1L, existing);
        assertEquals("Smartphones", existing.getName());
    }

    @Test
    void update_shouldThrowIfNotFound() {
        CategoryRequestDto request = new CategoryRequestDto("Smartphones");

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.update(1L, request));
    }

    @Test
    void delete_shouldRemoveCategory() {
        categoryService.delete(1L);
        verify(categoryRepository).deleteById(1L);
        verify(categoryCache).remove(1L);
    }
}
