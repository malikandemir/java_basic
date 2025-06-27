package com.example.joblearning.service;

import com.example.joblearning.exception.ResourceNotFoundException;
import com.example.joblearning.model.Product;
import com.example.joblearning.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ProductService.
 * This class demonstrates:
 * - JUnit 5 for unit testing
 * - Mockito for mocking dependencies
 * - Test-driven development practices
 */
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        // Set up test data
        product1 = new Product(1L, "Test Product 1", "Description 1", 19.99, "Electronics", 10);
        product2 = new Product(2L, "Test Product 2", "Description 2", 29.99, "Books", 20);
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        // Arrange
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Test Product 1", result.get(0).getName());
        assertEquals("Test Product 2", result.get(1).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProductById_WithValidId_ShouldReturnProduct() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        // Act
        Product result = productService.getProductById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Product 1", result.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getProductById_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById(999L);
        });
        verify(productRepository, times(1)).findById(999L);
    }

    @Test
    void createProduct_ShouldReturnSavedProduct() {
        // Arrange
        Product newProduct = new Product(null, "New Product", "New Description", 39.99, "Toys", 5);
        Product savedProduct = new Product(3L, "New Product", "New Description", 39.99, "Toys", 5);
        
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // Act
        Product result = productService.createProduct(newProduct);

        // Assert
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("New Product", result.getName());
        verify(productRepository, times(1)).save(newProduct);
    }

    @Test
    void updateProduct_WithValidId_ShouldReturnUpdatedProduct() {
        // Arrange
        Product updatedDetails = new Product(1L, "Updated Product", "Updated Description", 49.99, "Updated Category", 15);
        
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.save(any(Product.class))).thenReturn(updatedDetails);

        // Act
        Product result = productService.updateProduct(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(49.99, result.getPrice());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void deleteProduct_WithValidId_ShouldDeleteProduct() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        doNothing().when(productRepository).delete(any(Product.class));

        // Act
        productService.deleteProduct(1L);

        // Assert
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).delete(product1);
    }

    @Test
    void getProductsByCategory_ShouldReturnProductsInCategory() {
        // Arrange
        when(productRepository.findByCategory("Electronics")).thenReturn(Arrays.asList(product1));

        // Act
        List<Product> result = productService.getProductsByCategory("Electronics");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getCategory());
        verify(productRepository, times(1)).findByCategory("Electronics");
    }

    @Test
    void getProductsWithPriceLessThan_ShouldReturnProductsWithLowerPrice() {
        // Arrange
        when(productRepository.findByPriceLessThan(25.0)).thenReturn(Arrays.asList(product1));

        // Act
        List<Product> result = productService.getProductsWithPriceLessThan(25.0);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.get(0).getPrice() < 25.0);
        verify(productRepository, times(1)).findByPriceLessThan(25.0);
    }

    @Test
    void searchProductsByName_ShouldReturnMatchingProducts() {
        // Arrange
        when(productRepository.searchByNameContainingIgnoreCase("Test")).thenReturn(Arrays.asList(product1, product2));

        // Act
        List<Product> result = productService.searchProductsByName("Test");

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.get(0).getName().contains("Test"));
        assertTrue(result.get(1).getName().contains("Test"));
        verify(productRepository, times(1)).searchByNameContainingIgnoreCase("Test");
    }
}
