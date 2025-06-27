package com.example.inventory.service;

import com.example.inventory.exception.ResourceNotFoundException;
import com.example.inventory.model.InventoryItem;
import com.example.inventory.repository.InventoryRepository;
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
 * Unit tests for InventoryService.
 * This class demonstrates:
 * - JUnit 5 for unit testing in a microservice
 * - Mockito for mocking dependencies
 * - Test-driven development practices
 */
@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    private InventoryItem item1;
    private InventoryItem item2;

    @BeforeEach
    void setUp() {
        // Set up test data
        item1 = new InventoryItem(1L, "PROD-001", 10, "Warehouse A", 101L);
        item2 = new InventoryItem(2L, "PROD-002", 20, "Warehouse B", 102L);
    }

    @Test
    void getAllInventoryItems_ShouldReturnAllItems() {
        // Arrange
        when(inventoryRepository.findAll()).thenReturn(Arrays.asList(item1, item2));

        // Act
        List<InventoryItem> result = inventoryService.getAllInventoryItems();

        // Assert
        assertEquals(2, result.size());
        assertEquals("PROD-001", result.get(0).getProductCode());
        assertEquals("PROD-002", result.get(1).getProductCode());
        verify(inventoryRepository, times(1)).findAll();
    }

    @Test
    void getInventoryItemById_WithValidId_ShouldReturnItem() {
        // Arrange
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(item1));

        // Act
        InventoryItem result = inventoryService.getInventoryItemById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("PROD-001", result.getProductCode());
        verify(inventoryRepository, times(1)).findById(1L);
    }

    @Test
    void getInventoryItemById_WithInvalidId_ShouldThrowException() {
        // Arrange
        when(inventoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            inventoryService.getInventoryItemById(999L);
        });
        verify(inventoryRepository, times(1)).findById(999L);
    }

    @Test
    void getInventoryItemByProductCode_WithValidCode_ShouldReturnItem() {
        // Arrange
        when(inventoryRepository.findByProductCode("PROD-001")).thenReturn(Optional.of(item1));

        // Act
        InventoryItem result = inventoryService.getInventoryItemByProductCode("PROD-001");

        // Assert
        assertNotNull(result);
        assertEquals("PROD-001", result.getProductCode());
        verify(inventoryRepository, times(1)).findByProductCode("PROD-001");
    }

    @Test
    void getInventoryItemByProductCode_WithInvalidCode_ShouldThrowException() {
        // Arrange
        when(inventoryRepository.findByProductCode(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            inventoryService.getInventoryItemByProductCode("INVALID-CODE");
        });
        verify(inventoryRepository, times(1)).findByProductCode("INVALID-CODE");
    }

    @Test
    void createInventoryItem_ShouldReturnSavedItem() {
        // Arrange
        InventoryItem newItem = new InventoryItem(null, "PROD-003", 5, "Warehouse C", 103L);
        InventoryItem savedItem = new InventoryItem(3L, "PROD-003", 5, "Warehouse C", 103L);
        
        when(inventoryRepository.save(any(InventoryItem.class))).thenReturn(savedItem);

        // Act
        InventoryItem result = inventoryService.createInventoryItem(newItem);

        // Assert
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("PROD-003", result.getProductCode());
        verify(inventoryRepository, times(1)).save(newItem);
    }

    @Test
    void updateInventoryItem_WithValidId_ShouldReturnUpdatedItem() {
        // Arrange
        InventoryItem updatedDetails = new InventoryItem(1L, "PROD-001-UPDATED", 15, "Warehouse D", 101L);
        
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(inventoryRepository.save(any(InventoryItem.class))).thenReturn(updatedDetails);

        // Act
        InventoryItem result = inventoryService.updateInventoryItem(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals("PROD-001-UPDATED", result.getProductCode());
        assertEquals(15, result.getQuantity());
        assertEquals("Warehouse D", result.getWarehouseLocation());
        verify(inventoryRepository, times(1)).findById(1L);
        verify(inventoryRepository, times(1)).save(any(InventoryItem.class));
    }

    @Test
    void deleteInventoryItem_WithValidId_ShouldDeleteItem() {
        // Arrange
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(item1));
        doNothing().when(inventoryRepository).delete(any(InventoryItem.class));

        // Act
        inventoryService.deleteInventoryItem(1L);

        // Assert
        verify(inventoryRepository, times(1)).findById(1L);
        verify(inventoryRepository, times(1)).delete(item1);
    }

    @Test
    void updateInventoryQuantity_WithValidData_ShouldUpdateQuantity() {
        // Arrange
        when(inventoryRepository.findByProductCode("PROD-001")).thenReturn(Optional.of(item1));
        
        InventoryItem updatedItem = new InventoryItem(1L, "PROD-001", 15, "Warehouse A", 101L);
        when(inventoryRepository.save(any(InventoryItem.class))).thenReturn(updatedItem);

        // Act
        InventoryItem result = inventoryService.updateInventoryQuantity("PROD-001", 5);

        // Assert
        assertNotNull(result);
        assertEquals(15, result.getQuantity());
        verify(inventoryRepository, times(1)).findByProductCode("PROD-001");
        verify(inventoryRepository, times(1)).save(any(InventoryItem.class));
    }

    @Test
    void updateInventoryQuantity_WithNegativeResult_ShouldThrowException() {
        // Arrange
        when(inventoryRepository.findByProductCode("PROD-001")).thenReturn(Optional.of(item1));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            inventoryService.updateInventoryQuantity("PROD-001", -15);
        });
        verify(inventoryRepository, times(1)).findByProductCode("PROD-001");
        verify(inventoryRepository, never()).save(any(InventoryItem.class));
    }

    @Test
    void isInStock_WithSufficientQuantity_ShouldReturnTrue() {
        // Arrange
        when(inventoryRepository.findByProductCode("PROD-001")).thenReturn(Optional.of(item1));

        // Act
        boolean result = inventoryService.isInStock("PROD-001", 5);

        // Assert
        assertTrue(result);
        verify(inventoryRepository, times(1)).findByProductCode("PROD-001");
    }

    @Test
    void isInStock_WithInsufficientQuantity_ShouldReturnFalse() {
        // Arrange
        when(inventoryRepository.findByProductCode("PROD-001")).thenReturn(Optional.of(item1));

        // Act
        boolean result = inventoryService.isInStock("PROD-001", 15);

        // Assert
        assertFalse(result);
        verify(inventoryRepository, times(1)).findByProductCode("PROD-001");
    }

    @Test
    void isInStock_WithNonExistentProduct_ShouldReturnFalse() {
        // Arrange
        when(inventoryRepository.findByProductCode("NON-EXISTENT")).thenReturn(Optional.empty());

        // Act
        boolean result = inventoryService.isInStock("NON-EXISTENT", 5);

        // Assert
        assertFalse(result);
        verify(inventoryRepository, times(1)).findByProductCode("NON-EXISTENT");
    }

    @Test
    void getLowStockItems_ShouldReturnItemsBelowThreshold() {
        // Arrange
        when(inventoryRepository.findByQuantityLessThan(15)).thenReturn(Arrays.asList(item1));

        // Act
        List<InventoryItem> result = inventoryService.getLowStockItems(15);

        // Assert
        assertEquals(1, result.size());
        assertEquals("PROD-001", result.get(0).getProductCode());
        verify(inventoryRepository, times(1)).findByQuantityLessThan(15);
    }
}
