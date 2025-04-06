# Currency Exchange Microservices Application

This project demonstrates a microservices architecture for currency exchange and conversion, with JWT authentication for securing endpoints.

## Project Overview

The application consists of the following microservices:

1. **Eureka Server**: Service discovery for registering and locating services
2. **Config Server**: Centralized configuration for all services
3. **Currency Exchange Service**: Provides exchange rates between currencies
4. **Currency Conversion Service**: Converts amounts between different currencies
5. **API Gateway**: Entry point for all client requests with JWT authentication

## Prerequisites

- Java 17
- Maven
- Git

## Running the Application

Follow these steps in the exact order to run the application properly:

### 1. Start the Eureka Server (Service Registry)

```bash
cd eureka-server
mvn spring-boot:run
```

Access the Eureka dashboard at: http://localhost:8761

### 2. Start the Config Server

```bash
cd config-server
mvn spring-boot:run
```

The Config Server runs on port 8888.

### 3. Start the Currency Exchange Service

```bash
cd currency-exchange-service
mvn spring-boot:run
```

This service provides exchange rates between currencies and runs on port 8000.

### 4. Start the API Gateway

```bash
cd api-gateway
mvn spring-boot:run
```

The API Gateway runs on port 8765 and handles all incoming requests.

### 5. Start the Currency Conversion Service

```bash
cd currency-conversion-service
mvn spring-boot:run
```

This service converts amounts between currencies using exchange rates from the Currency Exchange Service and runs on port 8101.

## Authentication

The application uses JWT for authentication. To use the services:

1. Obtain a JWT token by making a POST request to the authentication endpoint:

```
POST http://localhost:8765/authenticate
Content-Type: application/json

{
  "username": "user",
  "password": "password"
}
```

2. Add the returned token to subsequent requests in the Authorization header:

```
Authorization: Bearer <your_token>
```

## API Endpoints

After authentication, the following endpoints are available:

### Currency Exchange Service
```
GET http://localhost:8765/currency-exchange/from/{from}/to/{to}
```
Example: http://localhost:8765/currency-exchange/from/USD/to/INR

### Currency Conversion Service
```
GET http://localhost:8765/currency-conversion/from/{from}/to/{to}/quantity/{quantity}
```
Example: http://localhost:8765/currency-conversion/from/USD/to/INR/quantity/10

## Architecture

- **Service Discovery**: Eureka Server enables services to find and communicate with each other
- **Centralized Configuration**: Config Server manages configuration for all services
- **API Gateway**: Single entry point with routing and security
- **Circuit Breaker**: Handles failures in service-to-service communication
- **JWT Authentication**: Secures all endpoints with token-based authentication

## Project Structure

```
currency-exchange/
├── api-gateway/                 # API Gateway service
├── config-server/               # Configuration server
├── config-files/                # Local configuration files
├── currency-exchange-service/   # Exchange rates service
├── currency-conversion-service/ # Currency conversion service 
├── currency-exchange-common/    # Shared library with common code
└── eureka-server/               # Service discovery server
```

## Troubleshooting

If you encounter issues:

1. Ensure you're starting services in the correct order
2. Verify all services are registered with Eureka
3. Check the JWT token is correctly included in API requests
4. Review logs for detailed error messages

## Security Implementation

This project implements JWT (JSON Web Token) security with these components:

- **JwtUtil**: Handles token generation and validation
- **Authentication Controller**: Provides endpoints for obtaining JWT tokens
- **JWT Filter**: Intercepts requests to validate tokens
- **Security Configuration**: Configures protected routes and authentication rules

For development purposes, a default user is configured:
- Username: **user**
- Password: **password** 