# The integration module

It stores the integration tests for the application.

To run the integration tests, you need to have Docker installed on your machine.

The integration tests now use **Testcontainers with Docker Compose** to automatically start and configure the required services from your existing `docker-compose.yml`:
- PostgreSQL database
- WireMock servers for external API mocking (file-api and OIDC)

The Docker Compose services are automatically started before the tests run and stopped after completion.
Each test will clear the database tables to ensure test isolation.

Each Integration test has to extend the `IntegrationTest` class.

```shell
./gradlew integrationTest
```

## What's changed from manual Docker Compose

- **Automatic lifecycle management**: Testcontainers automatically starts/stops your Docker Compose services
- **Dynamic port mapping**: Tests use the actual exposed ports from Docker Compose
- **Same service configuration**: Uses your existing `docker-compose.yml` exactly as configured
- **Isolated test environment**: Each test run gets fresh containers
- **No manual setup required**: No need to run `docker compose up/down` manually

## Container Configuration

The integration tests use your existing `docker-compose.yml` services:
- **PostgreSQL 16.3**: Exact same configuration as your compose file
- **WireMock 3.13.1**: File-api and OIDC mocks with the same stub mappings
- **Automatic cleanup**: All compose services are removed after test completion

This approach ensures your integration tests run against the exact same service configuration as your development and production environments.

