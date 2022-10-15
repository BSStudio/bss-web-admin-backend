package hu.bsstudio.bssweb.metrics.config

import hu.bsstudio.bssweb.event.repository.EventRepository
import hu.bsstudio.bssweb.member.repository.MemberRepository
import hu.bsstudio.bssweb.metrics.service.DefaultMetricsService
import hu.bsstudio.bssweb.video.repository.VideoRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MetricsServiceConfig(
    private val videoRepository: VideoRepository,
    private val eventRepository: EventRepository,
    private val memberRepository: MemberRepository
) {

    @Bean
    fun metricsService() = DefaultMetricsService(videoRepository, eventRepository, memberRepository)
}
