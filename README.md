# Forms Microservice

A Spring Boot microservice for dynamic form management and data collection within the Corelate platform. This service provides comprehensive form creation, schema management, dynamic table generation, and encryption capabilities.

## Overview

The Forms microservice is part of the Corelate ecosystem, designed to handle dynamic form creation, validation, and data persistence. It supports flexible form schemas, dynamic database table creation, and secure data encryption/decryption.

## Technology Stack

- **Java**: 21 (Eclipse Temurin)
- **Spring Boot**: 3.2.1
- **Spring Cloud**: 2023.0.1
- **Database**: PostgreSQL
- **Build Tool**: Maven 3.9
- **Container**: Google Distroless (gcr.io/distroless/java21-debian12)
- **CI/CD**: GitHub Actions with AWS ECR

### Key Dependencies

- **Spring Data JPA**: Database operations and ORM
- **Spring Cloud Config**: Centralized configuration management
- **Spring Cloud Netflix Eureka**: Service discovery and registration
- **Spring Cloud OpenFeign**: Declarative REST client
- **Resilience4j**: Circuit breaker, retry, and rate limiting
- **Kafka**: Event streaming via Spring Cloud Stream
- **OpenTelemetry**: Distributed tracing and observability (v1.32.0)
- **Micrometer**: Metrics collection with Prometheus registry
- **SpringDoc OpenAPI**: API documentation (v2.5.0)
- **JWT (jjwt)**: Token-based authentication (v0.11.5)
- **Lombok**: Boilerplate code reduction
- **JSR-305**: Annotations for software defect detection

## Features

### Core Functionality

- **Dynamic Form Management**
  - Create, read, update forms with custom schemas
  - Form field validation and type management
  - Form versioning and metadata tracking

- **Schema Management**
  - Define custom form schemas with components
  - Support for various field types and validations
  - Schema-based form rendering

- **Dynamic Table Operations**
  - Runtime database table creation based on schemas
  - Dynamic data insertion and retrieval
  - JSON-based table data export

- **Encryption Services**
  - RSA key pair generation
  - Message encryption and decryption
  - Secure data handling

### Enterprise Features

- **Service Discovery**: Eureka client integration
- **Circuit Breaker**: Resilience4j for fault tolerance
- **Distributed Tracing**: OpenTelemetry instrumentation
- **Event Streaming**: Kafka integration for async communication
- **Health Monitoring**: Spring Actuator endpoints
- **API Documentation**: Interactive Swagger UI

## Architecture

### Package Structure

```
com.corelate.forms
├── constants/          # Application constants and template definitions
├── controllers/        # REST API endpoints
│   ├── ApiController           # Build info and system endpoints
│   ├── EncryptionController    # Encryption/decryption operations
│   └── FormController          # Form CRUD operations
├── dto/               # Data Transfer Objects
├── entity/            # JPA entities
│   ├── BaseEntity              # Common entity fields
│   ├── Form                    # Form entity
│   ├── FormField               # Form field entity
│   ├── FormSchema              # Schema definition entity
│   ├── FormValue               # Form data values
│   └── SchemaComponent         # Schema component entity
├── exeption/          # Custom exceptions and global handler
├── functions/         # Business logic functions
├── mapper/            # Entity-DTO mappers
├── repository/        # JPA repositories
├── service/           # Service layer
│   ├── client/                 # Feign clients
│   └── impl/                   # Service implementations
└── utility/           # Utility classes (JWT, etc.)
```

### Design Patterns

- **Repository Pattern**: Data access abstraction
- **Service Layer Pattern**: Business logic separation
- **DTO Pattern**: Data transfer and validation
- **Mapper Pattern**: Entity-DTO conversion
- **Exception Handling**: Global exception handler

## Configuration

### Application Properties

**Server Configuration**
- Port: `8083`
- Application Name: `forms`

**Database Configuration**
- URL: `jdbc:postgresql://localhost:5434/formsdb`
- Username: `postgres`
- Password: `password`
- Hibernate DDL: `update`

**Service Discovery**
- Eureka Server: `http://localhost:8070/eureka/`

**Config Server**
- URL: `http://localhost:8071/`

**Kafka Configuration**
- Broker: `localhost:9092`
- Input Binding: `communication-sent`
- Output Binding: `send-communication`

### Resilience Configuration

**Circuit Breaker**
- Sliding Window Size: 10
- Failure Rate Threshold: 50%
- Wait Duration in Open State: 10s

**Retry Policy**
- Max Attempts: 3
- Wait Duration: 500ms
- Exponential Backoff: 2x multiplier

**Rate Limiter**
- Timeout: 1s
- Refresh Period: 5s
- Limit per Period: 1

## API Endpoints

### Form Management

