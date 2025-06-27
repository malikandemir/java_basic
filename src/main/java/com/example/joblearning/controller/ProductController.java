package com.example.joblearning.controller;

import com.example.joblearning.model.Product;
import com.example.joblearning.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Product entity.
 * This class demonstrates:
 * - RESTful API implementation with Spring MVC
 * - HTTP method mappings (GET, POST, PUT, DELETE)
 * - Request parameter handling
 * - Response entity construction
 * - Bean validation integration
 */
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:4200") // For Angular frontend
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Get all products.
     * GET /api/products
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Get product by ID.
     * GET /api/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    /**
     * Create a new product.
     * POST /api/products
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    /**
     * Update an existing product.
     * PUT /api/products/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    /**
     * Delete a product.
     * DELETE /api/products/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Find products by category.
     * GET /api/products/category/{category}
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        List<Product> products = productService.getProductsByCategory(category);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Find products with price less than the specified value.
     * GET /api/products/price?max={price}
     */
    @GetMapping("/price")
    public ResponseEntity<List<Product>> getProductsByPrice(@RequestParam("max") Double price) {
        List<Product> products = productService.getProductsWithPriceLessThan(price);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Search products by name.
     * GET /api/products/search?name={name}
     */
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam("name") String name) {
        List<Product> products = productService.searchProductsByName(name);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
