package hu.bsstudio.bssweb.event.repository

import hu.bsstudio.bssweb.event.entity.SimpleEventEntity
import io.kotest.matchers.date.shouldBeCloseTo
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.longs.shouldBeZero
import io.kotest.matchers.optional.shouldBeEmpty
import io.kotest.matchers.optional.shouldBePresent
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import java.time.Instant
import java.time.LocalDate
import kotlin.time.Duration.Companion.minutes

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EventRepositoryTest(
    @Autowired private val underTest: SimpleEventRepository,
    @Autowired private val entityManager: TestEntityManager,
) {
    @Test
    fun `create read delete`() {
        underTest.count().shouldBeZero()

        val id = underTest.save(SimpleEventEntity(url = URL, title = TITLE)).id

        entityManager.flush()

        val actual = underTest.findById(id)

        val expected =
            SimpleEventEntity(
                url = URL,
                title = TITLE,
                description = "",
                dateFrom = LocalDate.now(),
                dateTo = LocalDate.now(),
                visible = false,
            ).apply {
                this.id = id
                this.createdAt = Instant.now()
                this.updatedAt = Instant.now()
            }
        actual shouldBePresent {
            it.shouldBeEqualToIgnoringFields(expected, ::createdAt, ::updatedAt)
            it.createdAt.shouldBeCloseTo(expected.createdAt, duration = 1.minutes)
            it.updatedAt.shouldBeCloseTo(expected.updatedAt, duration = 1.minutes)
        }

        underTest.deleteById(id)
        underTest.findById(id).shouldBeEmpty()
    }

    private companion object {
        private const val URL = "simonyi-konferencia"
        private const val TITLE = "Simonyi Konferencia"
    }
}
