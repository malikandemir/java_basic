package com.example.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Main Spring Boot application class for the Inventory Microservice.
 * This class demonstrates:
 * - Spring Boot application setup for microservices
 * - Feign client integration for service-to-service communication
 */
@SpringBootApplication
@EnableFeignClients
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }
}
