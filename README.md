# Enterprise Task Manager - Backend

## Introduction

### Solution Overview

This repository contains the backend application/API for the Enterprise Task Manager application. It is built with Java using the Spring Boot framework and provides RESTful API's. It handles business logic, data persistence using Spring Data JPA connected to a PostgreSQL database (hosted on Supabase), and security management via Spring Security integrating with Supabase for authentication. It interacts with the [Enterprise Task Manager Frontend](https://github.com/Ross-T/enterprise-task-manager-frontend).

### Project Aim & Objectives

**Main Aim:** To develop a secure, scalable, robust, and performance-optimised backend for the Enterprise Task Manager, following enterprise Java development best practices.

**Key Objectives:**

1.  **Implement Secure RESTful API:** Expose endpoints for authentication, project CRUD, and task CRUD operations, secured using JWT and integrated with Supabase Auth.
2.  **Ensure Data Integrity and Persistence:** Manage data using Spring Data JPA and PostgreSQL (hosted on Supabase), ensuring data validation and relationships.
3.  **Design a Scalable Architecture:** Implement a layered architecture (Controller-Service-Repository) ensuring separation of concerns and maintainability.
4.  **Robustness and Error Handling:** Implement comprehensive exception handling, input validation, and rate limiting.
5.  **Optimise for Performance:** Implement caching and efficient database interactions.
6.  **Development and Deployment:** Use Spring Boot profiles for environment-specific configurations and provide API documentation using OpenAPI (Swagger). Deploy the repo onto Render and the database onto Supabase for both a DEV and PROD environments.

## Enterprise Considerations

This backend application is built with several enterprise-level qualities in mind:

* **Performance:**
    * **Caching:** Implements Spring Cache (`@EnableCaching`, `CacheConfig.java`) for frequently accessed data.
    * **Efficient Database Operations:** Uses Spring Data JPA with HikariCP connection pool and Hibernate properties (`application.properties`).
    * **Stateless Authentication:** Uses JWT for a stateless approach.

* **Scalability:**
    * **Layered Architecture:** I've used a typical Controller-Service-Repository modular approach to separate concerns.
    * **Stateless Design:** JWT allows easy horizontal scaling.
    * **Spring Boot:** Robust and enterprise-standard framework for Java applications.

* **Robustness:**
    * **Global Exception Handling:** Centralised `GlobalExceptionHandler` for consistent JSON error responses.
    * **Input Validation:** Validation on DTOs enforced at the controller level.
    * **Rate Limiting:** `RateLimitingFilter` protects against brute-force attacks.

* **Security:**
    * **JWT Authentication and Supabase Integration:** Secured using Spring Security (`WebSecurityConfig.java`). Authentication logic (signup/signin) handled via `SupabaseAuthService` interacting with Supabase. `JwtAuthenticationFilter` validates JWTs.
    * **Password Security:** Password hashing and management are implemented externally by Supabase Authentication.
    * **HTTPS:** Using Render for SSL handling.
    * **CORS Configuration:** Configured (`CorsConfig.java`, properties) for allowed origins.
    * **Environment Variables:** Sensitive information managed via environment variables.
    * **Input Validation:** Prevents injection attacks such as SQL injection or XSS.
    * **API Documentation:** OpenAPI endpoints can be secured.

* **Deployment:**
    * **Environment Profiles:** Uses Spring Profiles (`dev`, `prod`) activated via `SPRING_PROFILES_ACTIVE` environment variable.
    * **Database:** Using Supabase (PostgreSQL) for managed DB hosting.
    * **Hosting Platform:** Deployed on Render across two environments (dev and prod).

## Installation & Usage Instructions

### Prerequisites

* Java Development Kit (JDK) 17+
* Apache Maven (v3.6+)
* Git

