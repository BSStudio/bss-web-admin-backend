package hu.bsstudio.bssweb.metrics.config

import hu.bsstudio.bssweb.event.repository.SimpleEventRepository
import hu.bsstudio.bssweb.member.repository.MemberRepository
import hu.bsstudio.bssweb.metrics.service.DefaultMetricsService
import hu.bsstudio.bssweb.video.repository.SimpleVideoRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MetricsServiceConfig(
    private val videoRepository: SimpleVideoRepository,
    private val eventRepository: SimpleEventRepository,
    private val memberRepository: MemberRepository,
) {
    @Bean
    fun metricsService() = DefaultMetricsService(videoRepository, eventRepository, memberRepository)
}
