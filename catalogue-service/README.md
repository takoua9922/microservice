# Catalogue Service

Gestion Catalogue microservice built with Spring Boot. This service manages product catalog data and exposes RESTful APIs for CRUD operations on products.

## Features

- Spring Boot 3 (Java 17)
- PostgreSQL with Spring Data JPA
- REST API for managing products
- Eureka client registration
- Spring Cloud Config client support
- Dockerfile for containerization

## Prerequisites

- Java 17
- Maven 3.9+
- PostgreSQL database
- Running Spring Cloud Config Server (optional if using local configuration)
- Running Eureka Server for service discovery

## Running Locally

```bash
mvn spring-boot:run
```

Override database, Eureka, and Config Server connection properties via environment variables such as `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`, `EUREKA_SERVER_URL`, and `CONFIG_SERVER_URI`.

## Building the Docker Image

```bash
mvn -DskipTests package
docker build -t catalogue-service:latest .
```

## API Endpoints

- `GET /api/catalogue/products`
- `GET /api/catalogue/products/{id}`
- `POST /api/catalogue/products`
- `PUT /api/catalogue/products/{id}`
- `DELETE /api/catalogue/products/{id}`

## License

This project is provided as-is without warranty.
