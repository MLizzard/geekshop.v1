package com.study.geekshop.repository;

import com.study.geekshop.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByName(String name);

    @Query("SELECT p FROM Product p JOIN p.category c WHERE c.id = :categoryId")
    List<Product> findAllByCategoryId(Long categoryId);

    @Query(value = "SELECT p. * FROM products p "
                    + "JOIN categories c ON p.category_id = c.id "
                    + "WHERE c.name = :categoryName", nativeQuery = true)
    List<Product> findAllByCategoryName(String categoryName);

    @Query("SELECT p FROM Product p "
            + "JOIN p.category c "
            + "WHERE p.price BETWEEN :minPrice AND :maxPrice AND c.name = :categoryName")
    List<Product> findProductsByPriceRangeAndCategoryId(Double minPrice,
                                                        Double maxPrice,
                                                        String categoryName);

}