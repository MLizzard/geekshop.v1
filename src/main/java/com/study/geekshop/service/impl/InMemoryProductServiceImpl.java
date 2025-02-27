package com.study.geekshop.service.impl;

import com.study.geekshop.model.Product;
import com.study.geekshop.repository.InMemoryProductDao;
import com.study.geekshop.service.ProductService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InMemoryProductServiceImpl implements ProductService {
    private final InMemoryProductDao repository;

    @Override
    public List<Product> findAllProducts() {
        return repository.findAllProducts();
    }

    public Product findByName(String name) {
        return repository.findByName(name);
    }

    public Product findByIndex(int index) {
        return repository.findByIndex(index);
    }

    public Product saveProduct(Product product) {
        return repository.saveProduct(product);
    }

    public Product updateProduct(Product product) {
        return repository.updateProduct(product);
    }

    public void deleteProduct(int index) {
        repository.deleteProduct(index);
    }
}
