# Spring Boot Concepts

This document explains the core Spring Boot concepts used in this project.

## 1. Spring Boot Starters

**Starters** are dependency descriptors that bundle together all the dependencies needed for a specific feature.

In your `pom.xml`:
```xml
<!-- Web Starter - includes Spring MVC, embedded Tomcat, etc. -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- JPA Starter - includes Hibernate, JPA, etc. -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- MongoDB Starter - includes MongoDB driver, etc. -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

**Benefits:**
- No need to manually manage individual dependencies
- Pre-configured, tested combinations
- Version compatibility handled automatically

## 2. Auto-Configuration

Spring Boot automatically configures your application based on:
- Dependencies on the classpath
- Properties in `application.properties`
- Annotations in your code

**Example:** When you add `spring-boot-starter-web`:
- Automatically configures embedded Tomcat server
- Sets up Spring MVC
- Configures JSON serialization
- Sets up default error handling

Your `application.properties` can override defaults:
```properties
server.port=8080  # Override default port
spring.jpa.show-sql=true  # Enable SQL logging
```

## 3. @SpringBootApplication

Your main class uses this annotation:

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

`@SpringBootApplication` is a combination of three annotations:
- `@Configuration` - Marks this as a configuration class
- `@EnableAutoConfiguration` - Enables Spring Boot's auto-configuration
- `@ComponentScan` - Scans for components in the package and sub-packages

## 4. Dependency Injection (DI)

Spring manages object creation and dependency wiring automatically.

**Example from your controllers:**
```java
@RestController
public class UserRestController {
    @Autowired  // Spring injects UserService automatically
    private UserService userService;
}
```

**How it works:**
1. Spring scans for `@Component`, `@Service`, `@Repository`, `@Controller`
2. Creates singleton instances (by default)
3. Injects dependencies via `@Autowired`

## 5. Component Scanning

Spring automatically discovers and registers beans (components) in your application.

**Key Annotations:**
- `@Component` - Generic component
- `@Service` - Business logic layer
- `@Repository` - Data access layer
- `@Controller` / `@RestController` - Web controllers
- `@Configuration` - Configuration classes

**Example:**
```java
@Service  // Spring finds and registers this
public class UserService { ... }

@Repository  // Spring finds and registers this
public interface UserRepository extends MongoRepository<User, String> { ... }
```

## 6. Application Properties

Configuration is externalized in `application.properties`:

```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:h2:mem:testdb
spring.data.mongodb.host=localhost

# JPA
spring.jpa.hibernate.ddl-auto=update
```

**Benefits:**
- Externalized configuration
- Environment-specific files (`application-dev.properties`, `application-prod.properties`)
- No code changes needed for different environments

## 7. Spring Boot Parent POM

Your `pom.xml` uses:
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.0</version>
</parent>
```

**Provides:**
- Dependency version management
- Default Maven plugin configuration
- Resource filtering
- Java version configuration

## 8. Embedded Server

Spring Boot includes an embedded server (Tomcat by default).

**No need to:**
- Install Tomcat separately
- Deploy WAR files
- Configure server.xml

**Just run:**
```bash
mvn spring-boot:run
# or
java -jar target/springboot-portlet-mongodb-1.0.0.jar
```

## 9. Spring Data Repositories

Spring Data provides repository abstractions that reduce boilerplate code.

**Your repositories:**
```java
// JPA Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Spring automatically implements: save(), findAll(), findById(), etc.
}

// MongoDB Repository
public interface UserRepository extends MongoRepository<User, String> {
    // Spring automatically implements MongoDB operations
}
```

**Spring Data:**
- Automatically implements common methods
- Generates queries from method names
- Supports custom queries with `@Query`

## 10. Profiles

Separate configurations for different environments:

```properties
# application-dev.properties
spring.datasource.url=jdbc:h2:mem:testdb

# application-prod.properties
spring.datasource.url=jdbc:postgresql://prod-server:5432/mydb
```

**Activate with:**
```bash
java -jar app.jar --spring.profiles.active=prod
```

## 11. Configuration Classes

Custom configuration using `@Configuration`:

```java
@Configuration
public class CorsConfig {
    @Bean  // Creates a bean in Spring context
    public CorsFilter corsFilter() {
        // Configuration code
    }
}
```

**`@Bean` methods** create beans that are managed by Spring.

## 12. Actuator (Not in your project, but useful)

Add monitoring and management endpoints:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

**Provides endpoints like:**
- `/actuator/health` - Health check
- `/actuator/info` - Application info
- `/actuator/metrics` - Application metrics

## 13. DevTools

In your `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
</dependency>
```

**Features:**
- Automatic restart on code changes
- LiveReload support
- Property defaults for development

## 14. Spring Boot Maven Plugin

```xml
<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
</plugin>
```

**Enables:**
- `mvn spring-boot:run` - Run the application
- `mvn spring-boot:build-image` - Create Docker images
- Creates executable JARs with embedded dependencies

## 15. Convention over Configuration

Spring Boot uses sensible defaults, reducing configuration needs.

**Examples:**
- Default port: 8080
- Default context path: `/`
- Default error pages
- Default JSON serialization

**Override** via `application.properties` when needed.

## Architecture in Your Project

```
Application.java (Main Entry Point)
    ↓
@SpringBootApplication enables:
    ├── Component Scanning
    │   ├── @RestController → UserRestController, ProductRestController
    │   ├── @Service → UserService, ProductService
    │   └── @Repository → UserRepository, ProductRepository
    │
    ├── Auto-Configuration
    │   ├── Embedded Tomcat Server
    │   ├── Spring MVC
    │   ├── JPA/Hibernate
    │   └── MongoDB
    │
    └── Configuration
        ├── application.properties
        └── @Configuration classes (CorsConfig)
```

## Key Benefits

1. **Rapid Development** - Less boilerplate code
2. **Production Ready** - Embedded server, monitoring, etc.
3. **Opinionated** - Sensible defaults
4. **Standalone** - No external server needed
5. **Microservices Friendly** - Easy to build and deploy

## Summary

Spring Boot simplifies Spring development by:
- Providing **starters** for common needs
- **Auto-configuring** based on classpath
- Using **convention over configuration**
- Including **embedded server**
- Offering **production-ready** features

Your project uses these concepts to create a REST API with MongoDB and JPA with minimal configuration.


