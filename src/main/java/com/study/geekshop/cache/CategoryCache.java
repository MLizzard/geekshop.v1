package com.study.geekshop.cache;

import com.study.geekshop.model.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryCache extends LfuCache<Category> {
    public CategoryCache() {
        super(100);
    }
}
