# Data module

This module holds all the interfaces
for every repository and their respective entities.

This module also holds the database migrations.

## Flyway migrations
The project uses [Flyway][flyway] for database migrations.
If you need to make any changes to the database schema,
you should create a new migration file in the
[`src/main/resources/db/migration`][migrations-folder]
directory.

## Testing
The tests in this module will set up a postgres database
with the help of [Testcontainers][testcontainers].

You can set the postgres container version in the
[`src/test/resources/application.yml`][test-properties]
file.


### Testcontainers


[flyway]: https://flywaydb.org/
[migrations-folder]: src/main/resources/db/migration
[testcontainers]: https://www.testcontainers.org/quickstart/junit_5/
[test-properties]: src/test/resources/application.yml

