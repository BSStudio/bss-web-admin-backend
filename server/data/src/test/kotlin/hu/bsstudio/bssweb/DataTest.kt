package hu.bsstudio.bssweb

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration

@DataJpaTest(properties = ["spring.datasource.url=jdbc:tc:postgresql:16.0-alpine3.18:///db"])
@ContextConfiguration(classes = [DataModuleConfiguration::class])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DataTest
