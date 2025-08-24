# GradingWeb

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![Version](https://img.shields.io/badge/version-0.0.1--SNAPSHOT-blue)
![License](https://img.shields.io/badge/license-Apache%25202.0-green)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-green)
![React](https://img.shields.io/badge/React-19.1.0-blue)

A comprehensive university student grading and academic management system built with modern technologies. This platform provides tools for managing student profiles, grades, academic records, and includes a forum system for academic discussions.

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
-   [Deployment](#deployment)
-   [Contributing](#contributing)
-   [License](#license)
-   [Authors](#authors)
-   [Acknowledgments](#acknowledgments)

## Features

### Current Features

-   **Student Management**: Complete student profile management with academic records
-   **Grade Management**: Track and manage student grades across subjects
-   **Academic Records**: Maintain comprehensive academic history
-   **User Authentication**: Secure JWT-based authentication with OAuth2 support
-   **Forum System**: Discussion platform for academic topics with posts, comments, and channels
-   **University & Major Management**: Manage universities, majors, and academic programs
-   **Geographic Data**: Province and school management system
-   **Media Management**: File upload and management with AWS S3 integration
-   **Reporting System**: Generate academic reports and analytics
-   **Real-time Notifications**: Email notifications for important updates
-   **Caching System**: Redis-based caching for improved performance
-   **Rate Limiting**: API rate limiting for security and performance

### Planned Features

-   [ ] Mobile application support
-   [ ] Advanced analytics dashboard
-   [ ] Bulk data import/export
-   [ ] Real-time chat system
-   [ ] Advanced search and filtering
-   [ ] Multi-language support

## Technology Stack

### Backend (Grading-BE)

-   **Java 21** - Programming language
-   **Spring Boot 3.3.5** - Application framework
-   **Spring Security** - Authentication and authorization
-   **Spring Data JPA** - Data persistence
-   **PostgreSQL** - Primary database
-   **Redis** - Caching and session storage
-   **JWT** - Token-based authentication
-   **AWS S3** - File storage
-   **Spring Mail** - Email notifications
-   **MapStruct** - Object mapping
-   **Lombok** - Boilerplate code reduction
-   **SpringDoc OpenAPI** - API documentation

### Frontend (Grading-FE)

-   **React 19** - Frontend framework
-   **TypeScript** - Type-safe JavaScript
-   **Vite** - Build tool and development server
-   **Tailwind CSS** - Utility-first CSS framework
-   **Zustand** - State management
-   **Axios** - HTTP client
-   **React Hook Form** - Form management
-   **React Router 7** - Client-side routing
-   **Lucide React** - Icon library

### DevOps & Tools

-   **Docker & Docker Compose** - Containerization
-   **Maven** - Backend dependency management
-   **npm** - Frontend package management
-   **pgAdmin** - PostgreSQL administration

## Prerequisites

Before running this project, make sure you have the following installed:

-   **Java 21** or higher
-   **Node.js 18+** and npm
-   **Docker** and Docker Compose
-   **Git**
-   **Maven 3.6+** (optional, wrapper included)

## Installation

### 1. Clone the Repository

```bash
git clone https://github.com/PT-LoiX86/GradingWeb.git
cd GradingWeb
```

### 2. Environment Setup

Create environment files:

```bash
# Copy environment template
cp .env.example .env
```

Configure your `.env` file with the following variables:

```env
# Database Configuration
POSTGRES_USER=root
POSTGRES_PASSWORD=root
POSTGRES_DB=grading_db

# pgAdmin Configuration
PGADMIN_DEFAULT_EMAIL=admin@gradingweb.com
PGADMIN_DEFAULT_PASSWORD=admin

# JWT Configuration
JWT_SECRET=your-secret-key
JWT_EXPIRATION=86400000

# AWS S3 Configuration
AWS_ACCESS_KEY_ID=your-access-key
AWS_SECRET_ACCESS_KEY=your-secret-key
AWS_S3_BUCKET_NAME=your-bucket-name
AWS_REGION=your-region

# Email Configuration
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=your-email
SPRING_MAIL_PASSWORD=your-password

# Redis Configuration
REDIS_HOST=localhost
REDIS_PORT=6379
```

### 3. Backend Setup

```bash
cd Grading-BE

# Using Maven wrapper (recommended)
./mvnw clean install

# Or using system Maven
mvn clean install
```

### 4. Frontend Setup

```bash
cd Grading-FE

# Install dependencies
npm install

# Build for production
npm run build
```

### 5. Docker Setup (Recommended)

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

## Configuration

### Backend Configuration

Key configuration files:

-   `Grading-BE/src/main/resources/application.yml` - Main application configuration
-   `Grading-BE/src/main/resources/logback-spring.xml` - Logging configuration

### Frontend Configuration

-   `Grading-FE/vite.config.ts` - Vite build configuration
-   `Grading-FE/tailwind.config.js` - Tailwind CSS configuration
-   `Grading-FE/tsconfig.json` - TypeScript configuration

### Database Configuration

The application uses PostgreSQL as the primary database. Schema and initial data are managed through JPA migrations.

## Usage

### Development Mode

#### Backend

```bash
cd Grading-BE
./mvnw spring-boot:run
```

The backend will be available at `http://localhost:8080`

#### Frontend

```bash
cd Grading-FE
npm run dev
```

The frontend will be available at `http://localhost:5173`

### Production Mode

```bash
# Start all services with Docker
docker-compose up -d

# Or build and run separately
cd Grading-BE
./mvnw spring-boot:run -Dspring.profiles.active=prod

cd Grading-FE
npm run build
npm run preview
```

### API Endpoints

Once the backend is running, you can access:

-   **API Base URL**: `http://localhost:8080/api`
-   **Swagger UI**: `http://localhost:8080/swagger-ui.html`
-   **API Docs**: `http://localhost:8080/v3/api-docs`

### Database Administration

-   **pgAdmin**: `http://localhost:5050`
    -   Email: `admin@gradingweb.com`
    -   Password: `admin`

## Project Structure

```
GradingWeb/
├── docker-compose.yml              # Docker services configuration
├── LICENSE                         # Apache 2.0 License
├── README.md                       # This file
├── package.json                    # Root package.json
├── Grading-BE/                     # Backend application
│   ├── src/main/java/com/grd/gradingbe/
│   │   ├── controller/             # REST Controllers
│   │   ├── service/                # Business Logic Services
│   │   ├── repository/             # Data Access Layer
│   │   ├── model/                  # JPA Entities
│   │   ├── dto/                    # Data Transfer Objects
│   │   ├── configuration/          # Configuration Classes
│   │   ├── exception/              # Exception Handlers
│   │   └── GradingBeApplication.java # Main Application Class
│   ├── src/main/resources/
│   │   ├── application.yml         # Application Configuration
│   │   └── logback-spring.xml      # Logging Configuration
│   ├── src/test/                   # Test Classes
│   ├── Dockerfile                  # Backend Docker configuration
│   └── pom.xml                     # Maven dependencies
└── Grading-FE/                     # Frontend application
    ├── src/
    │   ├── components/             # React Components
    │   ├── contexts/               # React Contexts
    │   ├── hooks/                  # Custom Hooks
    │   ├── services/               # API Services
    │   ├── store/                  # State Management
    │   ├── types/                  # TypeScript Types
    │   └── main.tsx                # Application Entry Point
    ├── public/                     # Static Assets
    ├── Dockerfile                  # Frontend Docker configuration
    ├── package.json                # Frontend Dependencies
    ├── vite.config.ts              # Vite Configuration
    └── tailwind.config.js          # Tailwind Configuration
```

## API Documentation

### Authentication Endpoints

-   `POST /api/auth/login` - User login
-   `POST /api/auth/register` - User registration
-   `POST /api/auth/refresh` - Refresh JWT token
-   `POST /api/auth/logout` - User logout

### Student Management

-   `GET /api/students` - Get all students
-   `POST /api/students` - Create new student
-   `GET /api/students/{id}` - Get student by ID
-   `PUT /api/students/{id}` - Update student
-   `DELETE /api/students/{id}` - Delete student

### Grade Management

-   `GET /api/grades` - Get grade records
-   `POST /api/grades` - Create grade record
-   `PUT /api/grades/{id}` - Update grade
-   `DELETE /api/grades/{id}` - Delete grade

### Forum System

-   `GET /api/forums/posts` - Get forum posts
-   `POST /api/forums/posts` - Create new post
-   `GET /api/forums/posts/{id}` - Get post by ID
-   `POST /api/forums/posts/{id}/comments` - Add comment to post

For complete API documentation, visit the Swagger UI at `http://localhost:8080/swagger-ui.html` when the application is running.

## Testing

### Backend Testing

```bash
cd Grading-BE

# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=GradingBeApplicationTests

# Generate test coverage report
./mvnw jacoco:report
```

### Frontend Testing

```bash
cd Grading-FE

# Run tests
npm test

# Run tests with coverage
npm run test:coverage

# Run e2e tests
npm run test:e2e
```

### Test Coverage

Test coverage reports are generated in:

-   Backend: `Grading-BE/target/site/jacoco/index.html`
-   Frontend: `Grading-FE/coverage/index.html`

## Deployment

### Docker Deployment

```bash
# Production deployment with Docker
docker-compose -f docker-compose.prod.yml up -d
```

### Manual Deployment

#### Backend

```bash
cd Grading-BE
./mvnw clean package -DskipTests
java -jar target/Grading-BE-0.0.1-SNAPSHOT.jar
```

#### Frontend

```bash
cd Grading-FE
npm run build
# Deploy the 'dist' folder to your web server
```

### CI/CD Pipeline

The project includes GitHub Actions workflows for:

-   Automated testing on pull requests
-   Building and deploying to staging environment
-   Production deployment on main branch

## Contributing

We welcome contributions! Please follow these steps:

1. **Fork the repository**
2. **Create a feature branch**
    ```bash
    git checkout -b feature/amazing-feature
    ```
3. **Make your changes and commit**
    ```bash
    git commit -m 'Add some amazing feature'
    ```
4. **Push to the branch**
    ```bash
    git push origin feature/amazing-feature
    ```
5. **Open a Pull Request**

### Coding Standards

#### Backend (Java)

-   Follow Java naming conventions
-   Use Lombok annotations to reduce boilerplate
-   Write comprehensive JavaDoc for public methods
-   Maintain test coverage above 80%

#### Frontend (TypeScript/React)

-   Use TypeScript for type safety
-   Follow React functional component patterns
-   Use custom hooks for reusable logic
-   Implement proper error boundaries

### Pull Request Template

Please include:

-   Description of changes
-   Screenshots (if UI changes)
-   Test coverage information
-   Breaking changes (if any)

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Authors

-   **Development Team** - _Initial work and maintenance_
-   **University Contributors** - _Academic guidance and requirements_

For questions or support, please contact the development team.

## Acknowledgments

-   **Spring Boot Community** - For the excellent framework and documentation
-   **React Community** - For the robust frontend ecosystem
-   **University Partners** - For providing requirements and feedback
-   **Open Source Contributors** - For the amazing libraries and tools used in this project

### Key Dependencies

#### Backend

-   [Spring Boot](https://spring.io/projects/spring-boot) - Application framework
-   [Spring Security](https://spring.io/projects/spring-security) - Security framework
-   [PostgreSQL](https://www.postgresql.org/) - Database system
-   [Redis](https://redis.io/) - In-memory data structure store
-   [JWT](https://jwt.io/) - JSON Web Tokens for authentication

#### Frontend

-   [React](https://reactjs.org/) - Frontend framework
-   [Vite](https://vitejs.dev/) - Build tool
-   [Tailwind CSS](https://tailwindcss.com/) - CSS framework
-   [Zustand](https://github.com/pmndrs/zustand) - State management
-   [React Hook Form](https://react-hook-form.com/) - Form library

---

**Built with ❤️ for academic excellence**
