# The integration module
It stores the integration tests for the application.

To run the integration tests, you need to have Docker installed on your machine.

Testcontainers didn't have a clean way to start the compose file before all the tests.
So it's required to start the compose file manually before running the tests.
Each test will clear the database tables.

Each Integration test has to extend the `IntegrationTest` class.

```shell
docker compose up -d
./gradlew integrationTest
docker compose down
```
