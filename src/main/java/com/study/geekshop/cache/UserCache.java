package com.study.geekshop.cache;

import com.study.geekshop.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserCache extends LfuCache<User> {
    public UserCache() {
        super(100);
    }
}
