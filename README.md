# Java Learning Project

A comprehensive Java Spring Boot project demonstrating modern enterprise application development skills including microservices architecture, REST APIs, JPA, and frontend integration.

## Project Overview

This project is designed as a learning platform for Java job skills, showcasing a microservices-based e-commerce system with:

- **Product Service**: Main service for product management
- **Inventory Service**: Microservice for inventory management
- **Angular Frontend**: Modern web interface for the application

## Technology Stack

### Backend
- Java 17
- Spring Boot 3.1.0
- Spring Data JPA
- Spring Cloud OpenFeign for service communication
- H2 Database (in-memory)
- Maven for dependency management
- JUnit and Spring Boot Test for testing

### Frontend
- Angular
- TypeScript
- HTML/SCSS

## Project Structure

```
/
├── frontend/                      # Angular frontend application
├── inventory-service/             # Inventory microservice
│   ├── src/main/java/
│   │   └── com/example/inventory/
│   │       ├── controller/        # REST controllers
│   │       ├── model/             # Entity classes
│   │       ├── repository/        # Data access layer
│   │       ├── service/           # Business logic
│   │       └── exception/         # Custom exceptions
│   └── src/test/                  # Test classes
├── src/                           # Main product service
│   ├── main/java/
│   │   └── com/example/joblearning/
│   │       ├── client/            # Feign clients for service communication
│   │       ├── controller/        # REST controllers
│   │       ├── model/             # Entity classes
│   │       ├── repository/        # Data access layer
│   │       ├── service/           # Business logic
│   │       └── exception/         # Custom exceptions
│   └── test/                      # Test classes
└── pom.xml                        # Main Maven configuration
```

## Key Features

1. **RESTful API Implementation**
   - CRUD operations for products
   - Resource validation
   - Exception handling

2. **Microservices Architecture**
   - Service-to-service communication using Feign clients
   - Independent deployment capabilities

3. **Data Persistence**
   - JPA/Hibernate for ORM
   - Repository pattern implementation
   - Transaction management

4. **Testing**
   - Unit testing with JUnit
   - Integration testing with Spring Boot Test

5. **Frontend Integration**
   - Angular-based SPA
   - REST API consumption
   - Responsive design

## Running the Application

### Prerequisites
- Java 17 or higher
- Maven
- Node.js and npm (for frontend)

### Backend Services
1. Start the Product Service:
   ```
   cd /path/to/project
   mvn spring-boot:run
   ```

2. Start the Inventory Service:
   ```
   cd /path/to/project/inventory-service
   mvn spring-boot:run
   ```

### Frontend
1. Install dependencies:
   ```
   cd /path/to/project/frontend
   npm install
   ```

2. Start the development server:
   ```
   npm start
   ```

## Learning Objectives

This project demonstrates:
- Spring Boot application development
- Microservices architecture principles
- REST API design and implementation
- Data persistence with JPA
- Service-to-service communication
- Frontend-backend integration
- Testing strategies for Java applications