### Setup Steps

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/Ross-T/enterprise-task-manager-backend.git
    cd enterprise-task-manager-backend
    ```

2.  **Configure Environment Variables (if running locally):**
    Create a .env file in your root project directory, and make sure this file isn't committed (add it to .gitignore)
    ```dotenv
    # Choose to run in dev or prod:
    SPRING_PROFILES_ACTIVE=dev

    # Database Configuration (replace values with real database credentials)
    DB_URL=jdbc:postgresql://<your_supabase_host>:<port>/postgres?sslmode=require
    DB_USERNAME=<supabase_db_user>
    DB_PASSWORD=<supabase_db_password>

    # JWT Configuration (replace secret with real JWT secret)
    JWT_SECRET=<jwt_secret_key>
    JWT_EXPIRATION=86400000

    # Supabase Configuration (replace placeholders with real values)
    SUPABASE_URL=https://<supabase_project_ref>.supabase.co
    SUPABASE_KEY=<supabase_key>
    ```

3.  **Build the application:**
    ```bash
    mvn clean install -DskipTests
    ```

### Running the Application

1.  **Run using Maven (requires environment variables to have been set):**
    Ensure `SPRING_PROFILES_ACTIVE=dev` (or `prod`) is set, then:
    ```bash
    mvn spring-boot:run
    ```

2.  **Access the API:**
    * URL: `http://localhost:8080`
    * Swagger UI: `http://localhost:8080/swagger-ui.html`
    * OpenAPI Spec: `http://localhost:8080/v3/api-docs`

## Feature Overview

| Feature               | Purpose                                                      | Code Locations                                                                                                                               | Endpoints                      |
| :-------------------- | :----------------------------------------------------------- | :-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | :-------------------------------------- |
| User Authentication | Handles signup/signin via Supabase, JWT handling.            | `AuthController.java`, `SupabaseAuthServiceImpl.java`, `User.java`, `LoginRequest.java`, `SignupRequest.java`, `WebSecurityConfig.java`, `JwtAuthenticationFilter.java` | `/api/auth/signin`, `/api/auth/signup`  |
| Project Management    | CRUD operations for projects.                                | `ProjectController.java`, `ProjectServiceImpl.java`, `ProjectRepository.java`, `Project.java`, `ProjectCreateRequest.java`, `ProjectUpdateRequest.java` | `/api/projects`, `/api/projects/{id}` |
| Task Management       | CRUD operations for tasks.                                   | `TaskController.java`, `TaskServiceImpl.java`, `TaskRepository.java`, `Task.java`, `TaskCreateRequest.java`, `TaskUpdateRequest.java` | `/api/tasks/project/{projectId}`, `/api/tasks`, `/api/tasks/{id}` |
| Security Config     | Manages auth, JWT validation, CORS, rate limiting.         | `WebSecurityConfig.java`, `JwtAuthenticationFilter.java`, `CorsConfig.java`, `RateLimitingFilter.java`                                             | Secures `/api/**`       |
| Error Handling        | Global exception handling.                              | `GlobalExceptionHandler.java`                                                                                                                                           | Returns consistent JSON errors.         |
| Configuration         | Manages settings, profiles, DB, cache, Supabase integration. | `application*.properties`, `CacheConfig.java`, `SupabaseConfig.java`, `SupabaseProperties.java`, `OpenApiConfig.java` | Defines properties.           |
| API Documentation     | Generates interactive API documentation.                     | `OpenApiConfig.java`, Controller annotations (`@Operation`, etc.)                                                                                                     | Accessible via `/swagger-ui.html`       |

## API Documentation

This application uses SpringDoc OpenAPI (Swagger) to automatically generate comprehensive API documentation. This allows direct testing of endpoints from the browser, and ensures that documentation always automatically reflects the current state of API endpoints.

### How to Access (app must be running)
- **Swagger UI**: [`http://localhost:8080/swagger-ui.html`](http://localhost:8080/swagger-ui.html) - Interactive documentation
- **OpenAPI Spec**: [`http://localhost:8080/v3/api-docs`](http://localhost:8080/v3/api-docs) - Raw JSON specification

## Known Issues & Future Enhancements

* **Known Issues:**
    * Incomplete/failing test coverage.
    * Incomplete implementation of role management.
    * Caching/Rate limiting not fully optimised.
* **Potential Future Enhancements:**
    * Implement comprehensive testing.
    * Add robust logging and monitoring.
    * Enhance caching/rate limiting.

## References

* Spring Boot, Spring Security, Spring Data JPA, Maven, PostgreSQL, Supabase, JWT, OpenAPI, Bucket4j, Render
* Generative AI tools were consulted and all final code/decisions were personally reviewed, understood, and adapted.