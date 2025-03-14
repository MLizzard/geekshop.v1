package com.study.geekshop.repository;

import com.study.geekshop.model.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"user", "products"})
    List<Order> findAll();

    @EntityGraph(attributePaths = {"user", "products"})
    Order findById(long id);
}