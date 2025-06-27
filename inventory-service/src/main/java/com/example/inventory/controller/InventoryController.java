package com.example.inventory.controller;

import com.example.inventory.model.InventoryItem;
import com.example.inventory.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for Inventory service.
 * This class demonstrates:
 * - RESTful API implementation in a microservice
 * - HTTP method mappings
 * - Request parameter handling
 * - Response entity construction
 */
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * Get all inventory items.
     * GET /api/inventory
     */
    @GetMapping
    public ResponseEntity<List<InventoryItem>> getAllInventoryItems() {
        List<InventoryItem> items = inventoryService.getAllInventoryItems();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    /**
     * Get inventory item by ID.
     * GET /api/inventory/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<InventoryItem> getInventoryItemById(@PathVariable Long id) {
        InventoryItem item = inventoryService.getInventoryItemById(id);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    /**
     * Get inventory item by product code.
     * GET /api/inventory/product-code/{productCode}
     */
    @GetMapping("/product-code/{productCode}")
    public ResponseEntity<InventoryItem> getInventoryItemByProductCode(@PathVariable String productCode) {
        InventoryItem item = inventoryService.getInventoryItemByProductCode(productCode);
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    /**
     * Get inventory items by product ID.
     * GET /api/inventory/product/{productId}
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<InventoryItem>> getInventoryItemsByProductId(@PathVariable Long productId) {
        List<InventoryItem> items = inventoryService.getInventoryItemsByProductId(productId);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    /**
     * Create a new inventory item.
     * POST /api/inventory
     */
    @PostMapping
    public ResponseEntity<InventoryItem> createInventoryItem(@Valid @RequestBody InventoryItem inventoryItem) {
        InventoryItem createdItem = inventoryService.createInventoryItem(inventoryItem);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    /**
     * Update an existing inventory item.
     * PUT /api/inventory/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<InventoryItem> updateInventoryItem(@PathVariable Long id, @Valid @RequestBody InventoryItem inventoryItem) {
        InventoryItem updatedItem = inventoryService.updateInventoryItem(id, inventoryItem);
        return new ResponseEntity<>(updatedItem, HttpStatus.OK);
    }

    /**
     * Delete an inventory item.
     * DELETE /api/inventory/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventoryItem(@PathVariable Long id) {
        inventoryService.deleteInventoryItem(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Update inventory quantity.
     * PATCH /api/inventory/quantity/{productCode}
     */
    @PatchMapping("/quantity/{productCode}")
    public ResponseEntity<InventoryItem> updateInventoryQuantity(
            @PathVariable String productCode,
            @RequestBody Map<String, Integer> request) {
        
        Integer quantityChange = request.get("quantityChange");
        if (quantityChange == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        InventoryItem updatedItem = inventoryService.updateInventoryQuantity(productCode, quantityChange);
        return new ResponseEntity<>(updatedItem, HttpStatus.OK);
    }

    /**
     * Check if product is in stock.
     * GET /api/inventory/check-stock/{productCode}?quantity={quantity}
     */
    @GetMapping("/check-stock/{productCode}")
    public ResponseEntity<Map<String, Boolean>> checkStock(
            @PathVariable String productCode,
            @RequestParam(defaultValue = "1") Integer quantity) {
        
        boolean inStock = inventoryService.isInStock(productCode, quantity);
        return new ResponseEntity<>(Map.of("inStock", inStock), HttpStatus.OK);
    }

    /**
     * Get low stock items.
     * GET /api/inventory/low-stock?threshold={threshold}
     */
    @GetMapping("/low-stock")
    public ResponseEntity<List<InventoryItem>> getLowStockItems(@RequestParam(defaultValue = "5") Integer threshold) {
        List<InventoryItem> lowStockItems = inventoryService.getLowStockItems(threshold);
        return new ResponseEntity<>(lowStockItems, HttpStatus.OK);
    }
}
