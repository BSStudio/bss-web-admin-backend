package hu.bsstudio.bssweb.event.repository

import hu.bsstudio.bssweb.event.entity.DetailedEventEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDate
import java.util.UUID

@EnableAutoConfiguration
@EntityScan(basePackages = ["hu.bsstudio.bssweb"])
@DataJpaTest
@ContextConfiguration(classes = [DetailedEventRepositoryTest::class])
class DetailedEventRepositoryTest(
    @Autowired private val underTest: DetailedEventRepository
) {

    @Test
    fun `test create read and delete`() {
        assertThat(underTest.count()).isZero()
        val (id) = underTest.save(ENTITY)
        assertThat(underTest.findById(id)).hasValue(ENTITY)
        assertThat(underTest.count()).isOne()

        underTest.deleteById(id)
        assertThat(underTest.count()).isZero()
    }

    companion object {
        private val ENTITY = DetailedEventEntity(
            id = UUID.randomUUID(),
            url = "url",
            title = "title",
            description = "description",
            date = LocalDate.now(),
            visible = true,
            videos = listOf()
        )
    }
}
