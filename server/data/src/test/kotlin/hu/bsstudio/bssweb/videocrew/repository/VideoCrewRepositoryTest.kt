package hu.bsstudio.bssweb.videocrew.repository

import hu.bsstudio.bssweb.member.entity.MemberEntity
import hu.bsstudio.bssweb.member.repository.MemberRepository
import hu.bsstudio.bssweb.video.entity.VideoEntity
import hu.bsstudio.bssweb.video.repository.VideoRepository
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntityId
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.optional.shouldBeEmpty
import io.kotest.matchers.optional.shouldBePresent
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
internal class VideoCrewRepositoryTest(
    @param:Autowired private val underTest: VideoCrewRepository,
    @param:Autowired private val memberRepository: MemberRepository,
    @param:Autowired private val videoRepository: VideoRepository,
) {
    @Test
    internal fun `create read delete`() {
        val memberId = memberRepository.save(MemberEntity(url = "url", name = "name")).id
        val videoId = videoRepository.save(VideoEntity(title = "title")).id

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
