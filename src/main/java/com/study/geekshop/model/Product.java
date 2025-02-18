package com.study.geekshop.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {
    private String name;       // Название товара
    private double price;      // Цена
    private String description; // Описание
    private String category;   // Категория (например, "Манга", "Настольные игры", "Фигурки")
    private boolean inStock;   // Есть в наличии или нет
}
