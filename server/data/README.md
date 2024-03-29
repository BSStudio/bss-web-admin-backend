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

### Schemas
The project uses the `private` schema for all the tables.
The `public` schema is used for rendering views for the [GraphQL backend][graphql-backend].

## Testing

The tests in this module will set up a Postgres database
with the help of [Testcontainers][testcontainers].

This means that you need docker running on your machine when running these tests

You can skip these test during your local build using `-x :server:data:test`

```shell
# run all tests except :server:data
./gradlew -x :server:data:test test
```

Since the project is so well-structured,
there's no need to apply labels on these tests,
the module can be excluded from the build process.

You can set the postgres container version in the
[`src/test/resources/application.yml`][test-properties]
file.

## ER diagram

```mermaid
erDiagram


crew {
   uuid video_id FK
   uuid member_id FK
   varchar(250) position
}

event {
   uuid id PK
   varchar(250) url
   varchar(250) title
   varchar(8192) description
   date date_from
   boolean visible
   timestamp created_at
   timestamp updated_at
   date date_to
}

event_video {
   uuid event_id FK
   uuid video_id FK
}

label {
   uuid id PK
   varchar(64) name
   varchar(256) description
}

member {
   uuid id PK
   varchar(250) url
   varchar(250) name
   varchar(250) nickname
   varchar(2000) description
   date joined_at
   varchar(250) role
   varchar(250) status
   boolean archived
   timestamp created_at
   timestamp updated_at
}

status {
   varchar(250) name PK
}

video {
   uuid id PK
   varchar(250) title
   varchar(2000) description
   date shooting_date_start
   boolean visible
   timestamp created_at
   timestamp updated_at
   date shooting_date_end
}

video_label {
   uuid label_id FK
   uuid video_id FK
}

video_url {
   uuid video_id FK
   varchar(255) url
}

member ||--|| status: has
crew ||--|{ member: has
crew ||--|{ video: has
event_video ||--|{ event: has
event_video ||--|{ video: has
video_label ||--|{ video: has
video_label ||--|{ label: has
video_url ||--|{ video: has
``` 

[flyway]: https://flywaydb.org/

[migrations-folder]: src/main/resources/db/migration

[graphql-backend]: https://github.com/BSStudio/bss-web-graphql-backend

[testcontainers]: https://www.testcontainers.org/quickstart/junit_5/

[test-properties]: src/test/resources/application.yml
