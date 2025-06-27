package com.example.joblearning.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Feign client for communicating with the Inventory microservice.
 * This interface demonstrates:
 * - Declarative REST client with Feign
 * - Microservice communication
 * - Service discovery pattern
 */
@FeignClient(name = "inventory-service", url = "http://localhost:8081")
public interface InventoryClient {

    /**
     * Check if a product is in stock.
     * @param productCode The product code to check
     * @param quantity The required quantity
     * @return Response containing a map with "inStock" boolean
     */
    @GetMapping("/api/inventory/check-stock/{productCode}")
    ResponseEntity<Map<String, Boolean>> checkStock(
            @PathVariable("productCode") String productCode,
            @RequestParam(value = "quantity", defaultValue = "1") Integer quantity);
}
