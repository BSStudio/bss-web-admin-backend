# Web module

This module holds all the web related code.
Mainly controllers and the authorisation logic for the app.

## Open API
The resources folder contains the [open-api.yaml][open-api-yaml] file.
This is hosted by the server using [Springdoc openapi][springdoc] library.
Read the docs to see how to disable/customise the open api endpoint.

## Security
The server currently uses basic auth for the endpoints.
This will be replaced in the future.

For now you can change the default user and password in the `application.yml` file.
```yaml
spring:
  security:
    user:
      # name: "user" # it's user by default if not specified
      password: "password"
```

[springdoc]: https://springdoc.org/
[open-api-yaml]: server/web/src/main/resources/static/open-api.yaml
