package hu.bsstudio.bssweb.video.repository

import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import hu.bsstudio.bssweb.videocrew.entity.DetailedVideoCrewEntity
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.longs.shouldBeZero
import io.kotest.matchers.optional.shouldBeEmpty
import io.kotest.matchers.optional.shouldBePresent
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import java.time.LocalDate
import java.util.UUID

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DetailedVideoRepositoryTest(
    @Autowired private val underTest: DetailedVideoRepository,
    @Autowired private val entityManager: TestEntityManager
) {
    @Test
    internal fun `create read delete`() {
        underTest.count().shouldBeZero()

        val entity = DetailedVideoEntity(url = URL, title = TITLE)
        val id = underTest.save(entity).id
        // entityManager.run { flush(); clear() }

        val expected = createExpected(id)
        underTest.findById(id) shouldBePresent { it shouldBeEqualToComparingFields expected }

        underTest.deleteById(id)
        entityManager.flush()

        underTest.findById(id).shouldBeEmpty()
    }

    private fun createExpected(id: UUID, videoCrew: List<DetailedVideoCrewEntity> = emptyList()) =
        DetailedVideoEntity(
            url = URL,
            title = TITLE,
            description = "",
            uploadedAt = LocalDate.now(),
            visible = false
        ).apply {
            this.id = id
            this.videoCrew = videoCrew
        }

    private companion object {
        private const val URL = "szobakommando"
        private const val TITLE = "Szobakommando"
    }
}
