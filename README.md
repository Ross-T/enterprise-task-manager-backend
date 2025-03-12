# Enterprise Task Manager

## Solution Overview
Enterprise Task Manager is a full-stack application that facilitates task management for enterprise teams. It provides features for task creation, assignment, tracking, and reporting with enterprise-grade security, performance, and scalability.

## Project Aim & Objectives
**Main Goal**: Develop a scalable, secure, and robust task management system that meets enterprise-level requirements.

**Key Objectives**:
1. Implement secure user authentication and role-based access control
2. Create a responsive and intuitive user interface for task management
3. Design a scalable RESTful API layer for communication between frontend and backend
4. Ensure data integrity and system reliability with proper error handling and validation
5. Deploy the application with enterprise-level security considerations

## Enterprise Considerations
(To be filled as the project progresses)

## Installation & Usage Instructions
(To be filled as the project progresses)

## Feature Overview
(To be filled as the project progresses)

## Known Issues & Future Enhancements
(To be filled as the project progresses)

## References
(To be filled as the project progresses)

## Environment Configuration

This application uses environment variables for all sensitive configuration following enterprise security best practices. **No secrets are stored in the codebase.**

### Required Environment Variables:
- `DB_URL`: JDBC connection URL for PostgreSQL
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password 
- `JWT_SECRET`: Base64-encoded secret key for JWT signing (min 512 bits)
- `JWT_EXPIRATION`: JWT token expiration in milliseconds (default: 86400000 - 24hrs)

### Local Development Setup:
1. Create a `.env` file in the backend directory
2. Add the required environment variables (see template below)
3. This file is in .gitignore and should never be committed
