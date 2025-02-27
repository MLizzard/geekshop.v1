package com.study.geekshop.controllers;

import com.study.geekshop.exceptions.ProductNotFoundException;
import com.study.geekshop.model.Product;
import com.study.geekshop.service.ProductService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService service;

    @GetMapping
    public List<Product> findAllProducts() {
        return service.findAllProducts();
    }

    // Найти товар по имени (Query Parameter)
    @GetMapping("/search")
    public Product findByName(@RequestParam String name) {
        Product product = service.findByName(name);
        if (product == null) {
            throw new ProductNotFoundException("Продукт не найден");
        }
        return product;
    }

    // Найти товар по индексу (Path Parameter)
    @GetMapping("/{index}")
    public Product findByIndex(@PathVariable int index) {
        Product product = service.findByIndex(index);
        if (product == null) {
            throw new ProductNotFoundException("Некорректный индекс");
        }
        return product;
    }

    @PostMapping("save_product")
    public Product saveProduct(@RequestBody Product product) {
        return service.saveProduct(product);
    }

    @PutMapping("update_product")
    public Product updateProduct(@RequestBody Product product) {
        return service.updateProduct(product);
    }

    @DeleteMapping("delete_product/{index}")
    public void deleteProduct(@PathVariable int index) {
        service.deleteProduct(index);
    }
}