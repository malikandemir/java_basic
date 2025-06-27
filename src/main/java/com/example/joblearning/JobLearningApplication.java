package com.example.joblearning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Main Spring Boot application class.
 * This is the entry point for our Spring Boot application.
 * 
 * Features demonstrated:
 * - Spring Boot application setup
 * - Feign client integration for microservice communication
 */
@SpringBootApplication
@EnableFeignClients
public class JobLearningApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobLearningApplication.class, args);
    }
}
