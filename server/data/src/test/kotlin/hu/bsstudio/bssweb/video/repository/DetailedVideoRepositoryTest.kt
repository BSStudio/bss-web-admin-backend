package hu.bsstudio.bssweb.video.repository

import hu.bsstudio.bssweb.DataConfiguration
import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import hu.bsstudio.bssweb.member.entity.SimpleMemberEntity
import hu.bsstudio.bssweb.member.repository.MemberRepository
import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
import hu.bsstudio.bssweb.videocrew.entity.DetailedVideoCrewEntity
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntityId
import hu.bsstudio.bssweb.videocrew.repository.VideoCrewRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import java.time.LocalDate
import java.util.UUID

@DataJpaTest
@ContextConfiguration(classes = [DataConfiguration::class])
@TestPropertySource(properties = ["spring.flyway.locations=classpath:db/migration/{vendor}"])
class DetailedVideoRepositoryTest(
    @Autowired private val underTest: DetailedVideoRepository,
    @Autowired private val memberRepository: MemberRepository,
    @Autowired private val simpleVideoRepository: SimpleVideoRepository,
    @Autowired private val videoCrewRepository: VideoCrewRepository,
    @Autowired private val entityManager: TestEntityManager
) {
    @Test
    internal fun `create read delete`() {
        assertThat(underTest.count()).isZero

        val entity = DetailedVideoEntity(url = URL, title = TITLE)
        val id = underTest.save(entity).id

        val expected = createExpected(id)
        assertThat(underTest.findById(id))
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(expected)

        underTest.deleteById(id)
        assertThat(underTest.findById(id)).isEmpty()
    }

    @Test
    internal fun `create read delete with crew`() {
        val memberId = DetailedMemberEntity(name = MEMBER_NAME, url = MEMBER_URL, nickname = MEMBER_NICKNAME)
            .let { this.memberRepository.save(it) }
            .id
        val videoId = SimpleVideoEntity(url = URL, title = TITLE)
            .let { this.simpleVideoRepository.save(it) }
            .id
        val videoCrewId = VideoCrewEntityId(videoId, "cameraman", memberId)
        this.videoCrewRepository.save(VideoCrewEntity(videoCrewId))
        entityManager.run { flush(); clear() }

        val expected = createExpected(videoId, listOf(DetailedVideoCrewEntity(videoCrewId, SimpleMemberEntity(MEMBER_NAME, MEMBER_NICKNAME).apply { id = memberId })))
        assertThat(underTest.findById(videoId))
            .isPresent()
            .get()
            .usingRecursiveComparison()
            .isEqualTo(expected)
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
        private const val MEMBER_NAME = "Bence Csik"
        private const val MEMBER_URL = "bcsik"
        private const val MEMBER_NICKNAME = "CséBé"
    }
}
