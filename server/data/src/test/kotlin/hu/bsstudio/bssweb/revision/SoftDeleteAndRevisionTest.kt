package hu.bsstudio.bssweb.revision

import hu.bsstudio.bssweb.event.entity.EventEntity
import hu.bsstudio.bssweb.event.repository.EventRepository
import hu.bsstudio.bssweb.eventvideo.entity.EventVideoEntity
import hu.bsstudio.bssweb.eventvideo.repository.EventVideoRepository
import hu.bsstudio.bssweb.label.entity.LabelEntity
import hu.bsstudio.bssweb.label.repository.LabelRepository
import hu.bsstudio.bssweb.member.entity.MemberEntity
import hu.bsstudio.bssweb.member.repository.MemberRepository
import hu.bsstudio.bssweb.video.entity.VideoEntity
import hu.bsstudio.bssweb.video.repository.VideoRepository
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntityId
import hu.bsstudio.bssweb.videocrew.repository.VideoCrewRepository
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.optional.shouldBeEmpty
import io.kotest.matchers.optional.shouldBePresent
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase
import org.springframework.data.domain.Pageable
import org.springframework.data.history.RevisionMetadata.RevisionType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionTemplate
import java.time.Instant
import java.util.UUID

/** Envers writes a revision when the transaction commits, so every step here commits, and the tables are emptied after. */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
internal class SoftDeleteAndRevisionTest(
    @param:Autowired private val videoRepository: VideoRepository,
    @param:Autowired private val eventRepository: EventRepository,
    @param:Autowired private val memberRepository: MemberRepository,
    @param:Autowired private val labelRepository: LabelRepository,
    @param:Autowired private val eventVideoRepository: EventVideoRepository,
    @param:Autowired private val videoCrewRepository: VideoCrewRepository,
    @param:Autowired private val jdbcTemplate: JdbcTemplate,
    @Autowired transactionManager: PlatformTransactionManager,
) {
    private val transaction = TransactionTemplate(transactionManager)

    @AfterEach
    internal fun tearDown() {
        SecurityContextHolder.clearContext()
        jdbcTemplate.execute(
            "truncate crew, event_video, video_label, video_url, label, video, event, member, " +
                "video_aud, video_url_aud, video_label_aud, event_aud, member_aud, revinfo",
        )
    }

    @Test
    internal fun `a deleted video is hidden, keeps its links, and is recorded`() {
        val label = labelRepository.save(LabelEntity(name = "Label", description = "Label"))
        val member = memberRepository.save(MemberEntity(url = "member", name = "Member")).id
        val id = videoRepository.save(VideoEntity(title = TITLE, urls = listOf(URL)).apply { labels = listOf(label) }).id
        videoCrewRepository.save(VideoCrewEntity(VideoCrewEntityId(id, "camera", member)))

        deleteVideo(id)

        videoRepository.findById(id).shouldBeEmpty()
        videoRepository.count() shouldBe 0L
        videoRepository.findAllDeleted(Pageable.unpaged()).content.map { it.id } shouldContainExactly listOf(id)
        count("video_url") shouldBe 1
        count("video_label") shouldBe 1
        count("crew") shouldBe 1
        transaction.execute {
            videoRepository.findLastChangeRevision(id) shouldBePresent {
                it.metadata.revisionType shouldBe RevisionType.UPDATE
                it.entity.deletedAt.shouldNotBeNull()
                it.entity.title shouldBe TITLE
            }
        }
    }

    @Test
    internal fun `a restored video comes back with its links, and is recorded`() {
        val label = labelRepository.save(LabelEntity(name = "Label", description = "Label"))
        val member = memberRepository.save(MemberEntity(url = "member", name = "Member"))
        val id = videoRepository.save(VideoEntity(title = TITLE, urls = listOf(URL)).apply { labels = listOf(label) }).id
        videoCrewRepository.save(VideoCrewEntity(VideoCrewEntityId(id, "camera", member.id)))
        deleteVideo(id)

        transaction.execute { videoRepository.findDeletedById(id).get().deletedAt = null }

        videoRepository.findDeletedById(id).shouldBeEmpty()
        videoRepository.findAllDeleted(Pageable.unpaged()).content.shouldBeEmpty()
        transaction.execute {
            videoRepository.findById(id) shouldBePresent {
                it.urls shouldContainExactly listOf(URL)
                it.labels.map { label -> label.name } shouldContainExactly listOf("Label")
                it.videoCrew.map { crew -> crew.member.id } shouldContainExactly listOf(member.id)
            }
            videoRepository.findRevisions(id).content.map { it.entity.deletedAt == null } shouldContainExactly
                listOf(true, false, true)
        }
    }

    @Test
    internal fun `an earlier revision has the fields, urls and labels as they were`() {
        val label = labelRepository.save(LabelEntity(name = "Label", description = "Label"))
        val id = videoRepository.save(VideoEntity(title = TITLE, urls = listOf(URL)).apply { labels = listOf(label) }).id
        transaction.execute {
            videoRepository.findById(id).get().apply {
                title = "Changed"
                urls = listOf("changed")
                labels = emptyList()
            }
        }

        transaction.execute {
            val (first, last) = videoRepository.findRevisions(id).content
            first.metadata.revisionType shouldBe RevisionType.INSERT
            first.entity.title shouldBe TITLE
            first.entity.urls shouldContainExactly listOf(URL)
            first.entity.labels.map { it.name } shouldContainExactly listOf("Label")
            last.entity.title shouldBe "Changed"
            last.entity.urls shouldContainExactly listOf("changed")
            last.entity.labels.shouldBeEmpty()
        }
    }

    @Test
    internal fun `a revision records who made it`() {
        SecurityContextHolder.getContext().authentication = TestingAuthenticationToken("bcsik", null)
        val signedIn = memberRepository.save(MemberEntity(url = "bcsik", name = "Bence Csik")).id
        SecurityContextHolder.clearContext()
        val anonymous = memberRepository.save(MemberEntity(url = "anonymous", name = "Anonymous")).id

        username(signedIn) shouldBe "bcsik"
        username(anonymous).shouldBeNull()
    }

    @Test
    internal fun `a deleted item frees its title and url`() {
        val video = videoRepository.save(VideoEntity(title = TITLE)).id
        val event = eventRepository.save(EventEntity(url = URL, title = TITLE)).id
        val member = memberRepository.save(MemberEntity(url = URL, name = TITLE)).id
        transaction.execute {
            videoRepository.findById(video).get().deletedAt = Instant.now()
            eventRepository.findById(event).get().deletedAt = Instant.now()
            memberRepository.findById(member).get().deletedAt = Instant.now()
        }

        videoRepository.save(VideoEntity(title = TITLE)).id.shouldNotBeNull()
        eventRepository.save(EventEntity(url = URL, title = TITLE)).id.shouldNotBeNull()
        memberRepository.save(MemberEntity(url = URL, name = TITLE)).id.shouldNotBeNull()
        eventRepository.findAllDeleted(Pageable.unpaged()).content.map { it.id } shouldContainExactly listOf(event)
        memberRepository.findAllDeleted(Pageable.unpaged()).content.map { it.id } shouldContainExactly listOf(member)
        eventRepository.findDeletedById(event).shouldBePresent()
        memberRepository.findDeletedById(member).shouldBePresent()
    }

    @Test
    internal fun `counts, crews and the public views leave deleted items out`() {
        val label = labelRepository.save(LabelEntity(name = "Label", description = "Label"))
        val kept = videoRepository.save(VideoEntity(title = "Kept", visible = true).apply { labels = listOf(label) }).id
        val deleted = videoRepository.save(VideoEntity(title = "Deleted", visible = true).apply { labels = listOf(label) }).id
        val event = eventRepository.save(EventEntity(url = URL, title = TITLE, visible = true)).id
        listOf(kept, deleted).forEach { eventVideoRepository.save(EventVideoEntity(event, it)) }
        val member = memberRepository.save(MemberEntity(url = "member", name = "Member")).id
        val gone = memberRepository.save(MemberEntity(url = "gone", name = "Gone")).id
        listOf(member, gone).forEach { videoCrewRepository.save(VideoCrewEntity(VideoCrewEntityId(kept, "camera", it))) }

        deleteVideo(deleted)
        transaction.execute { memberRepository.findById(gone).get().deletedAt = Instant.now() }

        labelRepository.findAll().single().videoCount shouldBe 1L
        eventRepository.findById(event) shouldBePresent { it.videoCount shouldBe 1L }
        transaction.execute {
            eventRepository
                .findById(event)
                .get()
                .videos
                .map { it.id } shouldContainExactly listOf(kept)
            videoRepository
                .findById(kept)
                .get()
                .videoCrew
                .map { it.member.id } shouldContainExactly listOf(member)
        }
        publicIds("video") shouldContainExactly listOf(kept)
        publicIds("member") shouldContainExactly listOf(member)
        jdbcTemplate.queryForList("select video_id from public.event_video", UUID::class.java) shouldContainExactly listOf(kept)
        jdbcTemplate.queryForList("select member_id from public.crew", UUID::class.java) shouldContainExactly listOf(member)
    }

    private fun deleteVideo(id: UUID) = transaction.execute { videoRepository.findById(id).get().deletedAt = Instant.now() }

    private fun count(table: String) = jdbcTemplate.queryForObject("select count(*) from $table", Int::class.java)

    private fun username(memberId: UUID) =
        jdbcTemplate.queryForObject("select username from revinfo join member_aud using (rev) where id = ?", String::class.java, memberId)

    private fun publicIds(view: String) = jdbcTemplate.queryForList("select id from public.$view", UUID::class.java)

    private companion object {
        private const val TITLE = "Szobakommando"
        private const val URL = "szobakommando"
    }
}
