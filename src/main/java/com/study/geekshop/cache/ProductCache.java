package com.study.geekshop.cache;

import com.study.geekshop.model.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductCache extends LfuCache<Product> {
    public ProductCache() {
        super(100);
    }
}
