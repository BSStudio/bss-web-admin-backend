# Model module

This module holds all the public classes that are used to represent the data of the application.

This project uses json serialization to convert the data from the server to the client and vice versa.

## Tests

There are serialization and deserialization unit tests for each class in this module.

The module does not set up Spring autoconfiguration,
so the `ModelTestsConfig` class is used to set up the Spring environment for the tests.
Since this class is only available in the test scope, it won't affect other modules unit tests.

This will make it possible to use the `@JsonTest` annotation in the tests.
