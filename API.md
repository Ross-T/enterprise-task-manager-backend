# Enterprise Task Manager API Documentation

## Introduction

This document provides detailed information about the Enterprise Task Manager REST API. This API allows you to manage projects and tasks within the task management system.

## Base URL

All API endpoints are relative to the base URL:

```
http://localhost:8080/api
```

For production environments, replace with your deployed API URL.

## Authentication

All API requests require authentication using a JWT token obtained from the authentication endpoints.

**Headers:**
```
Authorization: Bearer <your_jwt_token>
```

## Error Handling

The API uses standard HTTP status codes to indicate the success or failure of requests:

| Status Code | Meaning |
|-------------|---------|
| 200 | Success - The request was successful |
| 201 | Created - A new resource was successfully created |
| 204 | No Content - The request was successful but returns no content |
| 400 | Bad Request - The request was malformed or invalid |
| 401 | Unauthorized - Authentication is required or failed |
| 403 | Forbidden - The authenticated user doesn't have required permissions |
| 404 | Not Found - The requested resource was not found |
| 500 | Internal Server Error - An unexpected error occurred on the server |

Error responses include a JSON body with details:
```json
{
  "timestamp": "2025-03-19T09:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Task title is required",
  "path": "/api/tasks"
}
```

## Authentication Endpoints

### Sign Up

**POST** `/api/auth/signup`

Creates a new user account.

**Request Body:**
```json
{
  "username": "johndoe",
  "email": "john.doe@example.com",
  "password": "securePassword123"
}
```

