package com.example.inventory.repository;

import com.example.inventory.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Inventory repository interface.
 * This interface demonstrates:
 * - Spring Data JPA repository pattern in a microservice
 * - Custom query methods
 */
@Repository
public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {

    /**
     * Find inventory item by product code.
     */
    Optional<InventoryItem> findByProductCode(String productCode);

    /**
     * Find inventory items by product ID.
     */
    List<InventoryItem> findByProductId(Long productId);

    /**
     * Find inventory items by warehouse location.
     */
    List<InventoryItem> findByWarehouseLocation(String warehouseLocation);

    /**
     * Find inventory items with quantity less than the specified value.
     */
    List<InventoryItem> findByQuantityLessThan(Integer quantity);
}
