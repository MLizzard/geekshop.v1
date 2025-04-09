package com.study.geekshop.controllers;

import com.study.geekshop.model.dto.request.ProductRequestDto;
import com.study.geekshop.model.dto.response.ProductResponseDto;
import com.study.geekshop.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductResponseDto> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ProductResponseDto findById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @GetMapping("/search/category/id")
    public ResponseEntity<List<ProductResponseDto>> findAllByCategoryId(@RequestParam Long categoryId) {
        return ResponseEntity.ok(productService.findAllByCategoryId(categoryId));
    }

    @GetMapping("/search/category/name")
    public ResponseEntity<List<ProductResponseDto>> findAllByCategoryName(@RequestParam String categoryName) {
        return ResponseEntity.ok(productService.findAllByCategoryName(categoryName));
    }

    @GetMapping("/filterByPriceAndCategory")
    public ResponseEntity<List<ProductResponseDto>> findAllByPriceRangeAndCategory(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice,
            @RequestParam String categoryName
    ){
        return ResponseEntity.ok(productService.findAllByPriceRangeAndCategoryName(
                minPrice,
                maxPrice,
                categoryName));
    }

    @PostMapping
    public ProductResponseDto create(@RequestBody ProductRequestDto dto) {
        return productService.create(dto);
    }

    @PutMapping("/{id}")
    public ProductResponseDto update(@PathVariable Long id, @RequestBody ProductRequestDto dto) {
        return productService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}
