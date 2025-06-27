package com.example.joblearning.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Product entity class.
 * This class demonstrates:
 * - JPA annotations for ORM (Object-Relational Mapping)
 * - Hibernate integration with Spring Boot
 * - Bean validation using Jakarta Validation
 * - Lombok annotations to reduce boilerplate code
 */
@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    @NotNull(message = "Product price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    private String category;
    
    private Integer stockQuantity;
}
