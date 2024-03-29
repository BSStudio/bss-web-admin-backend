package hu.bsstudio.bssweb

import hu.bsstudio.bssweb.config.BssFeignConfig
import hu.bsstudio.bssweb.event.repository.DetailedEventRepository
import hu.bsstudio.bssweb.eventvideo.repository.EventVideoRepository
import hu.bsstudio.bssweb.label.repository.LabelRepository
import hu.bsstudio.bssweb.member.repository.MemberRepository
import hu.bsstudio.bssweb.video.repository.DetailedVideoRepository
import hu.bsstudio.bssweb.videocrew.repository.VideoCrewRepository
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(classes = [BssFeignConfig::class, DataConfig::class])
@TestPropertySource(
    properties = [
        "bss.client.url=http://localhost:8080",
        "bss.client.username=user",
        "bss.client.password=password",
        "spring.flyway.enabled=false",
        "spring.datasource.url=jdbc:postgresql://localhost:5432/bss?currentSchema=private",
        "spring.datasource.username=user",
        "spring.datasource.password=password",
        "spring.flyway.default-schema=private",
        "spring.flyway.schemas=private,public",
    ],
)
open class IntegrationTest {
    @Autowired protected lateinit var eventRepository: DetailedEventRepository

    @Autowired protected lateinit var videoRepository: DetailedVideoRepository

    @Autowired protected lateinit var memberRepository: MemberRepository

    @Autowired protected lateinit var eventVideoRepository: EventVideoRepository

    @Autowired protected lateinit var videoCrewRepository: VideoCrewRepository

    @Autowired protected lateinit var labelRepository: LabelRepository

    @BeforeEach
    fun setUp() {
        this.labelRepository.deleteAll()
        this.videoCrewRepository.deleteAll()
        this.eventVideoRepository.deleteAll()
        this.memberRepository.deleteAll()
        this.videoRepository.deleteAll()
        this.eventRepository.deleteAll()
    }
}
