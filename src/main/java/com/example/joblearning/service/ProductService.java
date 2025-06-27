package com.example.joblearning.service;

import com.example.joblearning.client.InventoryClient;
import com.example.joblearning.model.Product;
import com.example.joblearning.repository.ProductRepository;
import com.example.joblearning.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Product service class.
 * This class demonstrates:
 * - Service layer in Spring architecture
 * - Transaction management
 * - Business logic implementation
 * - Integration with repository layer
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final InventoryClient inventoryClient;

    @Autowired
    public ProductService(ProductRepository productRepository, InventoryClient inventoryClient) {
        this.productRepository = productRepository;
        this.inventoryClient = inventoryClient;
    }

    /**
     * Get all products.
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Get product by ID.
     * @throws ResourceNotFoundException if product not found
     */
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    /**
     * Create a new product.
     */
    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Update an existing product.
     * @throws ResourceNotFoundException if product not found
     */
    @Transactional
    public Product updateProduct(Long id, Product productDetails) {
        Product product = getProductById(id);
        
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setCategory(productDetails.getCategory());
        product.setStockQuantity(productDetails.getStockQuantity());
        
        return productRepository.save(product);
    }

    /**
     * Delete a product.
     * @throws ResourceNotFoundException if product not found
     */
    @Transactional
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    /**
     * Find products by category.
     */
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    /**
     * Find products with price less than the specified value.
     */
    public List<Product> getProductsWithPriceLessThan(Double price) {
        return productRepository.findByPriceLessThan(price);
    }

    /**
     * Search products by name (case insensitive).
     */
    public List<Product> searchProductsByName(String name) {
        return productRepository.searchByNameContainingIgnoreCase(name);
    }
    
    /**
     * Check if a product is in stock.
     * This method demonstrates microservice communication using Feign client.
     * 
     * @param productCode The product code to check
     * @param quantity The required quantity
     * @return true if the product is in stock with the required quantity, false otherwise
     */
    public boolean isProductInStock(String productCode, Integer quantity) {
        try {
            ResponseEntity<Map<String, Boolean>> response = inventoryClient.checkStock(productCode, quantity);
            if (response.getBody() != null) {
                return response.getBody().getOrDefault("inStock", false);
            }
            return false;
        } catch (Exception e) {
            // Handle communication errors (e.g., inventory service is down)
            // In a real-world scenario, you might want to implement circuit breaker pattern
            // using libraries like Resilience4j or Spring Cloud Circuit Breaker
            return false;
        }
    }
}
