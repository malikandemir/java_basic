package com.example.joblearning.controller;

import com.example.joblearning.model.Product;
import com.example.joblearning.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for ProductController.
 * This class demonstrates:
 * - Spring MVC Test framework
 * - Testing REST controllers
 * - JSON serialization/deserialization in tests
 * - Request/response validation
 */
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        // Set up test data
        product1 = new Product(1L, "Test Product 1", "Description 1", 19.99, "Electronics", 10);
        product2 = new Product(2L, "Test Product 2", "Description 2", 29.99, "Books", 20);
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() throws Exception {
        // Arrange
        List<Product> products = Arrays.asList(product1, product2);
        when(productService.getAllProducts()).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Test Product 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Test Product 2")));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    void getProductById_WithValidId_ShouldReturnProduct() throws Exception {
        // Arrange
        when(productService.getProductById(1L)).thenReturn(product1);

        // Act & Assert
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Test Product 1")))
                .andExpect(jsonPath("$.price", is(19.99)));

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    void createProduct_WithValidData_ShouldReturnCreatedProduct() throws Exception {
        // Arrange
        Product newProduct = new Product(null, "New Product", "New Description", 39.99, "Toys", 5);
        Product savedProduct = new Product(3L, "New Product", "New Description", 39.99, "Toys", 5);
        
        when(productService.createProduct(any(Product.class))).thenReturn(savedProduct);

        // Act & Assert
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("New Product")))
                .andExpect(jsonPath("$.price", is(39.99)));

        verify(productService, times(1)).createProduct(any(Product.class));
    }

    @Test
    void updateProduct_WithValidData_ShouldReturnUpdatedProduct() throws Exception {
        // Arrange
        Product updatedProduct = new Product(1L, "Updated Product", "Updated Description", 49.99, "Updated Category", 15);
        
        when(productService.updateProduct(anyLong(), any(Product.class))).thenReturn(updatedProduct);

        // Act & Assert
        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Updated Product")))
                .andExpect(jsonPath("$.price", is(49.99)));

        verify(productService, times(1)).updateProduct(eq(1L), any(Product.class));
    }

    @Test
    void deleteProduct_WithValidId_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(productService).deleteProduct(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(1L);
    }

    @Test
    void getProductsByCategory_ShouldReturnProductsInCategory() throws Exception {
        // Arrange
        List<Product> products = Arrays.asList(product1);
        when(productService.getProductsByCategory("Electronics")).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/api/products/category/Electronics"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].category", is("Electronics")));

        verify(productService, times(1)).getProductsByCategory("Electronics");
    }

    @Test
    void getProductsByPrice_ShouldReturnProductsWithLowerPrice() throws Exception {
        // Arrange
        List<Product> products = Arrays.asList(product1);
        when(productService.getProductsWithPriceLessThan(25.0)).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/api/products/price?max=25.0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].price", lessThan(25.0)));

        verify(productService, times(1)).getProductsWithPriceLessThan(25.0);
    }

    @Test
    void searchProducts_ShouldReturnMatchingProducts() throws Exception {
        // Arrange
        List<Product> products = Arrays.asList(product1, product2);
        when(productService.searchProductsByName("Test")).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/api/products/search?name=Test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", containsString("Test")))
                .andExpect(jsonPath("$[1].name", containsString("Test")));

        verify(productService, times(1)).searchProductsByName("Test");
    }
}
