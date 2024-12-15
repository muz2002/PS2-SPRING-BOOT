# Inventory Management System

## Overview
This Spring Boot application provides a RESTful API for managing products, categories, and inventory levels.  
It includes authentication and authorization via Spring Security and sends email notifications when products reach low stock levels.

## Features
- **CRUD Operations**:
    - Products, Categories
- **Inventory Management**:
    - Add/Deduct stock
    - Check stock levels
- **Filtering and Searching**:
    - Filter products by category
    - Search products by name
- **Low Stock Reports**:
    - Retrieve low-stock products (default threshold: 10)
    - Sends an email notification to a configured email recipient
- **Validation**:
    - Input validation using `javax.validation`
- **Authentication & Authorization (Role-Based)**:
    - Uses Spring Security with in-memory users
    - `admin:admin123` role ADMIN (can CRUD)
    - `user:user123` role USER (read-only access to products)
- **Documentation**:
    - Swagger/OpenAPI for API documentation
- **Email Notifications**:
    - Configured SMTP server sends notifications for low-stock products

## Prerequisites
- Java 17+
- Gradle
- MySQL Database (create one named `inventory_db` or update application.properties accordingly)

## Setup
1. Update `src/main/resources/application.properties` with your MySQL and SMTP email credentials.
2. Run `./gradlew bootRun`.

## Accessing the Application
- API Base URL: `http://localhost:8080`
- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- OpenAPI Docs: [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

## Authentication
- **Basic Auth** is enabled.
- Use `admin:admin123` to login as Admin (full access).
- Use `user:user123` to login as User (read-only to product endpoints).

**Examples**:
- `curl -u admin:admin123 http://localhost:8080/api/products`
- `curl -u user:user123 http://localhost:8080/api/products`

## Email Notifications
When you hit the endpoint `/api/inventory/low-stock` and there are products with low stock (default threshold 10), an email will be sent to the configured recipient (`notification.email.recipient` in `application.properties`).

## Endpoints (Example)
- **Categories**:
    - `POST /api/categories` (Admin only)
    - `GET /api/categories/{id}` (Admin only)
- **Products**:
    - `POST /api/products` (Admin only)
    - `GET /api/products` (Admin, User)
    - `GET /api/products/{id}` (Admin, User)
- **Inventory**:
    - `POST /api/inventory/add/{productId}` (Admin only)
    - `POST /api/inventory/deduct/{productId}` (Admin only)
    - `GET /api/inventory/low-stock` (Admin only, triggers email if low stock found)

## Future Enhancements
- Persist users and roles in a database
- Implement token-based authentication (JWT) for stateless security
- Advanced notifications (SMS, Slack, etc.)
- Deployment instructions for cloud platforms
### Swagger / Health Check (No Auth Needed)
GET http://localhost:8080/swagger-ui/index.html

### Products (Requires Auth)
### As Admin (admin:admin123), we can do CRUD operations

# List all products
GET http://localhost:8080/api/products
Authorization: Basic YWRtaW46YWRtaW4xMjM=
Accept: application/json

### Create a new product (Admin only)
POST http://localhost:8080/api/products
Authorization: Basic YWRtaW46YWRtaW4xMjM=
Content-Type: application/json

{
"name": "Sample Product",
"price": 19.99,
"categoryId": 1,
"quantity": 100
}

### Get product by ID (Admin or User)
GET http://localhost:8080/api/products/1
Authorization: Basic YWRtaW46YWRtaW4xMjM=
Accept: application/json

### Update a product (Admin only)
PUT http://localhost:8080/api/products/1
Authorization: Basic YWRtaW46YWRtaW4xMjM=
Content-Type: application/json

{
"name": "Updated Product Name",
"price": 24.99,
"categoryId": 1,
"quantity": 150
}

### Delete a product (Admin only)
DELETE http://localhost:8080/api/products/1
Authorization: Basic YWRtaW46YWRtaW4xMjM=

### As User (user:user123), only GET requests should work on products
# List products as user
GET http://localhost:8080/api/products
Authorization: Basic dXNlcjp1c2VyMTIz
Accept: application/json

# Try to create product as user (Should fail)
POST http://localhost:8080/api/products
Authorization: Basic dXNlcjp1c2VyMTIz
Content-Type: application/json

{
"name": "Failing Product",
"price": 9.99,
"categoryId": 1,
"quantity": 10
}

### Categories (Admin only)
### Create Category
POST http://localhost:8080/api/categories
Authorization: Basic YWRtaW46YWRtaW4xMjM=
Content-Type: application/json

{
"name": "Electronics"
}

### Get Category by ID
GET http://localhost:8080/api/categories/1
Authorization: Basic YWRtaW46YWRtaW4xMjM=
Accept: application/json

### Update Category
PUT http://localhost:8080/api/categories/1
Authorization: Basic YWRtaW46YWRtaW4xMjM=
Content-Type: application/json

{
"name": "Updated Electronics"
}

### Delete Category
DELETE http://localhost:8080/api/categories/1
Authorization: Basic YWRtaW46YWRtaW4xMjM=

### Inventory (Admin only)
### Add stock to product
POST http://localhost:8080/api/inventory/add/2
Authorization: Basic YWRtaW46YWRtaW4xMjM=
Content-Type: application/json

{
"quantity": 20
}

### Deduct stock from product
POST http://localhost:8080/api/inventory/deduct/2
Authorization: Basic YWRtaW46YWRtaW4xMjM=
Content-Type: application/json

{
"quantity": 5
}

### Get stock level
GET http://localhost:8080/api/inventory/stock-level/1
Authorization: Basic YWRtaW46YWRtaW4xMjM=

### Low Stock (triggers email if configured)
GET http://localhost:8080/api/inventory/low-stock?threshold=10
Authorization: Basic YWRtaW46YWRtaW4xMjM=
