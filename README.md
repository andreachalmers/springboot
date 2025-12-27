# Spring Boot Application with Portlet, MongoDB, JPA, REST API, and Vue.js

This is a comprehensive Spring Boot application that integrates multiple technologies:

- **Spring Boot 3.2.0** - Main framework
- **Portlet Framework** - Java Portlet API 3.0 for portal development
- **MongoDB** - NoSQL database for document storage
- **JPA/Hibernate** - For relational database operations with JPQL queries
- **REST API** - RESTful endpoints for frontend consumption
- **Vue.js 3** - Modern frontend framework

## Project Structure

```
.
├── src/
│   ├── main/
│   │   ├── java/com/example/
│   │   │   ├── Application.java              # Main Spring Boot application
│   │   │   ├── config/                       # Configuration classes
│   │   │   ├── mongodb/                      # MongoDB models and repositories
│   │   │   ├── jpa/                          # JPA entities and repositories
│   │   │   ├── service/                      # Business logic services
│   │   │   ├── rest/controller/              # REST API controllers
│   │   │   └── portlet/controller/           # Portlet controllers
│   │   ├── resources/
│   │   │   └── application.properties        # Application configuration
│   │   └── webapp/WEB-INF/                   # Portlet configuration
│   └── test/                                 # Test files
├── frontend/                                 # Vue.js frontend application
│   ├── src/
│   │   ├── components/                       # Vue components
│   │   ├── App.vue                           # Main Vue app
│   │   └── main.js                           # Vue entry point
│   ├── index.html
│   ├── package.json
│   └── vite.config.js
└── pom.xml                                   # Maven dependencies

```

## Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **MongoDB** (running on localhost:27017)
- **Node.js 16+** and npm (for Vue.js frontend)

## Important Notes

### Portlet Support
**Portlets require a portlet container (like Liferay) to run.** The portlet implementation is included for demonstration purposes, but:
- Portlets will NOT work in a standalone Spring Boot application
- To use portlets, deploy the application to a portlet container (e.g., Liferay Portal)
- For standalone applications, use the **REST API endpoints** instead (recommended)
- The Vue.js frontend uses the REST API, which works perfectly in standalone mode

### Dependency Resolution
The project uses `javax.portlet:portlet-api` which is available in Maven Central. If you encounter dependency issues:
- Run `mvn clean install -U` to force update dependencies
- The portlet API is marked as `provided` scope since it's typically provided by the portlet container

## Setup Instructions

### 1. MongoDB Setup

Make sure MongoDB is installed and running:

```bash
# macOS (using Homebrew)
brew services start mongodb-community

# Or using Docker
docker run -d -p 27017:27017 --name mongodb mongo:latest
```

### 2. Backend Setup

```bash
# Navigate to project root
cd /Users/andreachalmers/development/cursor_java_

# Build the project
mvn clean install

# Run the Spring Boot application
mvn spring-boot:run
```

The backend will be available at `http://localhost:8080`

### 3. Frontend Setup

```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Start development server
npm run dev
```

The frontend will be available at `http://localhost:5173`

## API Endpoints

### Users API (MongoDB)

- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/username/{username}` - Get user by username
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user
- `GET /api/users/search/email?pattern={pattern}` - Search users by email pattern

### Products API (JPA)

- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create new product
- `PUT /api/products/{id}` - Update product
- `DELETE /api/products/{id}` - Delete product
- `GET /api/products/search?keyword={keyword}` - Search products (JPQL)
- `GET /api/products/available?minPrice={price}` - Get available products above price (JPQL)
- `GET /api/products/price-range?minPrice={min}&maxPrice={max}` - Get products by price range

## Features

### MongoDB Integration
- Spring Data MongoDB repositories
- Document-based storage for User entities
- Custom MongoDB queries

### JPA Integration
- JPA entities with Hibernate
- JPQL queries for complex database operations
- Native SQL queries support
- H2 in-memory database for development (can be switched to PostgreSQL)

### REST API
- RESTful endpoints with proper HTTP methods
- CORS configuration for frontend integration
- Request validation
- Error handling

### Portlet Support
- Java Portlet API 3.0
- Portlet controllers for portal integration
- Portlet configuration files

### Vue.js Frontend
- Vue 3 with Composition API
- Vue Router for navigation
- Axios for API calls
- Modern, responsive UI
- Components for Users and Products management

## JPQL Query Examples

The application includes several JPQL query examples in `ProductRepository`:

```java
// Find available products above a certain price
@Query("SELECT p FROM Product p WHERE p.price > :minPrice AND p.stock > 0")
List<Product> findAvailableProductsAbovePrice(@Param("minPrice") BigDecimal minPrice);

// Search products by keyword
@Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% OR p.description LIKE %:keyword%")
List<Product> searchProducts(@Param("keyword") String keyword);
```

## Configuration

### Database Configuration

Edit `src/main/resources/application.properties` to configure:

- MongoDB connection (host, port, database)
- JPA datasource (currently using H2, can be changed to PostgreSQL)
- Server port
- CORS settings

### Switching to PostgreSQL

To use PostgreSQL instead of H2:

1. Update `pom.xml` to include PostgreSQL driver (already included)
2. Update `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/yourdb
spring.datasource.username=youruser
spring.datasource.password=yourpassword
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

## Development

### Running Tests

```bash
mvn test
```

### Building for Production

```bash
# Backend
mvn clean package

# Frontend
cd frontend
npm run build
```

## Technologies Used

- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring Data MongoDB**
- **Hibernate**
- **MongoDB**
- **H2 Database** (development)
- **PostgreSQL** (production-ready)
- **Java Portlet API 3.0**
- **Vue.js 3**
- **Vite**
- **Axios**
- **Lombok**


