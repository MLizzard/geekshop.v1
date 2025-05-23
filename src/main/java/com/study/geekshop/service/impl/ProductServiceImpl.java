package com.study.geekshop.service.impl;

import com.study.geekshop.cache.ProductCache;
import com.study.geekshop.exceptions.CategoryNotFoundException;
import com.study.geekshop.exceptions.ProductNotFoundException;
import com.study.geekshop.exceptions.StorageException;
import com.study.geekshop.model.dto.request.ProductRequestDto;
import com.study.geekshop.model.dto.response.ProductResponseDto;
import com.study.geekshop.model.entity.Category;
import com.study.geekshop.model.entity.Product;
import com.study.geekshop.repository.CategoryRepository;
import com.study.geekshop.repository.ProductRepository;
import com.study.geekshop.service.ProductService;
import com.study.geekshop.service.StorageService;
import com.study.geekshop.service.mapper.ProductMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
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
    private final StorageService storageService;

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
    public List<ProductResponseDto> createAll(List<ProductRequestDto> dtos) {
        return dtos.stream()
                .map(dto -> {
                    Product product = productMapper.toEntity(dto);

                    Category category = categoryRepository.findById(dto.getCategoryId())
                            .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена"));

                    product.setCategory(category);
                    Product saved = productRepository.save(product);
                    productCache.put(saved.getId(), saved);
                    return productMapper.toDto(saved);
                })
                .toList();
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

    @Override
    @Transactional
    public ProductResponseDto createProductWithImage(ProductRequestDto productDto) {
        Product product = productMapper.toEntity(productDto);
        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Категория не найдена"));
        product.setCategory(category);

        // обработка изображения
        if (productDto.getImage() != null && !productDto.getImage().isEmpty()) {
            String filename = storageService.store(productDto.getImage());
            product.setImageUrl(filename);
        }

        Product savedProduct = productRepository.save(product);
        productCache.put(savedProduct.getId(), savedProduct);
        return productMapper.toDto(savedProduct);
    }

    @Override
    @Transactional
    public ProductResponseDto updateProductWithImage(Long id, ProductRequestDto productDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id.toString()));

        existingProduct.setName(productDto.getName());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setInStock(productDto.getInStock());

        if (productDto.getImage() != null && !productDto.getImage().isEmpty()) {
            if (existingProduct.getImageUrl() != null) {
                storageService.delete(existingProduct.getImageUrl());
            }

            // Сохраняем новое
            String newFilename = storageService.store(productDto.getImage());
            existingProduct.setImageUrl(newFilename);
        }

        Product updatedProduct = productRepository.save(existingProduct);
        productCache.put(updatedProduct.getId(), updatedProduct);
        return productMapper.toDto(updatedProduct);
    }

    @Override
    public Resource getProductImage(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new StorageException("Product not found"));
        if (product.getImageUrl() == null) {
            throw new StorageException("Image not found for product");
        }
        return storageService.loadAsResource(product.getImageUrl());
    }
}
