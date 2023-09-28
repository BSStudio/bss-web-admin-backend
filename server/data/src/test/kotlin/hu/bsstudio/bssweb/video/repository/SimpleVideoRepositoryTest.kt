package hu.bsstudio.bssweb.video.repository

import hu.bsstudio.bssweb.BssDataConfiguration
import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import java.time.LocalDate

@DataJpaTest
@ContextConfiguration(classes = [BssDataConfiguration::class])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(
    properties = [
        "spring.datasource.url=jdbc:tc:postgresql:15.4-alpine:///databasename"
    ]
)
class SimpleVideoRepositoryTest(
    @Autowired private val underTest: SimpleVideoRepository
) {

    @Test
    fun `create read delete`() {
        assertThat(underTest.count()).isZero

        val entity = SimpleVideoEntity(url = URL, title = TITLE)
        val id = underTest.save(entity).id

        assertThat(underTest.findById(id))
            .isPresent
            .get()
            .usingRecursiveComparison()
            .isEqualTo(
                SimpleVideoEntity(
                    url = URL,
                    title = TITLE,
                    uploadedAt = LocalDate.now(),
                    visible = false
                ).apply {
                    this.id = id
                }
            )

        underTest.deleteById(id)
        assertThat(underTest.findById(id)).isEmpty()
    }

    private companion object {
        private const val URL = "szobakommando"
        private const val TITLE = "Szobakommando"
    }
}
