package com.study.geekshop.service;

import com.study.geekshop.model.dto.request.ProductRequestDTO;
import com.study.geekshop.model.dto.response.ProductResponseDTO;
import com.study.geekshop.model.entity.Product;
import java.util.List;

public interface ProductService {
    List<ProductResponseDTO> findAll();
    ProductResponseDTO findById(Long id);
    ProductResponseDTO create(ProductRequestDTO dto);
    ProductResponseDTO update(Long id, ProductRequestDTO dto);
    void delete(Long id);
}
