package com.example.joblearning.repository;

import com.example.joblearning.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Product repository interface.
 * This interface demonstrates:
 * - Spring Data JPA repository pattern
 * - Automatic CRUD operations provided by JpaRepository
 * - Custom query methods
 * - JPQL queries
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Find products by category.
     * Spring Data JPA will automatically implement this method based on the name.
     */
    List<Product> findByCategory(String category);

    /**
     * Find products with price less than the specified value.
     */
    List<Product> findByPriceLessThan(Double price);

    /**
     * Find products with stock quantity greater than the specified value.
     */
    List<Product> findByStockQuantityGreaterThan(Integer quantity);

    /**
     * Custom JPQL query to find products by name containing the specified string (case insensitive).
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> searchByNameContainingIgnoreCase(String name);
}
