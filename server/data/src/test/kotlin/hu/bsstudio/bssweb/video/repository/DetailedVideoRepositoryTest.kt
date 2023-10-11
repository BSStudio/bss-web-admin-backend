package hu.bsstudio.bssweb.video.repository

import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import hu.bsstudio.bssweb.member.repository.MemberRepository
import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
import hu.bsstudio.bssweb.videocrew.entity.DetailedVideoCrewEntity
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntityId
import hu.bsstudio.bssweb.videocrew.repository.VideoCrewRepository
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.longs.shouldBeZero
import io.kotest.matchers.optional.shouldBeEmpty
import io.kotest.matchers.optional.shouldBePresent
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.testcontainers.shaded.org.bouncycastle.asn1.x500.style.RFC4519Style.member
import java.time.LocalDate
import java.util.UUID

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DetailedVideoRepositoryTest(
    @Autowired private val underTest: DetailedVideoRepository,
    @Autowired private val memberRepository: MemberRepository,
    @Autowired private val videoCrewRepository: VideoCrewRepository,
    @Autowired private val entityManager: TestEntityManager
) {
    @Test
    internal fun `create read delete`() {
        underTest.count().shouldBeZero()

        val entity = DetailedVideoEntity(url = URL, title = TITLE)
        val id = underTest.save(entity).id
        entityManager.run { flush(); clear() }

        val expected = createExpected(id)
        underTest.findById(id) shouldBePresent { it shouldBeEqualToComparingFields expected }

        underTest.deleteById(id)
        entityManager.flush()

        underTest.findById(id).shouldBeEmpty()
    }

    @Test
    internal fun `create read delete with crew`() {
        val member = DetailedMemberEntity(name = MEMBER_NAME, url = MEMBER_URL, nickname = MEMBER_NICKNAME)
            .let { this.entityManager.persist(it) }
        val videoId = SimpleVideoEntity(url = URL, title = TITLE)
            .let { this.entityManager.persistAndGetId(it, UUID::class.java) }
        this.entityManager.persist(VideoCrewEntity(VideoCrewEntityId(videoId, "cameraman", member.id)))
        this.entityManager.flush()

        val video = underTest.findById(videoId).shouldBePresent()

        underTest.deleteById(video.id)
        // todo entityManager.flush()

        underTest.findById(videoId).shouldBeEmpty()
        memberRepository.findById(member.id).shouldBePresent()
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
