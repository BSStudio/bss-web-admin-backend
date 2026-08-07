# The integration module

It stores the integration tests for the application.

Each integration test extends the `IntegrationTest` class. Tests use a real Postgres database and
call the application over HTTP. Each test clears the database tables in `@BeforeEach`.

## Local (default profile)

Start the compose stack once, then re-run tests freely:

```shell
docker compose --profile app up -d
./gradlew integrationTest
```

Optionally start the app via Spring Boot (requires `spring-boot-docker-compose` on the server module):

```shell
./gradlew :server:bootRun
./gradlew integrationTest
```

## CI (`ci` profile)

GitHub Actions runs a self-contained integration test: Testcontainers starts the compose stack,
runs tests, and tears it down. To reproduce locally:

```shell
CI=true REBUILD_IMAGES=true ./gradlew integrationTest
```

## Requirements

Docker must be installed and running.
