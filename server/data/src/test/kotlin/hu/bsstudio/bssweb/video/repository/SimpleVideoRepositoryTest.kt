package hu.bsstudio.bssweb.video.repository

import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.longs.shouldBeZero
import io.kotest.matchers.optional.shouldBeEmpty
import io.kotest.matchers.optional.shouldBePresent
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.time.Instant
import java.time.LocalDate

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SimpleVideoRepositoryTest(
    @Autowired private val underTest: SimpleVideoRepository,
) {
    @Test
    fun `create read delete`() {
        underTest.count().shouldBeZero()

        val entity = SimpleVideoEntity(url = URL, title = TITLE)
        val id = underTest.save(entity).id

        underTest.findById(id) shouldBePresent {
            it shouldBeEqualToComparingFields
                SimpleVideoEntity(
                    url = URL,
                    title = TITLE,
                    uploadedAt = LocalDate.now(),
                    visible = false,
                ).apply {
                    this.id = id
                    this.createdAt = Instant.now()
                    this.updatedAt = Instant.now()
                }
        }

        underTest.deleteById(id)
        underTest.findById(id).shouldBeEmpty()
    }

    private companion object {
        private const val URL = "szobakommando"
        private const val TITLE = "Szobakommando"
    }
}
