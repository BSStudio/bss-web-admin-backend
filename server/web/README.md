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

You can change the default user and password using the following properties:

```yaml
spring:
  security:
    user:
      # name: "user" # it's user by default if not specified
      password: "password"
```

## Testing

The `ControllerTestConfig` class creates a Spring environment for the tests in this module.
The `application.yml` in the `test/resources` folder will set the default user password for the controller tests.

[springdoc]: https://springdoc.org/
[open-api-yaml]: server/web/src/main/resources/static/open-api.yaml

