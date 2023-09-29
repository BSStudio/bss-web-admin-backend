package hu.bsstudio.bssweb

import hu.bsstudio.bssweb.config.BssDataConfig
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration

@DataJpaTest(properties = ["spring.datasource.url=jdbc:tc:postgresql:16.0-alpine3.18:///db"])
@ContextConfiguration(classes = [BssDataConfig::class])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DataTest
