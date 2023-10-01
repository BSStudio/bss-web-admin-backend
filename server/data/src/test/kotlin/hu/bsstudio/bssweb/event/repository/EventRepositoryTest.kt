package hu.bsstudio.bssweb.event.repository

import hu.bsstudio.bssweb.DataTest
import hu.bsstudio.bssweb.event.entity.SimpleEventEntity
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.longs.shouldBeZero
import io.kotest.matchers.optional.shouldBeEmpty
import io.kotest.matchers.optional.shouldBePresent
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

class EventRepositoryTest(
    @Autowired private val underTest: SimpleEventRepository
) : DataTest() {

    @Test
    fun `create read delete`() {
        underTest.count().shouldBeZero()

        val id = underTest.save(SimpleEventEntity(url = URL, title = TITLE)).id

        underTest.findById(id)
            .shouldBePresent()
            .shouldBeEqualToComparingFields(
                SimpleEventEntity(
                    url = URL,
                    title = TITLE,
                    description = "",
                    date = LocalDate.now(),
                    visible = false
                ).apply {
                    this.id = id
                }
            )

        underTest.deleteById(id)
        underTest.findById(id).shouldBeEmpty()
    }

    private companion object {
        private const val URL = "simonyi-konferencia"
        private const val TITLE = "Simonyi Konferencia"
    }
}
