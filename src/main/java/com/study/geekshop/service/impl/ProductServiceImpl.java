package com.study.geekshop.service.impl;

import com.study.geekshop.model.Product;
import com.study.geekshop.repository.ProductRepository;
import com.study.geekshop.service.ProductService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Primary
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;

    @Override
    public List<Product> findAllProducts() {
        return repository.findAll();
    }

    @Override
    public Product findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Product findByIndex(int index) {
        List<Product> products = repository.findAll();
        if (index < products.size() && index >= 0) {
            return products.get(index);
        }
        return null;
    }

    @Override
    public Product saveProduct(Product product) {
        return repository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        return repository.save(product);
    }

    @Override
    public void deleteProduct(int index) {
        List<Product> products = repository.findAll();
        if (index < products.size() && index >= 0) {
            products.remove(index);
        }
    }
}
