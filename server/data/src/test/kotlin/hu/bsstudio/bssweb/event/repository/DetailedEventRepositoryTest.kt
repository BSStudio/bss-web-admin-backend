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

@DataJpaTest
@ContextConfiguration(
    classes = [
        DetailedEventRepositoryTest::class,
        DetailedEventRepository::class
    ]
)
@EnableAutoConfiguration
@EntityScan(basePackages = ["hu.bsstudio.bssweb"])
class DetailedEventRepositoryTest(
    @Autowired private val underTest: DetailedEventRepository
) {

    @Test
    fun `test create read update and delete`() {
        val (id) = underTest.save(ENTITY)
        val foundEntity = underTest.findById(id)

        assertThat(foundEntity).hasValue(ENTITY)

        underTest.deleteById(id)
        val count = underTest.count()
        assertThat(count).isZero()
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
