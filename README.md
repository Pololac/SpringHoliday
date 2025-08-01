# SpringHoliday

Starter REST API for leave/holiday management built with **Spring Boot 3**, **Spring Security (JWT + refresh token)**, **JPA/Hibernate**, and **MySQL**.

## Table of Contents
- [Features](#features)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Configuration](#configuration)
  - [Run](#run)
- [Authentication](#authentication)
  - [Login](#login)
  - [Automatic token injection in Postman](#automatic-token-injection-in-postman)
  - [Refresh token](#refresh-token)
- [Domain](#domain)
  - [Booking DTOs](#booking-dtos)
- [Database](#database)
- [API Quick Reference](#api-quick-reference)
- [Development](#development)
- [License](#license)

## Features

- JWT-based authentication with access token (Bearer) and **refresh token in HTTP-only cookie**.
- Protected routes for authenticated users and admin-only access.
- Booking flow using DTOs with validation (`@NotNull`, `@Future`, etc.).
- MySQL persistence with JPA/Hibernate.
- Spring Boot 3 configuration with `SecurityFilterChain` (stateless, JWT filter).

## Architecture

- **Security**: `SecurityFilterChain` configured with route-based authorization and a custom `JwtFilter` placed before `UsernamePasswordAuthenticationFilter`.  
- **JWT**: `JwtUtil` for signing/validating tokens, `AuthService`/`AuthController` for login and refresh.  
- **Domain**: Entities such as `Booking`, related to `Room` and `User`; mapping via MapStruct mappers.  
- **Validation**: Jakarta Bean Validation for request DTOs.

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.6+
- MySQL (default examples assume `localhost:8889`)

### Configuration

Create an `env.properties` at the **project root** to store DB credentials:

```properties
# env.properties
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
```

Edit `src/main/resources/application.properties` to read those values and point to your database:

```properties
spring.datasource.url=jdbc:mysql://localhost:8889/springholiday
spring.datasource.username=${spring.datasource.username}
spring.datasource.password=${spring.datasource.password}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Optional: log SQL params
# logging.level.org.hibernate.SQL=debug
# logging.level.org.hibernate.orm.jdbc.bind=trace
```

> If you use profiles, mirror these settings in `application-{profile}.properties`.

### Run

```bash
# Build
mvn clean package

# Run
java -jar target/*.jar
# or
mvn spring-boot:run
```

The API will start on `http://localhost:8080` (unless configured otherwise).

## Authentication

### Login

**Request**
```
POST /api/login
Content-Type: application/json
```
```json
{
  "email": "user@example.com",
  "password": "secret"
}
```

**Response**
```json
{
  "token": "eyJhbGciOi...",
  "user": {
    "id": "uuid",
    "email": "user@example.com",
    "role": "ROLE_USER"
  }
}
```
The **refresh token** is sent as an **HTTP-only cookie** (e.g. `refreshToken`).

### Automatic token injection in Postman

At the **Collection** level, set **Authorization → Bearer Token** and use:
```
{{jwt_token}}
```
Then, in your **Login** request **Post-request** script, save values to environment variables:

```javascript
const body = pm.response.json();

if (body.token) {
  pm.environment.set("jwt_token", body.token);
}
if (body.user) {
  pm.environment.set("user_id", body.user.id);
  pm.environment.set("user_email", body.user.email);
  pm.environment.set("user_role", body.user.role);
}

// Optional: capture refresh cookie
const cookie = pm.cookies.get("refreshToken");
if (cookie) {
  pm.environment.set("refresh_token", cookie);
}
```

### Refresh token

If your refresh endpoint returns the **access token as raw body**, use this **Post-request** script on that request:

```javascript
const newJwt = pm.response.text().trim();
if (newJwt) {
  pm.environment.set("jwt_token", newJwt);
}

// If a new refresh cookie is sent:
const refreshedCookie = pm.cookies.get("refreshToken");
if (refreshedCookie) {
  pm.environment.set("refresh_token", refreshedCookie);
}
```

## Domain

### Booking DTOs

Dates must be sent as ISO strings in JSON, e.g.:

```json
{
  "startDate": "2025-11-03",
  "endDate": "2025-11-05",
  "guestCount": 3,
  "rooms": [
    { "id": "0416ae64-bac0-481e-859c-02708de6272b", "number": "2B", "capacity": 4, "price": 125.00 }
  ]
}
```

## Database

Create the schema in MySQL and grant access to your user:

```sql
CREATE DATABASE IF NOT EXISTS springholiday CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
GRANT ALL ON springholiday.* TO 'YOUR_DB_USERNAME'@'localhost';
FLUSH PRIVILEGES;
```

Then start the app; with `spring.jpa.hibernate.ddl-auto=update`, tables will be created automatically.  
If you use seed data, add SQL files under a `data/` folder or integrate Flyway/Liquibase.

## API Quick Reference

> Paths may vary depending on your controllers; adjust here if needed.

- `POST /api/login` — authenticate and receive JWT (+ refresh cookie)  
- `POST /api/refresh` — get a new JWT using the refresh cookie  
- `GET  /api/account` — current user info (requires `Authorization: Bearer {{jwt_token}}`)  
- `POST /api/bookings` — create a booking (authenticated)  

## Development

- Java version: 17+  
- Build tool: Maven  
- Primary dependencies: Spring Boot Web, Security, Validation, Data JPA, MySQL driver, MapStruct (for mapping).

## License
MIT (or your preferred license). Update this section if you use a different license.
