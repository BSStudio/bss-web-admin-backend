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

Local runs are never cached or considered up to date, because Gradle cannot see the externally
started stack.

## CI (`ci` profile)

GitHub Actions runs a self-contained integration test: Testcontainers starts the compose stack,
runs tests, and tears it down. To reproduce locally:

```shell
CI=true ./gradlew integrationTest
```

To skip Docker image rebuilds (faster when images are already present locally):

```shell
CI=true ./gradlew integrationTest -Pintegration.rebuildImages=false
```

## Requirements

Docker must be installed and running.
