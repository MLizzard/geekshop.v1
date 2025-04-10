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
import com.study.geekshop.service.ProductService;
import com.study.geekshop.service.mapper.ProductMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import java.util.List;

@Primary
@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final ProductCache productCache;

    @Override
    public List<ProductResponseDto> findAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Override
    public ProductResponseDto findById(Long id) {
        Product productFromCache = productCache.get(id);
        if (productFromCache != null) {
            return productMapper.toDto(productFromCache);
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Продукт не найден"));
        productCache.put(id, product);
        return productMapper.toDto(product);
    }

    @Override
    public List<ProductResponseDto> findAllByCategoryId(Long id) {
        return productRepository.findAllByCategoryId(id)
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Override
    public List<ProductResponseDto> findAllByCategoryName(String name) {
        return productRepository.findAllByCategoryName(name)
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Override
    public List<ProductResponseDto> findAllByPriceRangeAndCategoryName(
            Double minPrice,
            Double maxPrice,
            String categoryName) {
        return productRepository
                .findProductsByPriceRangeAndCategoryId(
                        minPrice,
                        maxPrice,
                        categoryName)
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Override
    public ProductResponseDto create(ProductRequestDto dto) {
        Product product = productMapper.toEntity(dto);
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена"));
        product.setCategory(category);
        product = productRepository.save(product);
        productCache.put(product.getId(), product);
        return productMapper.toDto(product);
    }

    @Override
    public ProductResponseDto update(Long id, ProductRequestDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Продукт не найден"));

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setInStock(dto.getInStock());

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена"));
        product.setCategory(category);
        productCache.put(id, product);
        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
        productCache.remove(id);
    }
}