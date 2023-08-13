package hu.bsstudio.bssweb.videocrew.repository

import hu.bsstudio.bssweb.DataConfiguration
import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import hu.bsstudio.bssweb.member.repository.MemberRepository
import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
import hu.bsstudio.bssweb.video.repository.SimpleVideoRepository
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntityId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource

@DataJpaTest
@ContextConfiguration(classes = [DataConfiguration::class])
@TestPropertySource(properties = ["spring.flyway.locations=classpath:db/migration/{vendor}"])
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
        assertThat(underTest.findById(id))
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(entity)

        underTest.deleteById(id)
        assertThat(underTest.findById(id)).isEmpty()
        assertThat(memberRepository.count()).isOne()
        assertThat(videoRepository.count()).isOne()
    }
}
