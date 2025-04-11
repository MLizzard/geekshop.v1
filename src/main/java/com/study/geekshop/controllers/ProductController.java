package com.study.geekshop.controllers;

import com.study.geekshop.model.dto.request.ProductRequestDto;
import com.study.geekshop.model.dto.response.ProductResponseDto;
import com.study.geekshop.service.ProductService;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "API for managing product information")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Get all products",
            description = "Returns a list of all available products.")
    public List<ProductResponseDto> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID",
            description = "Returns a product based on the provided identifier.")
    public ProductResponseDto findById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @GetMapping("/search/category/id")
    @Operation(summary = "Find products by category ID",
            description = "Returns a list of products belonging to a specific category ID.")
    public ResponseEntity<List<ProductResponseDto>> findAllByCategoryId(
            @RequestParam Long categoryId) {
        return ResponseEntity.ok(productService.findAllByCategoryId(categoryId));
    }

    @GetMapping("/search/category/name")
    @Operation(summary = "Find products by category name",
            description = "Returns a list of products belonging to a specific category name.")
    public ResponseEntity<List<ProductResponseDto>> findAllByCategoryName(
            @RequestParam String categoryName) {
        return ResponseEntity.ok(productService.findAllByCategoryName(categoryName));
    }

    @GetMapping("/filterByPriceAndCategory")
    @Operation(summary = "Find products by price range and category",
            description = "Returns a list of products within "
                        + "a specified price range and belonging to a specific category.")
    public ResponseEntity<List<ProductResponseDto>> findAllByPriceRangeAndCategory(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice,
            @RequestParam String categoryName
    ) {
        return ResponseEntity.ok(productService.findAllByPriceRangeAndCategoryName(
                minPrice,
                maxPrice,
                categoryName));
    }

    @PostMapping
    @Operation(summary = "Create a new product",
            description = "Creates a new product based on the provided data.")
    public ProductResponseDto create(@Valid @RequestBody ProductRequestDto dto) {
        return productService.create(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing product",
            description = "Updates the data of an existing product based on the provided identifier.")
    public ProductResponseDto update(@PathVariable Long id,
                                     @Valid @RequestBody ProductRequestDto dto) {
        return productService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product",
            description = "Deletes a product based on the provided identifier.")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}
