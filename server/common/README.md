# Common module

This module was created to contain all code that can be imported from any part of the project.

Primarily the MemberStatus enum is contained within this module.
The intention was to avoid exposing the data layer in the module layer,
while still allowing the MemberStatus to be used for validation at both layers.

> ⚠️ Note:   
> avoid bloating this module with code that is not used by the majority of the project.
> Usually there's a better place to put it.
> Always refer to the [project structure][project structure].

[project structure]: ../../README.md#project-structure

 