package hu.bsstudio.bssweb.event.repository

import hu.bsstudio.bssweb.DataConfiguration
import hu.bsstudio.bssweb.event.entity.SimpleEventEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import java.time.LocalDate

@DataJpaTest
@ContextConfiguration(classes = [DataConfiguration::class])
@TestPropertySource(properties = ["spring.flyway.locations=classpath:db/migration/{vendor}"])
class EventRepositoryTest(
    @Autowired private val underTest: SimpleEventRepository
) {

    @Test
    fun `create read delete`() {
        assertThat(this.underTest.count()).isZero

        val entity = SimpleEventEntity(url = URL, title = TITLE)
        this.underTest.save(entity)

        val id = entity.id
        val expected = SimpleEventEntity(
            url = URL,
            title = TITLE,
            description = "",
            date = LocalDate.now(),
            visible = false
        ).apply {
            this.id = id
        }
        assertThat(this.underTest.findById(id))
            .isPresent
            .get()
            .usingRecursiveComparison()
            .isEqualTo(expected)

        this.underTest.deleteById(id)
        assertThat(this.underTest.findById(id)).isEmpty
    }

    private companion object {
        private const val URL = "simonyi-konferencia"
        private const val TITLE = "Simonyi Konferencia"
    }
}