- `POST /api/forms` - Create a new form
- `GET /api/forms/{formId}` - Fetch form by ID
- `GET /api/forms` - Fetch all forms
- `PUT /api/forms` - Update existing form

### Encryption

- `GET /api/encryption/createkeys` - Generate RSA key pair
- `POST /api/encryption/encrypt` - Encrypt message
- `POST /api/encryption/decrypt` - Decrypt message

### System Information

- `GET /api/build-info` - Get build version
- `GET /api/java-version` - Get Java version

### Actuator Endpoints

- `/actuator/health` - Health check (liveness & readiness probes)
- `/actuator/prometheus` - Prometheus metrics
- `/actuator/info` - Application information
- `/actuator/*` - All actuator endpoints exposed

## Getting Started

### Prerequisites

- Java 21 (Eclipse Temurin recommended)
- Maven 3.9+
- PostgreSQL 12+
- Docker (optional, for containerized deployment)
- Kafka (for event streaming)
- Eureka Server (for service discovery)
- Config Server (for centralized configuration)

### Local Development

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd forms
   ```

2. **Configure PostgreSQL**
   ```bash
   # Create database
   createdb -U postgres -p 5434 formsdb
   ```

3. **Update application.yml** (if needed)
   - Database credentials
   - Kafka broker address
   - Eureka server URL
   - Config server URL

4. **Build the project**
   ```bash
   ./mvnw clean install
   ```

5. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

6. **Access the application**
   - Application: http://localhost:8083
   - Swagger UI: http://localhost:8083/swagger-ui.html
   - Actuator: http://localhost:8083/actuator

### Docker Deployment

#### Using Maven Jib Plugin (Recommended)

The project uses Google Distroless base image for enhanced security (95% CVE reduction):

```bash
# Build and push to ECR (requires AWS authentication)
./mvnw clean compile jib:build

# Build to local Docker daemon
./mvnw clean compile jib:dockerBuild
```

**Image Details:**
- **Base Image**: `gcr.io/distroless/java21-debian12`
- **ECR Repository**: `689635266888.dkr.ecr.ap-southeast-1.amazonaws.com/bpmn/corelate_forms_ms`
- **Security**: Minimal attack surface, no shell, production-ready
- **Size**: ~30MB smaller than standard JRE images

#### CI/CD with GitHub Actions

The project includes automated CI/CD pipeline that:
- Builds on push to `main` or `develop` branches
- Uses OIDC authentication (no long-lived credentials)
- Generates immutable image tags:
  - Full commit SHA
  - Short commit SHA (7 chars)
  - Branch + timestamp
  - Version + build number + SHA

**Required GitHub Secret:**
- `AWS_ROLE_ARN`: `arn:aws:iam::689635266888:role/GitHubActionsECRRole`

#### Using Dockerfile (Alternative)

```bash
# Build image
docker build -f src/main/resources/Dockerfile -t forms:latest .

# Run container
docker run -p 8083:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5434/formsdb \
  forms:latest
```

### Windows-Specific Commands

```bash
# Build project
mvnw.cmd clean install

# Run application
mvnw.cmd spring-boot:run

# Build Docker image with Jib
mvnw.cmd clean compile jib:build

# Build to local Docker daemon
mvnw.cmd clean compile jib:dockerBuild
```

## Monitoring & Observability

### Health Checks

- **Liveness Probe**: `/actuator/health/liveness`
- **Readiness Probe**: `/actuator/health/readiness`

### Metrics

- Prometheus metrics available at `/actuator/prometheus`
- Custom application metrics tagged with service name
- JVM, HTTP, and database metrics included

### Distributed Tracing

- OpenTelemetry Java agent automatically instruments the application
- Trace ID and Span ID included in logs
- Pattern: `%5p [forms,%X{trace_id},%X{span_id}]`

### Logging

- Debug level for `com.corelate.forms` package
- Kafka logging disabled (OFF)
- Structured logging with trace context

### Debugging Distroless Containers

Since distroless images have no shell, use Spring Boot Actuator for debugging:

```bash
# Health check
curl http://localhost:8083/actuator/health

# Metrics
curl http://localhost:8083/actuator/metrics

# Environment variables
curl http://localhost:8083/actuator/env

# Thread dump
curl http://localhost:8083/actuator/threaddump

