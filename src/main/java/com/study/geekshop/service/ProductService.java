package com.study.geekshop.service;

import com.study.geekshop.model.Product;
import java.util.ArrayList;
import java.util.List;

public interface ProductService {
    final List<Product> products = new ArrayList<Product>();

    public List<Product> findAllProducts();

    public Product findByName(String name);

    public Product findByIndex(int index);

    Product saveProduct(Product product);

    Product updateProduct(Product product);

    void deleteProduct(int index);
}