package hu.bsstudio.bssweb.videocrew.repository

import hu.bsstudio.bssweb.find
import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import hu.bsstudio.bssweb.persistAndGetId
import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntityId
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.optional.shouldBeEmpty
import io.kotest.matchers.optional.shouldBePresent
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import java.util.UUID

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VideoCrewRepositoryTest(
    @Autowired private val underTest: VideoCrewRepository,
    @Autowired private val entityManager: TestEntityManager
) {

    @Test
    fun `create read delete`() {
        val memberId = entityManager.persistAndGetId<UUID>(DetailedMemberEntity(url = "url", name = "name"))
        val videoId = entityManager.persistAndGetId<UUID>(SimpleVideoEntity(url = "url", title = "title"))

        val entity = VideoCrewEntity(VideoCrewEntityId(videoId, "cameraMan", memberId))
        val id = VideoCrewEntityId(videoId, "cameraMan", memberId)
        underTest.save(entity)
        underTest.findById(id) shouldBePresent { it shouldBeEqualToComparingFields entity }

        underTest.deleteById(id)
        entityManager.flush()

        underTest.findById(id).shouldBeEmpty()
        entityManager.find<DetailedMemberEntity>(memberId).shouldNotBeNull()
        entityManager.find<SimpleVideoEntity>(videoId).shouldNotBeNull()
    }
}
