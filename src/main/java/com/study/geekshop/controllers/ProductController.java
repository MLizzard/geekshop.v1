package com.study.geekshop.controllers;

import com.study.geekshop.exceptions.ProductNotFoundException;
import com.study.geekshop.model.Product;
import com.study.geekshop.service.ProductService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService = new ProductService();

    @GetMapping
    public List<Product> findAllProducts() {
        return productService.findAllProducts();
    }

    // Найти товар по имени (Query Parameter)
    @GetMapping("/search")
    public Product findByName(@RequestParam String name) {
        Product product = productService.findByName(name);
        if (product == null) {
            throw new ProductNotFoundException("Продукт не найден");
        }
        return product;
    }

    // Найти товар по индексу (Path Parameter)
    @GetMapping("/{index}")
    public Product findByIndex(@PathVariable int index) {
        Product product = productService.findByIndex(index);
        if (product == null) {
            throw new ProductNotFoundException("Некорректный индекс");
        }
        return product;
    }
}