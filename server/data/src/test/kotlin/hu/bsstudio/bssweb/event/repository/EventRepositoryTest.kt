package hu.bsstudio.bssweb.event.repository

import hu.bsstudio.bssweb.DataTest
import hu.bsstudio.bssweb.event.entity.SimpleEventEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate

class EventRepositoryTest(
    @Autowired private val underTest: SimpleEventRepository
) : DataTest() {

    @Test
    fun `create read delete`() {
        assertThat(underTest.count()).isZero

        val id = underTest.save(SimpleEventEntity(url = URL, title = TITLE)).id

        assertThat(underTest.findById(id))
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(
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
        assertThat(underTest.findById(id)).isEmpty()
    }

    private companion object {
        private const val URL = "simonyi-konferencia"
        private const val TITLE = "Simonyi Konferencia"
    }
}
