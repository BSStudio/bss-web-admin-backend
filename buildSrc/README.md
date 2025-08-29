# The build config
This module stores the build configuration for the application.

There are conventions configured for most use cases.


## Java conventions
The Java conventions will set the language level and set up the annotation processor.

## Java Library conventions
Enables the Gradle `java-library` plugin, sets the language level, configures the annotation processor, and enforces API/implementation separation for published libraries.
## Kotlin conventions
The Kotlin conventions will set up the Kotlin compiler to be compatible with Spring.
It's based on [Spring Initializr][spring-initializr].

## Dependency management
Will set up the BOM and the repositories for the project.

## Integration testing conventions
Will set up the integration testing configuration.
It will create a new scope: `integrationTest`.
It's based on [Gradle's documentation to set up integration tests][gradle-integration].

## Spotless conventions
Will set up the code formatter.

## Testing conventions
Will set up the testing configuration using Gradle's **TestingExtension** for modern test suite management.
It will configure JUnit Jupiter and set up test dependencies including:
- Spring Boot Starter Test
- MockK for Kotlin mocking
- SpringMockK for Spring integration testing
- Kotest assertions

The testing configuration now leverages Gradle's built-in test suite capabilities for better organization and dependency management.

## Spring app conventions
Will set up the Spring Boot plugin and basic Spring Boot starter dependency.
This convention has been simplified and no longer includes aggregated reports (these are now configured directly in the main `server/build.gradle.kts`).

## Jacoco conventions
Provides Jacoco test coverage configuration. Set coverage to 100% and enforce the coverage.
There are a few exceptions to the coverage enforcement.
The coverage is enforced by the `check` lifecycle.
There's always coverage reports generated after the tests are run.

[spring-initializr]: https://start.spring.io/
[gradle-integration]: https://docs.gradle.org/current/userguide/java_testing.html#sec:configuring_java_integration_tests
