package com.study.geekshop.service;

import com.study.geekshop.model.Product;
import com.study.geekshop.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> findAllProducts() {
        return productRepository.findAllProducts();
    }

    public Optional<Product> findByName(String name) {
        return productRepository.findByName(name);
    }

    public Optional<Product> findByIndex(int index) {
        return productRepository.findByIndex(index);
    }
}
