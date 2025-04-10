package com.study.geekshop.cache;

import com.study.geekshop.model.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderCache extends LfuCache<Order> {
    public OrderCache() {
        super(100);
    }
}
