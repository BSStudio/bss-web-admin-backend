# Common module

This module was created to contain all code that can be imported from any part of the project.

It was mostly created to contain the `MemberStatus` enum,
which is not only used by the `Member` model class,
but also by the `MemberEntity` classes.

> ⚠️ Note:   
> avoid bloating this module with code that is not used by the majority of the project.
> Usually there's a better place to put it.
> Always refer to the [project structure][project structure].

[project structure]: ../../README.md#project-structure

 