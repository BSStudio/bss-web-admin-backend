package hu.bsstudio.bssweb.eventvideo.repository

import hu.bsstudio.bssweb.event.entity.SimpleEventEntity
import hu.bsstudio.bssweb.event.repository.SimpleEventRepository
import hu.bsstudio.bssweb.eventvideo.entity.EventVideoEntity
import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
import hu.bsstudio.bssweb.video.repository.SimpleVideoRepository
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.optional.shouldBeEmpty
import io.kotest.matchers.optional.shouldBePresent
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import java.util.UUID

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EventVideoRepositoryTest(
    @Autowired private val underTest: EventVideoRepository,
    @Autowired private val entityManager: TestEntityManager
) {

    @Test
    fun `create read delete`() {
        val videoId = entityManager.persistAndGetId(SimpleVideoEntity(url = "url", title = "title"), UUID::class.java)
        val eventId = entityManager.persistAndGetId(SimpleEventEntity(url = "url", title = "title"), UUID::class.java)

        val entity = EventVideoEntity(eventId, videoId)
        underTest.save(entity)

        underTest.findById(entity) shouldBePresent { it shouldBeEqualToComparingFields entity }

        underTest.deleteById(entity)
        underTest.findById(entity).shouldBeEmpty()
        entityManager.find(SimpleVideoEntity::class.java, videoId).shouldNotBeNull()
        entityManager.find(SimpleEventEntity::class.java, eventId).shouldNotBeNull()
    }
}
