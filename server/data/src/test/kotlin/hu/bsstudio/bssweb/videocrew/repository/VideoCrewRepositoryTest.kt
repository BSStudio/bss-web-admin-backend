package hu.bsstudio.bssweb.videocrew.repository

import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import hu.bsstudio.bssweb.member.repository.MemberRepository
import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
import hu.bsstudio.bssweb.video.repository.SimpleVideoRepository
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntityId
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
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import java.util.UUID

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VideoCrewRepositoryTest(
    @Autowired private val underTest: VideoCrewRepository,
    @Autowired private val entityManager: TestEntityManager
) {

    @Test
    fun `create read delete`() {
        val memberId = entityManager.persistAndGetId(DetailedMemberEntity(url = "url", name = "name"), UUID::class.java)
        val videoId = entityManager.persistAndGetId(SimpleVideoEntity(url = "url", title = "title"), UUID::class.java)

        val entity = VideoCrewEntity(VideoCrewEntityId(videoId, "cameraMan", memberId))
        val id = VideoCrewEntityId(videoId, "cameraMan", memberId)
        underTest.save(entity)
        underTest.findById(id) shouldBePresent { it shouldBeEqualToComparingFields entity }

        underTest.deleteById(id)
        entityManager.flush()

        underTest.findById(id).shouldBeEmpty()
        entityManager.find(DetailedMemberEntity::class.java, memberId).shouldNotBeNull()
        entityManager.find(SimpleVideoEntity::class.java, videoId).shouldNotBeNull()
    }
}
