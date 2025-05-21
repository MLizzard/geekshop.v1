package com.study.geekshop.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

// com.study.geekshop.config.StorageConfig.java
@Configuration
@ConfigurationProperties(prefix = "storage")
public class StorageConfig {
    private String location = "uploads";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
