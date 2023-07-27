package hu.bsstudio.bssweb.video.repository

import hu.bsstudio.bssweb.DataConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource

@DataJpaTest
@ContextConfiguration(classes = [DataConfiguration::class])
@TestPropertySource(properties = ["spring.flyway.locations=classpath:db/migration/{vendor}"])
class DetailedVideoRepositoryTest(
    @Autowired private val underTest: DetailedVideoRepository
)
