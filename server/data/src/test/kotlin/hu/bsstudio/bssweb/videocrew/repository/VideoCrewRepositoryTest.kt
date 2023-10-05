package hu.bsstudio.bssweb.videocrew.repository

import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import hu.bsstudio.bssweb.member.repository.MemberRepository
import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
import hu.bsstudio.bssweb.video.repository.SimpleVideoRepository
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntityId
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.optional.shouldBeEmpty
import io.kotest.matchers.optional.shouldBePresent
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VideoCrewRepositoryTest(
    @Autowired private val underTest: VideoCrewRepository,
    @Autowired private val memberRepository: MemberRepository,
    @Autowired private val videoRepository: SimpleVideoRepository
) {

    @Test
    fun `create read delete`() {
        val memberId = memberRepository.save(DetailedMemberEntity(url = "url", name = "name")).id
        val videoId = videoRepository.save(SimpleVideoEntity(url = "url", title = "title")).id

        val id = VideoCrewEntityId(videoId, "cameraMan", memberId)
        val entity = VideoCrewEntity(id)
        underTest.save(entity)
        underTest.findById(id) shouldBePresent { it shouldBeEqualToComparingFields entity }

        underTest.deleteById(id)
        underTest.findById(id).shouldBeEmpty()
        memberRepository.count().shouldBe(1L)
        videoRepository.count().shouldBe(1L)
    }
}
