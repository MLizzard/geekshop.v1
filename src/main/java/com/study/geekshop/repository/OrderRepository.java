package com.study.geekshop.repository;

import com.study.geekshop.model.entity.Order;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"user"})
    List<Order> findAll();

    @EntityGraph(attributePaths = {"user"})
    Order findById(long id);
}