# Service module

This module holds all the business logic related code.

Avoid adding any web or data related code here.

The aim is to keep this module as reusable and clean as possible.

Use POJOs for the logic and keep Spring annotations in the `Config` classes.

## Testing
Since all test classes are POJOs, there's no need to set up a Spring environment for the tests.