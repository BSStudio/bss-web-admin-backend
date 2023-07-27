package hu.bsstudio.bssweb.video.repository

import hu.bsstudio.bssweb.DataConfiguration
import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
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
class SimpleVideoRepositoryTest(
    @Autowired private val underTest: SimpleVideoRepository
) {

    @Test
    fun `create read delete`() {
        assertThat(this.underTest.count()).isZero

        val entity = SimpleVideoEntity(url = URL, title = TITLE)
        this.underTest.save(entity)

        val id = entity.id
        val expected = SimpleVideoEntity(
            url = URL,
            title = TITLE,
            uploadedAt = LocalDate.now(),
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
        private const val URL = "szobakommando"
        private const val TITLE = "Szobakommando"
    }
}