**Response:**
```json
{
  "message": "User registered successfully!",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Sign In

**POST** `/api/auth/signin`

Authenticates a user and returns a JWT token.

**Request Body:**
```json
{
  "email": "john.doe@example.com",
  "password": "securePassword123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "johndoe",
  "email": "john.doe@example.com"
}
```

## Task Endpoints

### Get All Tasks

**GET** `/api/tasks`

Retrieves a list of all tasks in the system.

**Query Parameters:**
- `page` (Integer, optional): Page number for pagination (default: 0)
- `size` (Integer, optional): Number of items per page (default: 20)
- `sort` (String, optional): Field to sort by (default: createdAt,desc)

**Response:**
```json
[
  {
    "id": 1,
    "title": "Implement login page",
    "description": "Create login page with email and password fields",
    "status": "IN_PROGRESS",
    "priority": "HIGH",
    "dueDate": "2025-04-01T00:00:00",
    "createdAt": "2025-03-15T10:30:00",
    "projectId": 1,
    "projectName": "Website Redesign"
  },
  {
    "id": 2,
    "title": "Set up CI/CD pipeline",
    "description": "Configure GitHub Actions for automated testing and deployment",
    "status": "TODO",
    "priority": "MEDIUM",
    "dueDate": "2025-04-10T00:00:00",
    "createdAt": "2025-03-16T08:45:00",
    "projectId": 2,
    "projectName": "DevOps Improvements"
  }
]
```

### Get Task by ID

**GET** `/api/tasks/{id}`

Retrieves details of a specific task.

**Path Parameters:**
- `id` (Long): The ID of the task to retrieve

**Response:**
```json
{
  "id": 1,
  "title": "Implement login page",
  "description": "Create login page with email and password fields",
  "status": "IN_PROGRESS",
  "priority": "HIGH",
  "dueDate": "2025-04-01T00:00:00",
  "createdAt": "2025-03-15T10:30:00",
  "projectId": 1,
  "projectName": "Website Redesign"
}
```

### Create Task

**POST** `/api/tasks`

Creates a new task.

**Request Body:**
```json
{
  "title": "Implement user profile page",
  "description": "Create user profile page with avatar and settings",
  "priority": "MEDIUM",
  "dueDate": "2025-04-15T00:00:00",
  "projectId": 1
}
```

**Response:**
```json
{
  "id": 3,
  "title": "Implement user profile page",
  "description": "Create user profile page with avatar and settings",
  "status": "TODO",
  "priority": "MEDIUM",
  "dueDate": "2025-04-15T00:00:00",
  "createdAt": "2025-03-19T14:25:30",
  "projectId": 1,
  "projectName": "Website Redesign"
}
```

### Update Task

**PUT** `/api/tasks/{id}`

Updates an existing task.

**Path Parameters:**
- `id` (Long): The ID of the task to update

**Request Body:**
```json
{
  "title": "Implement user profile page with settings",
  "description": "Create user profile page with avatar, settings, and theme preferences",
  "status": "IN_PROGRESS",
  "priority": "HIGH",
  "dueDate": "2025-04-20T00:00:00",
  "projectId": 1
}
```

**Response:**
```json
{
  "id": 3,
  "title": "Implement user profile page with settings",
  "description": "Create user profile page with avatar, settings, and theme preferences",
  "status": "IN_PROGRESS",
  "priority": "HIGH",
  "dueDate": "2025-04-20T00:00:00",
  "createdAt": "2025-03-19T14:25:30",
  "projectId": 1,
  "projectName": "Website Redesign"
}
```

### Delete Task

**DELETE** `/api/tasks/{id}`

Deletes a specific task.

**Path Parameters:**
- `id` (Long): The ID of the task to delete

**Response:**
- Status: 204 No Content

### Get Tasks by Project

**GET** `/api/tasks/project/{projectId}`

Retrieves all tasks associated with a specific project.

**Path Parameters:**
- `projectId` (Long): The ID of the project

**Query Parameters:**
- `page` (Integer, optional): Page number for pagination (default: 0)
- `size` (Integer, optional): Number of items per page (default: 20)

**Response:**
```json
[
  {
    "id": 1,
    "title": "Implement login page",
    "description": "Create login page with email and password fields",
    "status": "IN_PROGRESS",
    "priority": "HIGH",
    "dueDate": "2025-04-01T00:00:00",
    "createdAt": "2025-03-15T10:30:00",
    "projectId": 1,
    "projectName": "Website Redesign"
  },
  {
    "id": 3,
    "title": "Implement user profile page with settings",
    "description": "Create user profile page with avatar, settings, and theme preferences",
    "status": "IN_PROGRESS",
    "priority": "HIGH",
    "dueDate": "2025-04-20T00:00:00",
    "createdAt": "2025-03-19T14:25:30",
    "projectId": 1,
    "projectName": "Website Redesign"
  }
]
```

### Get Tasks by Status

**GET** `/api/tasks/status/{status}`

Retrieves all tasks with a specific status.

**Path Parameters:**
- `status` (String): The task status (TODO, IN_PROGRESS, REVIEW, DONE)

**Query Parameters:**
- `page` (Integer, optional): Page number for pagination (default: 0)
- `size` (Integer, optional): Number of items per page (default: 20)

**Response:**
```json
[
  {
    "id": 1,
    "title": "Implement login page",
    "description": "Create login page with email and password fields",
    "status": "IN_PROGRESS",
    "priority": "HIGH",
    "dueDate": "2025-04-01T00:00:00",
    "createdAt": "2025-03-15T10:30:00",
    "projectId": 1,
    "projectName": "Website Redesign"
  },
  {
    "id": 3,
    "title": "Implement user profile page with settings",
    "description": "Create user profile page with avatar, settings, and theme preferences",
    "status": "IN_PROGRESS",
    "priority": "HIGH",
    "dueDate": "2025-04-20T00:00:00",
    "createdAt": "2025-03-19T14:25:30",
    "projectId": 1,
    "projectName": "Website Redesign"
  }
]
```

## Project Endpoints

### Get All Projects

**GET** `/api/projects`

Retrieves a list of all projects.

**Query Parameters:**
- `page` (Integer, optional): Page number for pagination (default: 0)
- `size` (Integer, optional): Number of items per page (default: 20)
- `sort` (String, optional): Field to sort by (default: createdAt,desc)

**Response:**
```json
[
  {
    "id": 1,
    "name": "Website Redesign",
    "description": "Complete overhaul of the company website",
    "createdAt": "2025-03-10T09:00:00"
  },
  {
    "id": 2,
    "name": "DevOps Improvements",
    "description": "Enhance CI/CD pipeline and monitoring",
    "createdAt": "2025-03-12T11:20:00"
  }
]
```

### Get Project by ID

**GET** `/api/projects/{id}`

Retrieves details of a specific project.

**Path Parameters:**
- `id` (Long): The ID of the project to retrieve

**Response:**
```json
{
  "id": 1,
  "name": "Website Redesign",
  "description": "Complete overhaul of the company website",
  "createdAt": "2025-03-10T09:00:00"
}
```

### Create Project

**POST** `/api/projects`

Creates a new project.

**Request Body:**
```json
{
  "name": "Mobile App Development",
  "description": "Create iOS and Android versions of our main application"
}
```

**Response:**
```json
{
  "id": 3,
  "name": "Mobile App Development",
  "description": "Create iOS and Android versions of our main application",
  "createdAt": "2025-03-19T15:30:00"
}
```

### Update Project

**PUT** `/api/projects/{id}`

Updates an existing project.

**Path Parameters:**
- `id` (Long): The ID of the project to update

**Request Body:**
```json
{
  "name": "Mobile App Development - Phase 1",
  "description": "Create iOS and Android versions of our main application with core functionality"
}
```

**Response:**
```json
{
  "id": 3,
  "name": "Mobile App Development - Phase 1",
  "description": "Create iOS and Android versions of our main application with core functionality",
  "createdAt": "2025-03-19T15:30:00"
}
```

### Delete Project

**DELETE** `/api/projects/{id}`

Deletes a specific project.

**Path Parameters:**
- `id` (Long): The ID of the project to delete

**Response:**
- Status: 204 No Content

## User Endpoints

### Get Current User

**GET** `/api/users/me`

Retrieves the profile of the currently authenticated user.

**Response:**
```json
{
  "id": 1,
  "username": "johndoe",
  "email": "john.doe@example.com",
  "createdAt": "2025-03-01T12:00:00",
  "roles": ["ROLE_USER"]
}
```

## Data Structures

### Task

| Field | Type | Description |
|-------|------|-------------|
| id | Long | Unique identifier |
| title | String | Task title |
| description | String | Detailed description |
| status | Enum | Current status (TODO, IN_PROGRESS, REVIEW, DONE) |
| priority | Enum | Priority level (LOW, MEDIUM, HIGH, CRITICAL) |
| dueDate | LocalDateTime | Due date and time |
| createdAt | LocalDateTime | Creation timestamp |
| projectId | Long | ID of associated project (nullable) |
| projectName | String | Name of associated project (nullable) |

### Project

| Field | Type | Description |
|-------|------|-------------|
| id | Long | Unique identifier |
| name | String | Project name |
| description | String | Project description |
| createdAt | LocalDateTime | Creation timestamp |

### User

| Field | Type | Description |
|-------|------|-------------|
| id | Long | Unique identifier |
| username | String | User's username |
| email | String | User's email address |
| roles | List<String> | User's assigned roles |
| createdAt | LocalDateTime | Account creation timestamp |