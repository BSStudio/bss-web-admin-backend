# The client module

This module stores exposes some interfaces to easily interact with the backend services.
It uses Spring Cloud OpenFeign to make the requests.

The `BssFeignConfig` class will expose beans for each controller in the backend services.

There are some properties to control authorisation.
Refer to the [additional-spring-configuration-metadata.json][metadata-json]


[metadata-json]: src/main/resources/META-INF/additional-spring-configuration-metadata.json