# Heap dump
curl http://localhost:8083/actuator/heapdump -O
```

## Security

### Container Security

- **Distroless Base Image**: Minimal attack surface with no shell or package managers
- **Immutable Tags**: All deployments use immutable image tags for audit trail
- **OIDC Authentication**: GitHub Actions uses OIDC (no long-lived AWS credentials)
- **IAM Least Privilege**: ECR access limited to specific repositories

### Application Security

- **Encryption**: RSA encryption for sensitive data via configurable `encrypt.key` property
- **JWT Support**: Token parsing and validation with JJWT library (v0.11.5)
- **Input Validation**: Bean validation on all DTOs
- **Note**: Spring Security dependencies are commented out (not currently active)

### Security Best Practices

✅ **Implemented:**
- Distroless container (95% CVE reduction)
- Immutable image tags
- OIDC authentication for CI/CD
- Actuator endpoints for monitoring

🔒 **Recommended:**
- Enable ECR image scanning
- Implement Spring Security for authentication/authorization
- Use AWS Secrets Manager for sensitive configuration
- Regular dependency updates via Dependabot

## Development

### Code Quality

- Lombok annotations for cleaner code
- Validation annotations on DTOs
- Global exception handling
- Consistent error response format

### Testing

```bash
# Run tests
./mvnw test

# Run tests with coverage
./mvnw test jacoco:report
```

Test class: `AppManagementApplicationTests.java`

### API Documentation

Access interactive API documentation:
- Swagger UI: http://localhost:8083/swagger-ui.html
- OpenAPI JSON: http://localhost:8083/v3/api-docs

## Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Verify PostgreSQL is running on configured port
   - Check database credentials in application.yml or environment variables
   - Ensure `formsdb` database exists
   - Verify network connectivity in containerized environments

2. **Eureka Registration Failed**
   - Confirm Eureka server is running and accessible
   - Check network connectivity
   - Verify `eureka.client.serviceUrl.defaultZone` configuration
   - Check DNS resolution for service names in Kubernetes/Docker

3. **Kafka Connection Issues**
   - Ensure Kafka broker is running and accessible
   - Verify topic names match configuration
   - Check Kafka logs for errors
   - Verify `KAFKA_BOOTSTRAP_SERVERS` environment variable

4. **Port Already in Use**
   - Change `server.port` in application.yml
   - Or stop the process using port 8083

5. **Jib Build Failures**
   - Ensure Docker daemon is running (for `jib:dockerBuild`)
   - Verify AWS credentials for ECR (for `jib:build`)
   - Check network connectivity to ECR
   - Verify mainClass is correctly set in pom.xml

6. **GitHub Actions Build Failures**
   - Verify `AWS_ROLE_ARN` secret is set in repository
   - Check IAM role permissions for ECR
   - Ensure ECR repository exists
   - Review GitHub Actions logs for specific errors

7. **Application Won't Start in Distroless**
   - Check logs via `docker logs <container-id>`
   - Verify all required environment variables are set
   - Use actuator endpoints to debug (no shell access)
   - Ensure mainClass in pom.xml matches actual main class

### Debug Mode

Enable debug logging:
```yaml
logging:
  level:
    com.corelate.forms: DEBUG
```

For container debugging without shell access:
```bash
# View logs
docker logs -f <container-id>

# Check if container is running
docker ps | grep forms

# Inspect container
docker inspect <container-id>
```

## Project Information

- **Group ID**: com.corelate
- **Artifact ID**: forms
- **Version**: 0.0.1-SNAPSHOT
- **Packaging**: JAR
- **Description**: Forms Microservice for Corelate
- **Main Class**: com.corelate.forms.AppManagementApplication

## CI/CD Pipeline

### GitHub Actions Workflow

The project uses GitHub Actions for automated builds and deployments:

**Triggers:**
- Push to `main` or `develop` branches
- Manual workflow dispatch

**Pipeline Steps:**
1. Checkout code
2. Set up JDK 21 with Maven cache
3. Configure AWS credentials via OIDC
4. Login to Amazon ECR
5. Generate immutable image tags
6. Build and push Docker image with Jib
7. Display image details

**Image Tags Generated:**
- Full commit SHA (primary)
- Short commit SHA (7 characters)
- Branch name + timestamp
- Version + build number + SHA

### ECR Configuration

- **Repository**: `689635266888.dkr.ecr.ap-southeast-1.amazonaws.com/bpmn/corelate_forms_ms`
- **Region**: ap-southeast-1
- **Tag Mutability**: Immutable (recommended)
- **Authentication**: OIDC via GitHubActionsECRRole

## Contributing

1. Follow Spring Boot best practices
2. Maintain consistent code formatting
3. Write unit tests for new features
4. Update API documentation
5. Follow the existing package structure
6. Ensure Lombok DTOs extending BaseEntity use `@EqualsAndHashCode(callSuper = true)`
7. Test builds locally before pushing

## License

Proprietary - Corelate Platform

## Contact

For issues or questions, contact the Corelate development team.

---

## Quick Reference

**Build Version**: Configurable via `build.version` property  
**ECR Image**: `689635266888.dkr.ecr.ap-southeast-1.amazonaws.com/bpmn/corelate_forms_ms`  
**Base Image**: `gcr.io/distroless/java21-debian12`  
**Security**: Distroless (95% CVE reduction, no shell)  
**Deployment**: Immutable tags via GitHub Actions
