# The integration module

It stores the integration tests for the application.

To run the integration tests, you need to have Docker installed on your machine.

The integration tests now use **Testcontainers with Docker Compose** managed by **Spring's dependency injection** to automatically start and configure the required services from your existing `docker-compose.yml`:
- PostgreSQL database
- Your Spring Boot application
- WireMock servers for external API mocking (file-api and OIDC)

## Architecture

The integration test setup uses Spring's proper dependency injection patterns:

- **`TestContainerConfiguration`**: A Spring `@TestConfiguration` that manages Docker Compose as a singleton bean
- **`SharedDockerComposeContainer`**: Thread-safe singleton holder that ensures only one Docker Compose instance
- **`ContainerPropertyConfigurer`**: Spring-managed bean for configuring dynamic properties  
- **`IntegrationTest`**: Base class that uses Spring's `@DynamicPropertySource` to configure properties

### Spring Benefits

✅ **Proper Spring patterns**: Uses `@TestConfiguration`, `@Bean`, and `@Scope("singleton")`  
✅ **Dependency injection**: Container lifecycle managed by Spring's IoC container  
✅ **Thread safety**: Double-checked locking pattern for safe singleton initialization  
✅ **Automatic cleanup**: Spring handles bean lifecycle and cleanup  
✅ **Configuration management**: Spring manages all container-related beans  

The Docker Compose services are automatically started before the first test runs and stopped after all tests complete.
Each test will clear the database tables to ensure test isolation.

Each Integration test has to extend the `IntegrationTest` class.

```shell
./gradlew integrationTest
```

## What's changed from manual Docker Compose

- **Spring-managed lifecycle**: Docker Compose containers are managed as Spring beans
- **Singleton scope**: Spring ensures only one Docker Compose instance across all tests
- **Dependency injection**: Proper Spring IoC patterns instead of static objects
- **Thread-safe initialization**: Safe concurrent access to shared container instance
- **Dynamic port mapping**: Tests use the actual exposed ports from Docker Compose
- **Same service configuration**: Uses your existing `docker-compose.yml` exactly as configured
- **No manual setup required**: No need to run `docker compose up/down` manually

## Container Configuration

The integration tests use your existing `docker-compose.yml` services:
- **PostgreSQL 16.3**: Exact same configuration as your compose file
- **Spring Boot App**: Your actual application with all its dependencies
- **WireMock services**: File-api and OIDC mocks with the same stub mappings
- **Spring-managed cleanup**: All compose services are cleaned up by Spring's lifecycle management

This approach ensures your integration tests run against the exact same service configuration as your development and production environments, while using proper Spring dependency injection patterns for container management.

