package com.study.geekshop.controllers;

import com.study.geekshop.model.dto.request.ProductRequestDto;
import com.study.geekshop.model.dto.response.ProductResponseDto;
import com.study.geekshop.service.ProductService;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
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
    public ResponseEntity<List<ProductResponseDto>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID",
            description = "Returns a product based on the provided identifier.")
    public ResponseEntity<ProductResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
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
    public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody ProductRequestDto dto) {
        return ResponseEntity.ok(productService.create(dto));
    }

    @PostMapping("/bulk")
    @Operation(summary = "Bulk create products",
            description = "Создаёт несколько продуктов за один запрос.")
    public ResponseEntity<List<ProductResponseDto>> createAll(
            @Valid @RequestBody List<ProductRequestDto> dtos) {
        return ResponseEntity.ok(productService.createAll(dtos));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing product",
            description = "Updates the data of an existing product based on the provided identifier.")
    public ResponseEntity<ProductResponseDto> update(@PathVariable Long id,
                                     @Valid @RequestBody ProductRequestDto dto) {
        return ResponseEntity.ok(productService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product",
            description = "Deletes a product based on the provided identifier.")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDto> createProductWithImage(
            @ModelAttribute ProductRequestDto productDto) {
        return ResponseEntity.ok(productService.createProductWithImage(productDto));
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getProductImage(@PathVariable Long id) {
        Resource image = productService.getProductImage(id);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // или другой подходящий тип
                .body(image);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDto> updateProductWithImage(
            @PathVariable Long id,
            @ModelAttribute ProductRequestDto productDto) {
        return ResponseEntity.ok(productService.updateProductWithImage(id, productDto));
    }
}
