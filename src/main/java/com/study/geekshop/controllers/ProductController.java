package com.study.geekshop.controllers;

import com.study.geekshop.exceptions.ProductNotFoundException;
import com.study.geekshop.model.Product;
import com.study.geekshop.service.ProductService;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<Product> findAllProducts() {
        return productService.findAllProducts();
    }

    // Найти товар по имени (Query Parameter)
    @GetMapping("/search")
    public Optional<Product> findByName(@RequestParam String name) {
        Optional<Product> product = productService.findByName(name);
        if (product.isEmpty()) {
            throw new ProductNotFoundException("Продукт не найден");
        }
        return product;
    }

    // Найти товар по индексу (Path Parameter)
    @GetMapping("/{index}")
    public Optional<Product> findByIndex(@PathVariable int index) {
        Optional<Product> product = productService.findByIndex(index);
        if (product.isEmpty()) {
            throw new ProductNotFoundException("Некорректный индекс");
        }
        return product;
    }
}