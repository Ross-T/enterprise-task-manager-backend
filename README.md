# Enterprise Task Manager Backend

## Solution Overview
Enterprise Task Manager Backend is a comprehensive Java Spring Boot application that powers the task management system for enterprise teams. It encompasses:

- **Core Business Logic**: Implementing task management workflows, project organization, and business rules
- **Data Management**: Entity relationships, persistence, and data integrity enforcement
- **Security Layer**: Authentication, authorization, and data protection mechanisms
- **RESTful APIs**: External interfaces for frontend communication
- **Service Integration**: Connectivity with Supabase for authentication services
- **Performance Optimization**: Efficient data processing and response handling

This repository contains the complete backend application - the frontend React implementation is available in a [separate repository](https://github.com/Ross-T/enterprise-task-manager-frontend).

## Technology Stack
- Java 17
- Spring Boot 3.4.3
- Spring Security with JWT Authentication
- PostgreSQL/Supabase Database
- JUnit & Cucumber for Testing
- Maven Build System

## Project Aim & Objectives
**Main Goal**: Develop a scalable, secure, and high-performance task management system that meets enterprise-level requirements.

**Key Objectives**:
1. Implement secure user authentication and role-based access control
2. Design a scalable domain model for task and project management
3. Ensure data integrity with comprehensive validation and error handling
4. Support integration with the React frontend application
5. Implement enterprise-level security considerations
6. Optimize performance for enterprise-scale usage

## Features
- User authentication via Supabase integration
- Project management with ownership and member access control
- Task creation, assignment, and status tracking
- Priority-based task organization
- Comprehensive validation and error handling
- Reporting capabilities for task and project status
- RESTful API endpoints for frontend integration

## API Reference

| Endpoint | Method | Description |
|----------|--------|-------------|
| /api/tasks | GET | Retrieve all tasks |
| /api/tasks/{id} | GET | Retrieve specific task |
| /api/tasks | POST | Create new task |
| /api/tasks/{id} | PUT | Update existing task |
| /api/tasks/{id} | DELETE | Delete specific task |
| /api/projects | GET | Retrieve all projects |
| /api/projects/{id} | GET | Retrieve specific project |
| /api/projects | POST | Create new project |
| /api/projects/{id} | PUT | Update existing project |
| /api/projects/{id} | DELETE | Delete specific project |
| /api/tasks/project/{projectId} | GET | Retrieve tasks by project |
| /api/tasks/status/{status} | GET | Retrieve tasks by status |

*Full API documentation with request/response examples available in [API.md](API.md)*

## Installation & Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL database or Supabase account

### Local Development Setup
1. Clone this repository
   ```bash
   git clone https://github.com/Ross-T/enterprise-task-manager.git
   cd enterprise-task-manager
   ```
   
2. Create a `.env` file in the project root with required environment variables
   ```properties
   # Database
   DB_URL=jdbc:postgresql://localhost:5432/taskmanager
   DB_USERNAME=postgres
   DB_PASSWORD=your_password
   
   # JWT
   JWT_SECRET=your_long_secure_random_string
   JWT_EXPIRATION=86400000
   
   # Supabase
   SUPABASE_URL=your_supabase_url
   SUPABASE_KEY=your_supabase_key
   ```

3. Build the application
   ```bash
   mvn clean install
   ```

4. Run the application
   ```bash
   mvn spring-boot:run
   ```

5. API will be available at http://localhost:8080

## Testing Approach

This project implements multiple testing strategies:

- **Unit Tests**: Testing individual components in isolation
- **Integration Tests**: Testing interactions between components
- **Behavior-Driven Development**: Using Cucumber for human-readable test specifications that match business requirements

Run tests with:
```bash
# Run all tests
mvn test

# Run only unit tests
mvn test -Dtest=*Test

# Run only Cucumber tests
mvn test -Dtest=CucumberIntegrationTest
```

## Deployment

The application is designed for containerized deployment:

```bash
# Build Docker image
docker build -t enterprise-task-manager-backend .

# Run container
docker run -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://db:5432/taskmanager \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=secure_password \
  -e JWT_SECRET=your_secret \
  -e SUPABASE_URL=your_url \
  -e SUPABASE_KEY=your_key \
  enterprise-task-manager-backend
```

## Environment Configuration

This application uses environment variables for all sensitive configuration following enterprise security best practices. **No secrets are stored in the codebase.**

### Required Environment Variables:
- `DB_URL`: JDBC connection URL for PostgreSQL
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password 
- `JWT_SECRET`: Base64-encoded secret key for JWT signing (min 512 bits)
- `JWT_EXPIRATION`: JWT token expiration in milliseconds (default: 86400000 - 24hrs)
- `SUPABASE_URL`: URL to your Supabase instance
- `SUPABASE_KEY`: API key for Supabase

## Architecture

The backend follows a layered architecture pattern:

1. **Controller Layer**: REST endpoints that handle HTTP requests/responses
2. **Service Layer**: Business logic implementation
3. **Repository Layer**: Data access and persistence
4. **Domain Model**: Entity classes representing the business domain
5. **Security**: Authentication and authorization controls

![Architecture Diagram](docs/architecture.png)
*Architecture diagram shows the flow from controllers through services to repositories and the database.*

## Performance Considerations

The backend implements several performance optimizations:
- Database query optimization through indexing
- Connection pooling for database efficiency
- Caching for frequently accessed data
- Asynchronous processing for long-running operations

## Known Issues & Future Enhancements
(To be filled as the project progresses)

## References
(To be filled as the project progresses)