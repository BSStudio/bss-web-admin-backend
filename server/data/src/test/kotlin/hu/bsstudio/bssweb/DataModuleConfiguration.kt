package hu.bsstudio.bssweb

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa

@EnableAutoConfiguration
@AutoConfigureDataJpa
class DataModuleConfiguration
// this class is used to autoconfigure the data layer for testing
// it's defined in the test scope as the main class will be configured by the main Spring Boot class
