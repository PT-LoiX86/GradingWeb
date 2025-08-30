# Grading Backend (GradBE)

# Grading Backend (GradBE)

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![Version](https://img.shields.io/badge/version-0.0.1--SNAPSHOT-blue)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14+-blue)
![Redis](https://img.shields.io/badge/Redis-6+-red)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14+-blue)
![Redis](https://img.shields.io/badge/Redis-6+-red)

A comprehensive backend API for university student grading and academic management system. This RESTful API provides robust services for managing student profiles, grades, academic records, and includes a forum system for academic discussions.
A comprehensive backend API for university student grading and academic management system. This RESTful API provides robust services for managing student profiles, grades, academic records, and includes a forum system for academic discussions.

## Table of Contents

-   [Features](#features)
-   [Technology Stack](#technology-stack)
-   [Prerequisites](#prerequisites)
-   [Installation](#installation)
-   [Configuration](#configuration)
-   [Usage](#usage)
-   [Project Structure](#project-structure)
-   [API Documentation](#api-documentation)
-   [Testing](#testing)
-   [Security](#security)
-   [Performance](#performance)
-   [Security](#security)
-   [Performance](#performance)
-   [Deployment](#deployment)
-   [Contributing](#contributing)

## Features

### Core Features

### Core Features

-   **Student Management**: Complete CRUD operations for student profiles and academic records
-   **Grade Management**: Track and manage student grades across subjects and academic periods
-   **Academic Records**: Comprehensive academic history management with transcript generation
-   **User Authentication & Authorization**: JWT-based authentication with OAuth2 (Google) integration
-   **Forum System**: Academic discussion platform with posts, comments, and channels
-   **University & Major Management**: Administrative tools for academic program management
-   **Geographic Data Management**: Province and school data handling
-   **File Management**: Secure file upload and storage with AWS S3 integration
-   **Email Notifications**: Automated email services for important updates
-   **Real-time Data**: Redis-based caching for optimal performance
-   **Rate Limiting**: API protection with configurable rate limiting
-   **Audit Logging**: Comprehensive audit trail for all operations
-   **Student Management**: Complete CRUD operations for student profiles and academic records
-   **Grade Management**: Track and manage student grades across subjects and academic periods
-   **Academic Records**: Comprehensive academic history management with transcript generation
-   **User Authentication & Authorization**: JWT-based authentication with OAuth2 (Google) integration
-   **Forum System**: Academic discussion platform with posts, comments, and channels
-   **University & Major Management**: Administrative tools for academic program management
-   **Geographic Data Management**: Province and school data handling
-   **File Management**: Secure file upload and storage with AWS S3 integration
-   **Email Notifications**: Automated email services for important updates
-   **Real-time Data**: Redis-based caching for optimal performance
-   **Rate Limiting**: API protection with configurable rate limiting
-   **Audit Logging**: Comprehensive audit trail for all operations

### API Features

### API Features

-   **RESTful Design**: Following REST principles with clear HTTP semantics
-   **OpenAPI Documentation**: Auto-generated API documentation with Swagger UI
-   **Data Validation**: Comprehensive input validation and error handling
-   **Pagination & Filtering**: Efficient data retrieval with pagination support
-   **CORS Support**: Configurable cross-origin resource sharing
-   **Health Checks**: Built-in health monitoring endpoints
-   **RESTful Design**: Following REST principles with clear HTTP semantics
-   **OpenAPI Documentation**: Auto-generated API documentation with Swagger UI
-   **Data Validation**: Comprehensive input validation and error handling
-   **Pagination & Filtering**: Efficient data retrieval with pagination support
-   **CORS Support**: Configurable cross-origin resource sharing
-   **Health Checks**: Built-in health monitoring endpoints

## Technology Stack

### Core Technologies

### Core Technologies

-   **Java 21** - Latest LTS Java version with modern language features
-   **Spring Boot 3.3.5** - Enterprise-grade application framework
-   **Spring Security** - Comprehensive security framework
-   **Spring Data JPA** - Data persistence with JPA/Hibernate
-   **PostgreSQL** - Primary relational database
-   **Redis** - In-memory caching and session storage

### Authentication & Security

-   **JWT (JSON Web Tokens)** - Stateless authentication
-   **OAuth2** - Google authentication integration
-   **Spring Security** - Authorization and access control
-   **BCrypt** - Password hashing

### File Management & Communication

-   **AWS S3** - Cloud file storage
-   **Apache Tika** - File type detection
-   **Spring Mail** - Email service integration
-   **Thymeleaf** - HTML email template engine

### Development & Documentation

-   **SpringDoc OpenAPI** - API documentation generation
-   **MapStruct** - Type-safe object mapping
-   **Java 21** - Latest LTS Java version with modern language features
-   **Spring Boot 3.3.5** - Enterprise-grade application framework
-   **Spring Security** - Comprehensive security framework
-   **Spring Data JPA** - Data persistence with JPA/Hibernate
-   **PostgreSQL** - Primary relational database
-   **Redis** - In-memory caching and session storage

### Authentication & Security

-   **JWT (JSON Web Tokens)** - Stateless authentication
-   **OAuth2** - Google authentication integration
-   **Spring Security** - Authorization and access control
-   **BCrypt** - Password hashing

### File Management & Communication

-   **AWS S3** - Cloud file storage
-   **Apache Tika** - File type detection
-   **Spring Mail** - Email service integration
-   **Thymeleaf** - HTML email template engine

### Development & Documentation

-   **SpringDoc OpenAPI** - API documentation generation
-   **MapStruct** - Type-safe object mapping
-   **Lombok** - Boilerplate code reduction
-   **Maven** - Dependency management and build tool
-   **Maven** - Dependency management and build tool

## Prerequisites

Before running this backend application, ensure you have the following installed:
Before running this backend application, ensure you have the following installed:

-   **Java 21** or higher (JDK)
-   **Java 21** or higher (JDK)
-   **Maven 3.6+** (optional, wrapper included)
-   **PostgreSQL 14+** (or Docker)
-   **Redis 6+** (or Docker)
-   **Git** for version control
-   **Docker & Docker Compose** (recommended for development)
-   **PostgreSQL 14+** (or Docker)
-   **Redis 6+** (or Docker)
-   **Git** for version control
-   **Docker & Docker Compose** (recommended for development)

## Installation

### 1. Clone the Repository

```bash
git clone <repository-url>
cd GradBE
git clone <repository-url>
cd GradBE
```

### 2. Environment Configuration

### 2. Environment Configuration

Set up the required environment variables:
Set up the required environment variables:

```bash
# JWT Configuration
export JWT_SECRET=your-256-bit-secret-key

# Database Configuration (if not using Docker)
export POSTGRES_URL=jdbc:postgresql://localhost:5432/grading_db
export POSTGRES_USERNAME=root
export POSTGRES_PASSWORD=root

# Google OAuth2 (optional)
export GOOGLE_CLIENT_ID=your-google-client-id
export GOOGLE_CLIENT_SECRET=your-google-client-secret
export JWT_SECRET=your-256-bit-secret-key

# Database Configuration (if not using Docker)
export POSTGRES_URL=jdbc:postgresql://localhost:5432/grading_db
export POSTGRES_USERNAME=root
export POSTGRES_PASSWORD=root

# Google OAuth2 (optional)
export GOOGLE_CLIENT_ID=your-google-client-id
export GOOGLE_CLIENT_SECRET=your-google-client-secret

# Email Configuration
export MAIL_USERNAME=your-email@gmail.com
export MAIL_PASSWORD=your-app-password
export MAIL_USERNAME=your-email@gmail.com
export MAIL_PASSWORD=your-app-password

# AWS S3 Configuration
export AWS_ACCESS_KEY_ID=your-access-key
export AWS_SECRET_ACCESS_KEY=your-secret-key
export AWS_S3_BUCKET_NAME=your-bucket-name
export AWS_S3_ENDPOINT=your-s3-endpoint
# AWS S3 Configuration
export AWS_ACCESS_KEY_ID=your-access-key
export AWS_SECRET_ACCESS_KEY=your-secret-key
export AWS_S3_BUCKET_NAME=your-bucket-name
export AWS_S3_ENDPOINT=your-s3-endpoint
```

### 3. Database Setup (Docker - Recommended)

### 3. Database Setup (Docker - Recommended)

```bash
# Start PostgreSQL and Redis with Docker Compose
docker-compose up -d postgres redis

# Or start all services including the application
docker-compose up -d
# Start PostgreSQL and Redis with Docker Compose
docker-compose up -d postgres redis

# Or start all services including the application
docker-compose up -d
```

### 4. Manual Database Setup (Alternative)

If you prefer to install PostgreSQL manually:

### 4. Manual Database Setup (Alternative)

If you prefer to install PostgreSQL manually:

```bash
# Install PostgreSQL (Ubuntu/Debian)
sudo apt update
sudo apt install postgresql postgresql-contrib
# Install PostgreSQL (Ubuntu/Debian)
sudo apt update
sudo apt install postgresql postgresql-contrib

# Create database
sudo -u postgres createdb grading_db
# Create database
sudo -u postgres createdb grading_db

# Install Redis
sudo apt install redis-server
# Install Redis
sudo apt install redis-server
```

### 5. Build and Run

### 5. Build and Run

```bash
# Build the application
./mvnw clean install
# Build the application
./mvnw clean install

# Run the application
./mvnw spring-boot:run
# Run the application
./mvnw spring-boot:run

# Or run with specific profile
./mvnw spring-boot:run -Dspring.profiles.active=dev
# Or run with specific profile
./mvnw spring-boot:run -Dspring.profiles.active=dev
```

## Configuration

### Application Configuration

### Application Configuration

The main configuration file is located at `src/main/resources/application.yml`. Key configuration sections:

#### Database Configuration

```yaml
spring:
    datasource:
        url: jdbc:postgresql://localhost:5432/grading_db
        username: root
        password: root
        driver-class-name: org.postgresql.Driver
    jpa:
        hibernate:
            ddl-auto: update
        show-sql: true
```

#### Redis Configuration

```yaml
spring:
    data:
        redis:
            host: localhost
            port: 6379
            connect-timeout: 2s
            timeout: 1s
```

#### Security Configuration

```yaml
env:
    jwt:
        secret: ${JWT_SECRET}
        issuer: http://localhost:8080
        refresh-token-validity-days: 7
        max-tokens-per-user: 5
```

#### File Upload Configuration

```yaml
app:
    file-upload:
        max-requests-per-minute: 10
        max-files-per-request: 5
        max-file-size: 10485760 # 10MB
        allowed-extensions: [jpg, jpeg, png, gif, pdf, doc, docx]
```

### Environment-Specific Profiles

Create additional configuration files for different environments:

-   `application-dev.yml` - Development environment
-   `application-prod.yml` - Production environment
-   `application-test.yml` - Testing environment

### Logging Configuration

Logging is configured via `src/main/resources/logback-spring.xml` with:

-   Console logging for development
-   File-based logging for production
-   Configurable log levels per package

## Usage

### Development Mode

```bash
# Start the application
# Start the application
./mvnw spring-boot:run

# Or with specific profile
./mvnw spring-boot:run -Dspring.profiles.active=dev

# Or run with debug mode
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

# Or with specific profile
./mvnw spring-boot:run -Dspring.profiles.active=dev

# Or run with debug mode
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
```

The backend API will be available at `http://localhost:8080`
The backend API will be available at `http://localhost:8080`

### Production Mode

```bash
# Build the JAR file
./mvnw clean package -DskipTests

# Run the JAR
java -jar target/Grading-BE-0.0.1-SNAPSHOT.jar

# Or with production profile
java -jar target/Grading-BE-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### Docker Deployment

```bash
# Build and run with Docker Compose
# Build the JAR file
./mvnw clean package -DskipTests

# Run the JAR
java -jar target/Grading-BE-0.0.1-SNAPSHOT.jar

# Or with production profile
java -jar target/Grading-BE-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### Docker Deployment

```bash
# Build and run with Docker Compose
docker-compose up -d

# View logs
docker-compose logs -f grading-be
# View logs
docker-compose logs -f grading-be

# Stop services
docker-compose down
# Stop services
docker-compose down
```

### API Access Points

### API Access Points

Once the backend is running, you can access:

-   **API Base URL**: `http://localhost:8080/api`
-   **Swagger UI**: `http://localhost:8080/swagger-ui.html`
-   **API Documentation**: `http://localhost:8080/v3/api-docs`
-   **Health Check**: `http://localhost:8080/actuator/health`
-   **Application Info**: `http://localhost:8080/actuator/info`

### Database Tools

If using Docker Compose with pgAdmin:

If using Docker Compose with pgAdmin:

-   **pgAdmin**: `http://localhost:5050`
    -   Email: `admin@gradingweb.com`
    -   Password: `admin`

### Testing API

You can test the API using:

```bash
# Health check
curl http://localhost:8080/actuator/health

# API documentation
curl http://localhost:8080/v3/api-docs

# Login example
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password"}'
```

### Testing API

You can test the API using:

```bash
# Health check
curl http://localhost:8080/actuator/health

# API documentation
curl http://localhost:8080/v3/api-docs

# Login example
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password"}'
```

## Project Structure

```
GradBE/
GradBE/
├── docker-compose.yml              # Docker services configuration
├── Dockerfile                      # Application Docker configuration
├── README.md                       # This documentation
├── pom.xml                         # Maven dependencies and build configuration
├── mvnw                           # Maven wrapper script (Unix)
├── mvnw.cmd                       # Maven wrapper script (Windows)
└── src/
    ├── main/
    │   ├── java/com/grd/gradingbe/
    │   │   ├── GradingBeApplication.java    # Main application class
    │   │   ├── annotation/                  # Custom annotations
    │   │   ├── aspect/                      # AOP aspects (rate limiting, etc.)
    │   │   ├── audit/                       # Audit configuration
    │   │   ├── configuration/               # Configuration classes
    │   │   │   ├── SecurityConfig.java      # Security configuration
    │   │   │   ├── RedisConfig.java         # Redis configuration
    │   │   │   ├── CorsConfig.java          # CORS configuration
    │   │   │   └── ...
    │   │   ├── controller/                  # REST API controllers
    │   │   │   ├── AuthController.java      # Authentication endpoints
    │   │   │   ├── StudentController.java   # Student management
    │   │   │   ├── GradeController.java     # Grade management
    │   │   │   └── ...
    │   │   ├── dto/                         # Data Transfer Objects
    │   │   │   ├── request/                 # Request DTOs
    │   │   │   ├── response/                # Response DTOs
    │   │   │   ├── entity/                  # Entity DTOs
    │   │   │   └── enums/                   # Enum definitions
    │   │   ├── exception/                   # Exception handling
    │   │   │   ├── GlobalExceptionHandler.java
    │   │   │   └── CustomExceptions.java
    │   │   ├── mapper/                      # MapStruct mappers
    │   │   ├── model/                       # JPA entities
    │   │   │   ├── User.java
    │   │   │   ├── Student.java
    │   │   │   ├── Grade.java
    │   │   │   └── ...
    │   │   ├── repository/                  # Data access layer
    │   │   ├── service/                     # Business logic services
    │   │   │   ├── AuthService.java
    │   │   │   ├── StudentService.java
    │   │   │   ├── GradeService.java
    │   │   │   └── ...
    │   │   └── utilities/                   # Utility classes
    │   └── resources/
    │       ├── application.yml              # Main configuration
    │       ├── logback-spring.xml           # Logging configuration
    │       ├── banner.txt                   # Application banner
    │       └── templates/                   # Email templates
    │           ├── registration-mail-template.html
    │           └── change-password-mail-template.html
    └── test/
        └── java/com/grd/gradingbe/
            ├── GradingBeApplicationTests.java
            ├── integration/                 # Integration tests
            └── service/                     # Service unit tests
├── Dockerfile                      # Application Docker configuration
├── README.md                       # This documentation
├── pom.xml                         # Maven dependencies and build configuration
├── mvnw                           # Maven wrapper script (Unix)
├── mvnw.cmd                       # Maven wrapper script (Windows)
└── src/
    ├── main/
    │   ├── java/com/grd/gradingbe/
    │   │   ├── GradingBeApplication.java    # Main application class
    │   │   ├── annotation/                  # Custom annotations
    │   │   ├── aspect/                      # AOP aspects (rate limiting, etc.)
    │   │   ├── audit/                       # Audit configuration
    │   │   ├── configuration/               # Configuration classes
    │   │   │   ├── SecurityConfig.java      # Security configuration
    │   │   │   ├── RedisConfig.java         # Redis configuration
    │   │   │   ├── CorsConfig.java          # CORS configuration
    │   │   │   └── ...
    │   │   ├── controller/                  # REST API controllers
    │   │   │   ├── AuthController.java      # Authentication endpoints
    │   │   │   ├── StudentController.java   # Student management
    │   │   │   ├── GradeController.java     # Grade management
    │   │   │   └── ...
    │   │   ├── dto/                         # Data Transfer Objects
    │   │   │   ├── request/                 # Request DTOs
    │   │   │   ├── response/                # Response DTOs
    │   │   │   ├── entity/                  # Entity DTOs
    │   │   │   └── enums/                   # Enum definitions
    │   │   ├── exception/                   # Exception handling
    │   │   │   ├── GlobalExceptionHandler.java
    │   │   │   └── CustomExceptions.java
    │   │   ├── mapper/                      # MapStruct mappers
    │   │   ├── model/                       # JPA entities
    │   │   │   ├── User.java
    │   │   │   ├── Student.java
    │   │   │   ├── Grade.java
    │   │   │   └── ...
    │   │   ├── repository/                  # Data access layer
    │   │   ├── service/                     # Business logic services
    │   │   │   ├── AuthService.java
    │   │   │   ├── StudentService.java
    │   │   │   ├── GradeService.java
    │   │   │   └── ...
    │   │   └── utilities/                   # Utility classes
    │   └── resources/
    │       ├── application.yml              # Main configuration
    │       ├── logback-spring.xml           # Logging configuration
    │       ├── banner.txt                   # Application banner
    │       └── templates/                   # Email templates
    │           ├── registration-mail-template.html
    │           └── change-password-mail-template.html
    └── test/
        └── java/com/grd/gradingbe/
            ├── GradingBeApplicationTests.java
            ├── integration/                 # Integration tests
            └── service/                     # Service unit tests
```

## API Documentation

### Interactive Documentation

The API provides comprehensive documentation through Swagger UI:

-   **Swagger UI**: `http://localhost:8080/swagger-ui.html`
-   **OpenAPI Spec**: `http://localhost:8080/v3/api-docs`
-   **OpenAPI JSON**: `http://localhost:8080/v3/api-docs.yaml`

### Core API Endpoints

#### Authentication & Authorization

```
POST   /api/auth/login              # User login with email/password
POST   /api/auth/register           # User registration
POST   /api/auth/refresh            # Refresh JWT token
POST   /api/auth/logout             # User logout
GET    /api/auth/verify-email       # Email verification
POST   /api/auth/forgot-password    # Password reset request
POST   /api/auth/reset-password     # Password reset confirmation
GET    /api/auth/oauth2/google      # Google OAuth2 login
```

#### Student Management

```
GET    /api/students                # Get paginated list of students
POST   /api/students                # Create new student
GET    /api/students/{id}           # Get student by ID
PUT    /api/students/{id}           # Update student information
DELETE /api/students/{id}           # Delete student
GET    /api/students/{id}/grades    # Get student's grades
```

#### Grade Management

```
GET    /api/grades                  # Get paginated grade records
POST   /api/grades                  # Create new grade record
GET    /api/grades/{id}             # Get grade by ID
PUT    /api/grades/{id}             # Update grade
DELETE /api/grades/{id}             # Delete grade
GET    /api/grades/student/{id}     # Get grades for specific student
GET    /api/grades/subject/{id}     # Get grades for specific subject
```

#### Academic Management

```
GET    /api/universities            # Get universities
POST   /api/universities            # Create university
GET    /api/majors                  # Get academic majors
POST   /api/majors                  # Create major
GET    /api/subjects                # Get subjects
POST   /api/subjects                # Create subject
```

#### Forum System

```
GET    /api/forums/posts            # Get forum posts with pagination
POST   /api/forums/posts            # Create new post
GET    /api/forums/posts/{id}       # Get post by ID
PUT    /api/forums/posts/{id}       # Update post
DELETE /api/forums/posts/{id}       # Delete post
POST   /api/forums/posts/{id}/comments  # Add comment to post
GET    /api/forums/channels         # Get forum channels
```

#### File Management

```
POST   /api/files/upload            # Upload file to S3
GET    /api/files/{id}              # Get file metadata
DELETE /api/files/{id}              # Delete file
GET    /api/files/download/{id}     # Download file
```

#### Administrative

```
GET    /api/admin/users             # Get all users (admin only)
PUT    /api/admin/users/{id}/role   # Update user role
GET    /api/admin/statistics        # Get system statistics
GET    /api/provinces               # Get provinces/geographic data
```

### API Response Format

All API responses follow a consistent structure:

**Success Response:**

```json
{
  "success": true,
  "data": { ... },
  "message": "Operation completed successfully",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

**Error Response:**

```json
{
    "success": false,
    "error": {
        "code": "VALIDATION_ERROR",
        "message": "Invalid input data",
        "details": [
            "Email is required",
            "Password must be at least 8 characters"
        ]
    },
    "timestamp": "2024-01-15T10:30:00Z"
}
```

**Paginated Response:**

```json
{
  "success": true,
  "data": {
    "content": [...],
    "page": 0,
    "size": 20,
    "totalElements": 100,
    "totalPages": 5,
    "first": true,
    "last": false
  }
}
```

## Testing

### Running Tests

### Running Tests

```bash
# Run all tests
./mvnw test

# Run tests with coverage report
./mvnw test jacoco:report

# Run tests with coverage report
./mvnw test jacoco:report

# Run specific test class
./mvnw test -Dtest=AuthServiceTest

# Run integration tests
./mvnw test -Dtest="*IntegrationTest"

# Run tests with specific profile
./mvnw test -Dspring.profiles.active=test
```

### Test Categories

#### Unit Tests

-   **Service Layer Tests**: Business logic validation
-   **Repository Tests**: Data access layer testing with `@DataJpaTest`
-   **Controller Tests**: REST API testing with `@WebMvcTest`
-   **Utility Tests**: Helper class and utility method testing

#### Integration Tests

-   **End-to-End API Tests**: Full application context testing
-   **Database Integration**: Real database interaction testing
-   **Security Integration**: Authentication and authorization testing
-   **External Service Integration**: AWS S3, email service testing

### Test Configuration

Test-specific configuration in `src/test/resources/application-test.yml`:

```yaml
spring:
    datasource:
        url: jdbc:h2:mem:testdb
        driver-class-name: org.h2.Driver
    jpa:
        hibernate:
            ddl-auto: create-drop
    data:
        redis:
            host: localhost # Use embedded Redis for tests
```

### Test Coverage

Generate and view test coverage reports:

```bash
# Generate coverage report
./mvnw jacoco:report

# View report (opens in browser)
open target/site/jacoco/index.html
```

Coverage targets:

-   **Overall**: > 80%
-   **Service Layer**: > 90%
-   **Controller Layer**: > 85%

## Security

### Authentication & Authorization

#### JWT Token Security

-   **Algorithm**: HS256 with 256-bit secret key
-   **Expiration**: 24 hours for access tokens, 7 days for refresh tokens
-   **Token Rotation**: Automatic refresh token rotation on use
-   **Max Tokens**: 5 active tokens per user (configurable)

#### OAuth2 Integration

-   **Google OAuth2**: Secure third-party authentication
-   **PKCE Support**: Enhanced security for OAuth2 flows
-   **State Parameter**: CSRF protection for OAuth2 flows

#### Password Security

-   **Hashing**: BCrypt with strength 12
-   **Requirements**: Minimum 8 characters with complexity rules
-   **Reset Tokens**: Secure, time-limited password reset tokens

#### API Security

-   **Rate Limiting**: Configurable per-endpoint rate limits
-   **CORS**: Configurable cross-origin resource sharing
-   **Input Validation**: Comprehensive validation on all endpoints
-   **SQL Injection Protection**: Parameterized queries with JPA

### Security Headers

```yaml
# Configured security headers
Content-Security-Policy: default-src 'self'
X-Frame-Options: DENY
X-Content-Type-Options: nosniff
X-XSS-Protection: 1; mode=block
Strict-Transport-Security: max-age=31536000; includeSubDomains
```

### Security Best Practices

1. **Environment Variables**: Sensitive data stored in environment variables
2. **Secrets Management**: No hardcoded secrets in source code
3. **HTTPS Only**: Production deployment requires HTTPS
4. **Database Security**: Connection pooling with encrypted connections
5. **File Upload Security**: File type validation and virus scanning
6. **Audit Logging**: Complete audit trail for sensitive operations

## Performance

### Caching Strategy

#### Redis Caching

-   **User Sessions**: Distributed session storage
-   **Frequently Accessed Data**: Student profiles, grade summaries
-   **Rate Limiting**: Token bucket implementation
-   **Cache TTL**: Configurable time-to-live for different data types

#### Database Optimization

-   **Connection Pooling**: HikariCP for optimal database connections
-   **JPA Optimizations**: Lazy loading, fetch joins, query optimization
-   **Indexing**: Strategic database indexes for frequent queries
-   **Pagination**: Efficient pagination for large datasets

### Monitoring & Metrics

#### Spring Boot Actuator

-   **Health Checks**: Application, database, and Redis health monitoring
-   **Metrics**: JVM metrics, HTTP request metrics, custom business metrics
-   **Info Endpoint**: Application version and build information

#### Performance Monitoring

```bash
# Health check
curl http://localhost:8080/actuator/health

# Metrics
curl http://localhost:8080/actuator/metrics

# Database connection info
curl http://localhost:8080/actuator/metrics/hikaricp.connections.active
```

### Performance Tuning

#### JVM Configuration

```bash
# Production JVM settings
-Xms512m -Xmx2g
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-XX:+HeapDumpOnOutOfMemoryError
```

#### Database Tuning

-   **Connection Pool**: Optimized pool size based on load
-   **Query Optimization**: Analyzed slow queries with EXPLAIN
-   **Bulk Operations**: Batch processing for large data operations

## Deployment

### Docker Deployment (Recommended)

#### Development Environment

```bash
# Clone the repository
git clone <repository-url>
cd GradBE

# Start services
docker-compose up -d

# View logs
docker-compose logs -f grading-be
```

#### Production Environment

```bash
# Build production image
docker build -t grading-be:latest .

# Run with production configuration
docker run -d \
  --name grading-be \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e JWT_SECRET=your-production-secret \
  -e POSTGRES_URL=your-db-url \
  grading-be:latest
# Build production image
docker build -t grading-be:latest .

# Run with production configuration
docker run -d \
  --name grading-be \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e JWT_SECRET=your-production-secret \
  -e POSTGRES_URL=your-db-url \
  grading-be:latest
```

### Manual Deployment

#### Prerequisites for Production

-   Java 21 JRE or JDK
-   PostgreSQL 14+ database server
-   Redis 6+ server
-   Minimum 1GB RAM, 2 CPU cores
-   SSL certificate for HTTPS

#### Build and Deploy

```bash
# Build the application
./mvnw clean package -DskipTests -Pprod

# Copy the JAR to your server
scp target/Grading-BE-0.0.1-SNAPSHOT.jar user@server:/opt/grading-be/

# Create systemd service (optional)
sudo tee /etc/systemd/system/grading-be.service > /dev/null <<EOF
[Unit]
Description=Grading BE Application
After=syslog.target

[Service]
User=gradingbe
ExecStart=/usr/bin/java -jar /opt/grading-be/Grading-BE-0.0.1-SNAPSHOT.jar
SuccessExitStatus=143
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
EOF

# Start the service
sudo systemctl enable grading-be
sudo systemctl start grading-be
# Build the application
./mvnw clean package -DskipTests -Pprod

# Copy the JAR to your server
scp target/Grading-BE-0.0.1-SNAPSHOT.jar user@server:/opt/grading-be/

# Create systemd service (optional)
sudo tee /etc/systemd/system/grading-be.service > /dev/null <<EOF
[Unit]
Description=Grading BE Application
After=syslog.target

[Service]
User=gradingbe
ExecStart=/usr/bin/java -jar /opt/grading-be/Grading-BE-0.0.1-SNAPSHOT.jar
SuccessExitStatus=143
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
EOF

# Start the service
sudo systemctl enable grading-be
sudo systemctl start grading-be
```

### Environment Configuration for Production

### Environment Configuration for Production

```bash
# Create environment file
sudo tee /opt/grading-be/.env > /dev/null <<EOF
SPRING_PROFILES_ACTIVE=prod
JWT_SECRET=your-256-bit-production-secret
POSTGRES_URL=jdbc:postgresql://localhost:5432/grading_db_prod
POSTGRES_USERNAME=grading_user
POSTGRES_PASSWORD=secure-password
REDIS_HOST=localhost
REDIS_PORT=6379
AWS_ACCESS_KEY_ID=your-aws-key
AWS_SECRET_ACCESS_KEY=your-aws-secret
AWS_S3_BUCKET_NAME=your-production-bucket
MAIL_USERNAME=noreply@yourcompany.com
MAIL_PASSWORD=your-mail-password
EOF
# Create environment file
sudo tee /opt/grading-be/.env > /dev/null <<EOF
SPRING_PROFILES_ACTIVE=prod
JWT_SECRET=your-256-bit-production-secret
POSTGRES_URL=jdbc:postgresql://localhost:5432/grading_db_prod
POSTGRES_USERNAME=grading_user
POSTGRES_PASSWORD=secure-password
REDIS_HOST=localhost
REDIS_PORT=6379
AWS_ACCESS_KEY_ID=your-aws-key
AWS_SECRET_ACCESS_KEY=your-aws-secret
AWS_S3_BUCKET_NAME=your-production-bucket
MAIL_USERNAME=noreply@yourcompany.com
MAIL_PASSWORD=your-mail-password
EOF
```

### Load Balancing & Scaling

#### Nginx Configuration

```nginx
upstream grading_backend {
    server 127.0.0.1:8080;
    server 127.0.0.1:8081;  # Additional instances
}

server {
    listen 443 ssl http2;
    server_name api.yourcompany.com;

    ssl_certificate /path/to/ssl/cert.pem;
    ssl_certificate_key /path/to/ssl/private.key;

    location / {
        proxy_pass http://grading_backend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

### Monitoring & Health Checks

```bash
# Health check endpoint
curl https://api.yourcompany.com/actuator/health

# Metrics monitoring
curl https://api.yourcompany.com/actuator/metrics

# Application info
curl https://api.yourcompany.com/actuator/info
```

## Contributing

We welcome contributions to the Grading Backend! Please follow these guidelines:

### Development Workflow

We welcome contributions to the Grading Backend! Please follow these guidelines:

### Development Workflow

1. **Fork the repository**

    ```bash
    git clone https://github.com/your-username/GradBE.git
    cd GradBE
    ```

2. **Create a feature branch**

    ```bash
    git checkout -b feature/your-feature-name
    ```

3. **Set up development environment**

    ```bash
    # Install dependencies
    ./mvnw clean install

    # Start development services
    docker-compose up -d postgres redis
    ```

4. **Make your changes and test**

    ```bash
    # Run tests
    ./mvnw test

    # Run application
    ./mvnw spring-boot:run
    ```

5. **Commit your changes**

    ```bash
    git add .
    git commit -m "feat: add your feature description"
    ```

6. **Push and create Pull Request**
    ```bash
    git push origin feature/your-feature-name
    ```

### Coding Standards

#### Java/Spring Boot Standards

-   **Code Style**: Follow Google Java Style Guide
-   **Naming**: Use clear, descriptive names for classes, methods, and variables
-   **Documentation**: Write JavaDoc for all public methods and classes
-   **Annotations**: Use appropriate Spring annotations (@Service, @Repository, @Controller)
-   **Error Handling**: Implement proper exception handling with custom exceptions
-   **Logging**: Use SLF4J with appropriate log levels
-   **Testing**: Write unit and integration tests for all new features

#### Database Standards

-   **Migrations**: Use JPA/Hibernate for schema changes
-   **Naming**: Use snake_case for database tables and columns
-   **Relationships**: Define proper JPA relationships with appropriate fetch strategies
-   **Indexing**: Add indexes for frequently queried columns

#### API Standards

-   **REST**: Follow RESTful API principles
-   **HTTP Status Codes**: Use appropriate HTTP status codes
-   **Request/Response**: Use DTOs for API request and response objects
-   **Validation**: Validate all input using Bean Validation annotations
-   **Documentation**: Document all endpoints with OpenAPI annotations

### Code Review Process

1. **Self Review**: Review your own code before submitting
2. **Automated Checks**: Ensure all tests pass and code quality checks succeed
3. **Peer Review**: At least one team member must review and approve
4. **Integration Testing**: Test the feature in a staging environment
5. **Documentation**: Update relevant documentation and API specs

### Pull Request Template

```markdown
## Description

Brief description of the changes

## Type of Change

-   [ ] Bug fix
-   [ ] New feature
-   [ ] Breaking change
-   [ ] Documentation update

## Testing

-   [ ] Unit tests added/updated
-   [ ] Integration tests added/updated
-   [ ] Manual testing completed

## Checklist

-   [ ] Code follows the project's coding standards
-   [ ] Self-review completed
-   [ ] Tests pass locally
-   [ ] Documentation updated
-   [ ] No sensitive information exposed
```

### Issue Reporting

When reporting issues, please include:

-   **Environment**: Development, staging, or production
-   **Java Version**: Output of `java -version`
-   **Steps to Reproduce**: Clear steps to reproduce the issue
-   **Expected Behavior**: What you expected to happen
-   **Actual Behavior**: What actually happened
-   **Logs**: Relevant log entries or stack traces
-   **Configuration**: Relevant configuration settings

### Support & Community

-   **Documentation**: Check this README and API documentation first
-   **Issues**: Use GitHub Issues for bug reports and feature requests
-   **Discussions**: Use GitHub Discussions for questions and general discussion
-   **Email**: Contact the development team for sensitive issues

---

## License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.
This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

## Contact

**Development Team**

-   Email: dev-team@yourcompany.com
-   Project Repository: [GitHub Repository URL]
-   Documentation: [Documentation URL]

For urgent security issues, please contact: security@yourcompany.com

---

**Built with ❤️ for academic excellence and student success**
**Built with ❤️ for academic excellence and student success**
