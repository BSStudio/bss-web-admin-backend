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
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File

@Testcontainers
@SpringJUnitConfig(classes = [BssFeignConfig::class, DataConfig::class])
open class IntegrationTest {
    companion object {
        @Container
        @JvmStatic
        val compose: DockerComposeContainer<*> =
            DockerComposeContainer(File("../docker-compose.yml"), File("../docker-compose.ci.yml"))
                .withExposedService("postgres", 5432)
                .withExposedService("app", 8080)
                .withOptions("--profile app")
                .withBuild(true)

        @DynamicPropertySource
        @JvmStatic
        fun configureProperties(registry: DynamicPropertyRegistry) {
            val postgresHost = compose.getServiceHost("postgres", 5432)
            val postgresPort = compose.getServicePort("postgres", 5432)
            val appHost = compose.getServiceHost("app", 8080)
            val appPort = compose.getServicePort("app", 8080)

            registry.add("spring.datasource.url") {
                "jdbc:postgresql://$postgresHost:$postgresPort/bss?currentSchema=private"
            }
            registry.add("spring.datasource.username") { "user" }
            registry.add("spring.datasource.password") { "password" }
            registry.add("spring.flyway.enabled") { "false" }
            registry.add("bss.client.url") { "http://$appHost:$appPort" }
        }
    }

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
