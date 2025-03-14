package com.study.geekshop.service.impl;

import com.study.geekshop.model.dto.request.ProductRequestDTO;
import com.study.geekshop.model.dto.response.ProductResponseDTO;
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

    @Override
    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDTO)
                .toList();
    }

    @Override
    public ProductResponseDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Продукт не найден"));
        return productMapper.toDTO(product);
    }

    @Override
    public ProductResponseDTO create(ProductRequestDTO dto) {
        Product product = productMapper.toEntity(dto);
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Категория не найдена"));
        product.setCategory(category);
        product = productRepository.save(product);
        return productMapper.toDTO(product);
    }

    @Override
    public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Продукт не найден"));

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setInStock(dto.isInStock());

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Категория не найдена"));
        product.setCategory(category);

        return productMapper.toDTO(productRepository.save(product));
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}