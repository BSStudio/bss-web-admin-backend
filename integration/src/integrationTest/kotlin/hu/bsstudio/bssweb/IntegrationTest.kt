package hu.bsstudio.bssweb

import hu.bsstudio.bssweb.config.BssWebAdminBackendClientConfig
import hu.bsstudio.bssweb.event.repository.EventRepository
import hu.bsstudio.bssweb.eventvideo.repository.EventVideoRepository
import hu.bsstudio.bssweb.label.repository.LabelRepository
import hu.bsstudio.bssweb.member.repository.MemberRepository
import hu.bsstudio.bssweb.video.repository.VideoRepository
import hu.bsstudio.bssweb.videocrew.repository.VideoCrewRepository
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(classes = [BssWebAdminBackendClientConfig::class, DataConfig::class])
@TestPropertySource(
    properties = [
        "bss.client.url=http://localhost:8080",
        "bss.client.token=token",
        "spring.flyway.enabled=false",
        "spring.datasource.url=jdbc:postgresql://localhost:5432/bss?currentSchema=private",
        "spring.datasource.username=user",
        "spring.datasource.password=password",
    ],
)
open class IntegrationTest {
    @Autowired protected lateinit var eventRepository: EventRepository

    @Autowired protected lateinit var videoRepository: VideoRepository

    @Autowired protected lateinit var memberRepository: MemberRepository

    @Autowired protected lateinit var eventVideoRepository: EventVideoRepository

    @Autowired protected lateinit var videoCrewRepository: VideoCrewRepository

    @Autowired protected lateinit var labelRepository: LabelRepository

    @Autowired private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun setUp() {
        // not deleteAll(): it only marks videos, events and members deleted, so their rows and history would carry over
        jdbcTemplate.execute(
            "truncate crew, event_video, video_label, video_url, label, video, event, member, " +
                "video_aud, video_url_aud, video_label_aud, event_aud, member_aud, revinfo",
        )
    }
}
