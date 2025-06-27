package com.example.inventory.controller;

import com.example.inventory.model.InventoryItem;
import com.example.inventory.service.InventoryService;
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
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for InventoryController.
 * This class demonstrates:
 * - Spring MVC Test framework in a microservice
 * - Testing REST controllers
 * - JSON serialization/deserialization in tests
 */
@WebMvcTest(InventoryController.class)
public class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private InventoryItem item1;
    private InventoryItem item2;

    @BeforeEach
    void setUp() {
        // Set up test data
        item1 = new InventoryItem(1L, "PROD-001", 10, "Warehouse A", 101L);
        item2 = new InventoryItem(2L, "PROD-002", 20, "Warehouse B", 102L);
    }

    @Test
    void getAllInventoryItems_ShouldReturnAllItems() throws Exception {
        // Arrange
        List<InventoryItem> items = Arrays.asList(item1, item2);
        when(inventoryService.getAllInventoryItems()).thenReturn(items);

        // Act & Assert
        mockMvc.perform(get("/api/inventory"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].productCode", is("PROD-001")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].productCode", is("PROD-002")));

        verify(inventoryService, times(1)).getAllInventoryItems();
    }

    @Test
    void getInventoryItemById_WithValidId_ShouldReturnItem() throws Exception {
        // Arrange
        when(inventoryService.getInventoryItemById(1L)).thenReturn(item1);

        // Act & Assert
        mockMvc.perform(get("/api/inventory/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.productCode", is("PROD-001")))
                .andExpect(jsonPath("$.quantity", is(10)));

        verify(inventoryService, times(1)).getInventoryItemById(1L);
    }

    @Test
    void getInventoryItemByProductCode_WithValidCode_ShouldReturnItem() throws Exception {
        // Arrange
        when(inventoryService.getInventoryItemByProductCode("PROD-001")).thenReturn(item1);

        // Act & Assert
        mockMvc.perform(get("/api/inventory/product-code/PROD-001"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.productCode", is("PROD-001")))
                .andExpect(jsonPath("$.quantity", is(10)));

        verify(inventoryService, times(1)).getInventoryItemByProductCode("PROD-001");
    }

    @Test
    void createInventoryItem_WithValidData_ShouldReturnCreatedItem() throws Exception {
        // Arrange
        InventoryItem newItem = new InventoryItem(null, "PROD-003", 5, "Warehouse C", 103L);
        InventoryItem savedItem = new InventoryItem(3L, "PROD-003", 5, "Warehouse C", 103L);
        
        when(inventoryService.createInventoryItem(any(InventoryItem.class))).thenReturn(savedItem);

        // Act & Assert
        mockMvc.perform(post("/api/inventory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newItem)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.productCode", is("PROD-003")))
                .andExpect(jsonPath("$.quantity", is(5)));

        verify(inventoryService, times(1)).createInventoryItem(any(InventoryItem.class));
    }

    @Test
    void updateInventoryItem_WithValidData_ShouldReturnUpdatedItem() throws Exception {
        // Arrange
        InventoryItem updatedItem = new InventoryItem(1L, "PROD-001-UPDATED", 15, "Warehouse D", 101L);
        
        when(inventoryService.updateInventoryItem(anyLong(), any(InventoryItem.class))).thenReturn(updatedItem);

        // Act & Assert
        mockMvc.perform(put("/api/inventory/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedItem)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.productCode", is("PROD-001-UPDATED")))
                .andExpect(jsonPath("$.quantity", is(15)));

        verify(inventoryService, times(1)).updateInventoryItem(eq(1L), any(InventoryItem.class));
    }

    @Test
    void deleteInventoryItem_WithValidId_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(inventoryService).deleteInventoryItem(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/inventory/1"))
                .andExpect(status().isNoContent());

        verify(inventoryService, times(1)).deleteInventoryItem(1L);
    }

    @Test
    void updateInventoryQuantity_WithValidData_ShouldReturnUpdatedItem() throws Exception {
        // Arrange
        InventoryItem updatedItem = new InventoryItem(1L, "PROD-001", 15, "Warehouse A", 101L);
        
        when(inventoryService.updateInventoryQuantity(eq("PROD-001"), anyInt())).thenReturn(updatedItem);

        // Act & Assert
        mockMvc.perform(patch("/api/inventory/quantity/PROD-001")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"quantityChange\": 5}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.productCode", is("PROD-001")))
                .andExpect(jsonPath("$.quantity", is(15)));

        verify(inventoryService, times(1)).updateInventoryQuantity(eq("PROD-001"), eq(5));
    }

    @Test
    void checkStock_WithInStockItem_ShouldReturnTrue() throws Exception {
        // Arrange
        when(inventoryService.isInStock("PROD-001", 5)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(get("/api/inventory/check-stock/PROD-001?quantity=5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.inStock", is(true)));

        verify(inventoryService, times(1)).isInStock("PROD-001", 5);
    }

    @Test
    void checkStock_WithOutOfStockItem_ShouldReturnFalse() throws Exception {
        // Arrange
        when(inventoryService.isInStock("PROD-001", 15)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(get("/api/inventory/check-stock/PROD-001?quantity=15"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.inStock", is(false)));

        verify(inventoryService, times(1)).isInStock("PROD-001", 15);
    }

    @Test
    void getLowStockItems_ShouldReturnItemsBelowThreshold() throws Exception {
        // Arrange
        List<InventoryItem> lowStockItems = Arrays.asList(item1);
        when(inventoryService.getLowStockItems(15)).thenReturn(lowStockItems);

        // Act & Assert
        mockMvc.perform(get("/api/inventory/low-stock?threshold=15"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].productCode", is("PROD-001")))
                .andExpect(jsonPath("$[0].quantity", is(10)));

        verify(inventoryService, times(1)).getLowStockItems(15);
    }
}
