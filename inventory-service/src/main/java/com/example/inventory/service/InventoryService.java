package com.example.inventory.service;

import com.example.inventory.model.InventoryItem;
import com.example.inventory.repository.InventoryRepository;
import com.example.inventory.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Inventory service class.
 * This class demonstrates:
 * - Service layer in a microservice architecture
 * - Transaction management
 * - Business logic implementation
 */
@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * Get all inventory items.
     */
    public List<InventoryItem> getAllInventoryItems() {
        return inventoryRepository.findAll();
    }

    /**
     * Get inventory item by ID.
     * @throws ResourceNotFoundException if item not found
     */
    public InventoryItem getInventoryItemById(Long id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with id: " + id));
    }

    /**
     * Get inventory item by product code.
     * @throws ResourceNotFoundException if item not found
     */
    public InventoryItem getInventoryItemByProductCode(String productCode) {
        return inventoryRepository.findByProductCode(productCode)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with product code: " + productCode));
    }

    /**
     * Get inventory items by product ID.
     */
    public List<InventoryItem> getInventoryItemsByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId);
    }

    /**
     * Create a new inventory item.
     */
    @Transactional
    public InventoryItem createInventoryItem(InventoryItem inventoryItem) {
        return inventoryRepository.save(inventoryItem);
    }

    /**
     * Update an existing inventory item.
     * @throws ResourceNotFoundException if item not found
     */
    @Transactional
    public InventoryItem updateInventoryItem(Long id, InventoryItem inventoryItemDetails) {
        InventoryItem inventoryItem = getInventoryItemById(id);
        
        inventoryItem.setProductCode(inventoryItemDetails.getProductCode());
        inventoryItem.setQuantity(inventoryItemDetails.getQuantity());
        inventoryItem.setWarehouseLocation(inventoryItemDetails.getWarehouseLocation());
        inventoryItem.setProductId(inventoryItemDetails.getProductId());
        
        return inventoryRepository.save(inventoryItem);
    }

    /**
     * Delete an inventory item.
     * @throws ResourceNotFoundException if item not found
     */
    @Transactional
    public void deleteInventoryItem(Long id) {
        InventoryItem inventoryItem = getInventoryItemById(id);
        inventoryRepository.delete(inventoryItem);
    }

    /**
     * Update inventory quantity.
     * @throws ResourceNotFoundException if item not found
     */
    @Transactional
    public InventoryItem updateInventoryQuantity(String productCode, Integer quantityChange) {
        InventoryItem inventoryItem = getInventoryItemByProductCode(productCode);
        
        int newQuantity = inventoryItem.getQuantity() + quantityChange;
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Cannot reduce quantity below zero");
        }
        
        inventoryItem.setQuantity(newQuantity);
        return inventoryRepository.save(inventoryItem);
    }

    /**
     * Check if product is in stock.
     */
    public boolean isInStock(String productCode, Integer requiredQuantity) {
        try {
            InventoryItem item = getInventoryItemByProductCode(productCode);
            return item.getQuantity() >= requiredQuantity;
        } catch (ResourceNotFoundException e) {
            return false;
        }
    }

    /**
     * Get low stock items (items with quantity below threshold).
     */
    public List<InventoryItem> getLowStockItems(Integer threshold) {
        return inventoryRepository.findByQuantityLessThan(threshold);
    }
}
