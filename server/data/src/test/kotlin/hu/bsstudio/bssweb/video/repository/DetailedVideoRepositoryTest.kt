package hu.bsstudio.bssweb.video.repository

import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import hu.bsstudio.bssweb.member.entity.SimpleMemberEntity
import hu.bsstudio.bssweb.member.repository.MemberRepository
import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import hu.bsstudio.bssweb.videocrew.entity.DetailedVideoCrewEntity
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntityId
import hu.bsstudio.bssweb.videocrew.repository.VideoCrewRepository
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.date.shouldBeCloseTo
import io.kotest.matchers.equality.shouldBeEqualToIgnoringFields
import io.kotest.matchers.longs.shouldBeZero
import io.kotest.matchers.optional.shouldBeEmpty
import io.kotest.matchers.optional.shouldBePresent
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import java.time.Instant
import java.time.LocalDate
import java.util.UUID
import kotlin.time.Duration.Companion.minutes

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DetailedVideoRepositoryTest(
    @Autowired private val underTest: DetailedVideoRepository,
    @Autowired private val memberRepository: MemberRepository,
    @Autowired private val videoCrewRepository: VideoCrewRepository,
    @Autowired private val entityManager: TestEntityManager,
) {
    @Test
    internal fun `create read delete`() {
        underTest.count().shouldBeZero()

        val entity = DetailedVideoEntity(title = TITLE)
        val id = underTest.save(entity).id
        entityManager.run {
            flush()
            clear()
        }

        val expected = createExpected(id)
        underTest.findById(id) shouldBePresent {
            it.shouldBeEqualToIgnoringFields(expected, ::createdAt, ::updatedAt)
            it.createdAt.shouldBeCloseTo(expected.createdAt, duration = 1.minutes)
            it.updatedAt.shouldBeCloseTo(expected.updatedAt, duration = 1.minutes)
        }

        underTest.deleteById(id)
        underTest.findById(id).shouldBeEmpty()
    }

    @Test
    internal fun `create read delete with crew and category`() {
        val memberId =
            DetailedMemberEntity(name = MEMBER_NAME, url = MEMBER_URL, nickname = MEMBER_NICKNAME)
                .let { this.memberRepository.save(it) }
                .id
        val videoId =
            DetailedVideoEntity(title = TITLE).apply { this.labels = listOf("BSS") }
                .let { this.underTest.save(it) }
                .id
        val videoCrewId = VideoCrewEntityId(videoId, "cameraman", memberId)
        this.videoCrewRepository.save(VideoCrewEntity(videoCrewId))
        entityManager.run {
            flush()
            clear()
        }

        val expected =
            createExpected(
                videoId,
                listOf(
                    DetailedVideoCrewEntity(
                        videoCrewId,
                        SimpleMemberEntity(MEMBER_NAME, MEMBER_NICKNAME).apply {
                            id = memberId
                        },
                    ),
                ),
                listOf("BSS"),
            )
        underTest.findById(videoId) shouldBePresent {
            assertSoftly {
                it.shouldBeEqualToIgnoringFields(expected, ::createdAt, ::updatedAt)
                it.createdAt.shouldBeCloseTo(expected.createdAt, duration = 1.minutes)
                it.updatedAt.shouldBeCloseTo(expected.updatedAt, duration = 1.minutes)
            }
        }
    }

    private fun createExpected(
        id: UUID,
        videoCrew: List<DetailedVideoCrewEntity> = emptyList(),
        categories: List<String> = emptyList(),
    ) = DetailedVideoEntity(
        urls = emptyList(),
        title = TITLE,
        description = "",
        shootingDateStart = LocalDate.now(),
        shootingDateEnd = LocalDate.now(),
        visible = false,
    ).apply {
        this.id = id
        this.videoCrew = videoCrew
        this.labels = categories
        this.createdAt = Instant.now()
        this.updatedAt = Instant.now()
    }

    private companion object {
        private const val TITLE = "Szobakommando"
        private const val MEMBER_NAME = "Bence Csik"
        private const val MEMBER_URL = "bcsik"
        private const val MEMBER_NICKNAME = "CséBé"
    }
}
