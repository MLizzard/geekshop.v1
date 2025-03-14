package com.study.geekshop.repository;

import com.study.geekshop.model.entity.Product;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryProductDao {

    private final List<Product> products = new ArrayList<>();

    public List<Product> findAllProducts() {
        return products;
    }

    public Product findByName(String name) {
        for (Product product : products) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }

    public Product findById(Long id) { // Find by ID
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }

    public Product saveProduct(Product product) {
        products.add(product);
        return product;
    }

    public Product updateProduct(Product product) {
        var index = products.indexOf(product);
        if (index != -1) {
            products.set(index, product);
            return product;
        }
        return null;
    }

    public void deleteProduct(Long id) { // Delete by ID
        products.removeIf(product -> product.getId().equals(id));
    }
}
