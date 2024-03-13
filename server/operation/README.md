# Operation module
This module holds all the interfaces for every controller and their respective clients.

This module will require the developers
to keep their controllers and clients consistent with each other,
as the clients will be generated from the controllers.

## Caveats
Unfortunately Spring Cloud OpenFeign does not support class level `@RequestMapping` annotations.  
So paths must be defined in the method level. [see][1]

Default values for `@RequestParam` are not supported either.

[1]: https://github.com/spring-cloud/spring-cloud-openfeign/issues/547
