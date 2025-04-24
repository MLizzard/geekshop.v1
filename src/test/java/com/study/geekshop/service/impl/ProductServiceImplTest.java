package com.study.geekshop.service.impl;

import com.study.geekshop.cache.ProductCache;
import com.study.geekshop.exceptions.CategoryNotFoundException;
import com.study.geekshop.exceptions.ProductNotFoundException;
import com.study.geekshop.model.dto.request.ProductRequestDto;
import com.study.geekshop.model.dto.response.ProductResponseDto;
import com.study.geekshop.model.entity.Category;
import com.study.geekshop.model.entity.Product;
import com.study.geekshop.repository.CategoryRepository;
import com.study.geekshop.repository.ProductRepository;
import com.study.geekshop.service.mapper.ProductMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductCache productCache;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductRequestDto createRequestDto() {
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName("Test");
        dto.setPrice(100.0);
        dto.setInStock(true);
        dto.setDescription("desc");
        dto.setCategoryId(1L);
        return dto;
    }

    private Product createProduct() {
        Product product = new Product();
        Category category = new Category();
        category.setId(1L);
        product.setName("Test category");
        product.setId(1L);
        product.setName("Test");
        product.setPrice(100.0);
        product.setDescription("desc");
        product.setInStock(true);
        product.setCategory(category);
        return product;
    }

    private ProductResponseDto createResponseDto() {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(1L);
        dto.setName("Test");
        return dto;
    }
    @Test
    void createAll_shouldSaveAllProductsAndReturnDtos() {
        // Arrange
        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setName("Phone");
        requestDto.setPrice(499.99);
        requestDto.setDescription("Smartphone");
        requestDto.setInStock(true);
        requestDto.setCategoryId(1L);

        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        Product product = new Product();
        product.setId(100L);
        product.setName("Phone");
        product.setCategory(category);

        ProductResponseDto responseDto = new ProductResponseDto();
        responseDto.setId(100L);
        responseDto.setName("Phone");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productMapper.toEntity(requestDto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(responseDto);

        // Act
        List<ProductResponseDto> result = productService.createAll(List.of(requestDto));

        // Assert
        assertEquals(1, result.size());
        assertEquals("Phone", result.get(0).getName());

        verify(categoryRepository).findById(1L);
        verify(productRepository).save(product);
        verify(productCache).put(100L, product);
    }

    @Test
    void createAll_shouldThrowExceptionWhenCategoryNotFound() {
        // Arrange
        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setCategoryId(42L);

        when(categoryRepository.findById(42L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CategoryNotFoundException.class, () -> {
            productService.createAll(List.of(requestDto));
        });

        verify(productRepository, never()).save(any());
    }

    @Test
    void findAll() {
        Product product = new Product();
        product.setName("Phone");
        product.setPrice(499.99);
        product.setDescription("Smartphone");
        product.setInStock(true);
        product.setId(1L);

        when(productRepository.findAll()).thenReturn(List.of(product));
        List <ProductResponseDto> result = productService.findAll();
        assertEquals(1, result.size());

    }

    @Test
    void testFindById_FromCache() {
        Product product = createProduct();
        ProductResponseDto responseDto = createResponseDto();
        when(productCache.get(1L)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(responseDto);

        ProductResponseDto result = productService.findById(1L);

        assertEquals("Test", result.getName());
    }

    @Test
    void testFindById_FromRepository() {
        Product product = createProduct();
        ProductResponseDto responseDto = createResponseDto();
        when(productCache.get(1L)).thenReturn(null);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(responseDto);

        ProductResponseDto result = productService.findById(1L);

        verify(productCache).put(1L, product);
        assertEquals("Test", result.getName());
    }

    @Test
    void testFindById_NotFound() {
        when(productCache.get(1L)).thenReturn(null);
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.findById(1L));
    }

    @Test
    void testFindAllByCategoryId() {
        Product product = createProduct();
        ProductResponseDto responseDto = createResponseDto();
        when(productRepository.findAllByCategoryId(1L)).thenReturn(List.of(product));
        when(productMapper.toDto(product)).thenReturn(responseDto);

        List<ProductResponseDto> result = productService.findAllByCategoryId(1L);

        assertEquals(1, result.size());
    }

    @Test
    void testFindAllByCategoryName() {
        Product product = createProduct();
        ProductResponseDto responseDto = createResponseDto();
        when(productRepository.findAllByCategoryName("Test")).thenReturn(List.of(product));
        when(productMapper.toDto(product)).thenReturn(responseDto);

        List<ProductResponseDto> result = productService.findAllByCategoryName("Test");

        assertEquals(1, result.size());
    }

    @Test
    void testFindAllByPriceRangeAndCategoryName() {
        Product product = createProduct();
        ProductResponseDto responseDto = createResponseDto();
        when(productRepository.findProductsByPriceRangeAndCategoryId(50.0, 150.0, "Test")).thenReturn(List.of(product));
        when(productMapper.toDto(product)).thenReturn(responseDto);

        List<ProductResponseDto> result = productService.findAllByPriceRangeAndCategoryName(50.0, 150.0, "Test");

        assertEquals(1, result.size());
    }

    @Test
    void testCreate() {
        ProductRequestDto dto = createRequestDto();
        Product product = createProduct();
        ProductResponseDto responseDto = createResponseDto();
        when(productMapper.toEntity(dto)).thenReturn(product);
        when(categoryRepository.findById(dto.getCategoryId())).thenReturn(Optional.of(product.getCategory()));
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(responseDto);

        ProductResponseDto result = productService.create(dto);

        verify(productCache).put(1L, product);
        assertEquals("Test", result.getName());
    }

    @Test
    void testCreate_CategoryNotFound() {
        ProductRequestDto dto = createRequestDto();
        Product product = createProduct();
        when(productMapper.toEntity(dto)).thenReturn(product);
        when(categoryRepository.findById(dto.getCategoryId())).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> productService.create(dto));
    }

    @Test
    void testCreateAll() {
        ProductRequestDto dto = createRequestDto();
        Product product = createProduct();
        ProductResponseDto responseDto = createResponseDto();
        when(productMapper.toEntity(dto)).thenReturn(product);
        when(categoryRepository.findById(dto.getCategoryId())).thenReturn(Optional.of(product.getCategory()));
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(responseDto);

        List<ProductResponseDto> result = productService.createAll(List.of(dto));

        verify(productCache).put(1L, product);
        assertEquals(1, result.size());
    }

    @Test
    void testUpdate() {
        ProductRequestDto dto = createRequestDto();
        Product product = createProduct();
        ProductResponseDto responseDto = createResponseDto();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(dto.getCategoryId())).thenReturn(Optional.of(product.getCategory()));
        when(productRepository.save(any())).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(responseDto);

        ProductResponseDto result = productService.update(1L, dto);

        verify(productCache).put(1L, product);
        assertEquals("Test", result.getName());
    }

    @Test
    void testUpdate_ProductNotFound() {
        ProductRequestDto dto = createRequestDto();
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.update(1L, dto));
    }

    @Test
    void testUpdate_CategoryNotFound() {
        ProductRequestDto dto = createRequestDto();
        Product product = createProduct();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(dto.getCategoryId())).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> productService.update(1L, dto));
    }

    @Test
    void testDelete() {
        productService.delete(1L);
        verify(productRepository).deleteById(1L);
        verify(productCache).remove(1L);
    }

}
