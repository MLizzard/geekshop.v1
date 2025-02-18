package com.study.geekshop.service;

import com.study.geekshop.model.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final List<Product> products = List.of(
            // Манга
            Product.builder()
                    .name("Beastars")
                    .price(40.0)
                    .description("Манга про мир антропоморфных животных и их взаимоотношения.")
                    .category("Манга")
                    .inStock(true)
                    .build(),

            Product.builder()
                    .name("Tokyo Ghoul")
                    .price(20.0)
                    .description("Мрачная история про каннибалов-гуляй и борьбу за выживание.")
                    .category("Манга")
                    .inStock(true)
                    .build(),

            // Настольные игры
            Product.builder()
                    .name("Манчкин")
                    .price(30.0)
                    .description("Настольная карточная игра с юмором и пародиями на ролевые игры.")
                    .category("Настольные игры")
                    .inStock(false)
                    .build(),

            Product.builder()
                    .name("Dungeons & Dragons Starter Set")
                    .price(50.0)
                    .description("Набор для начинающих игроков в D&D, содержащий правила и кубики.")
                    .category("Настольные игры")
                    .inStock(true)
                    .build(),

            // Фигурки
            Product.builder()
                    .name("Фигурка Гоку (Dragon Ball)")
                    .price(60.0)
                    .description("Фигурка Гоку в супер-саянской форме.")
                    .category("Фигурки")
                    .inStock(true)
                    .build(),

            Product.builder()
                    .name("Фигурка Танжиро (Demon Slayer)")
                    .price(55.0)
                    .description("Качественная фигурка главного героя из Demon Slayer.")
                    .category("Фигурки")
                    .inStock(false)
                    .build(),

            // Электроника
            Product.builder()
                    .name("Игровая клавиатура Razer")
                    .price(120.0)
                    .description("Механическая клавиатура с RGB-подсветкой.")
                    .category("Электроника")
                    .inStock(true)
                    .build(),

            Product.builder()
                    .name("Геймерская мышь Logitech G502")
                    .price(80.0)
                    .description("Игровая мышь с настраиваемыми кнопками и сенсором HERO.")
                    .category("Электроника")
                    .inStock(true)
                    .build(),

            // Мерч
            Product.builder()
                    .name("Футболка Cyberpunk 2077")
                    .price(35.0)
                    .description("Стильная футболка с принтом из Cyberpunk 2077.")
                    .category("Одежда и мерч")
                    .inStock(true)
                    .build(),

            Product.builder()
                    .name("Толстовка The Witcher")
                    .price(60.0)
                    .description("Худи с символикой Ведьмака.")
                    .category("Одежда и мерч")
                    .inStock(true)
                    .build()
    );

    public List<Product> findAllProducts() {
        return products;
    }

    public Product findByName(String name) {
        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(name)) {
                return product; // Если нашли, сразу возвращаем продукт
            }
        }
        return null; // Если не нашли, возвращаем null
    }

    public Product findByIndex(int index) {
        if (index >= 0 && index < products.size()) {
            return products.get(index); // Если индекс в пределах списка, возвращаем продукт
        }
        return null; // Если индекс некорректный, возвращаем null
    }
}