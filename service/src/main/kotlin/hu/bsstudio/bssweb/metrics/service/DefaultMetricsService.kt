package hu.bsstudio.bssweb.metrics.service

import hu.bsstudio.bssweb.event.repository.EventRepository
import hu.bsstudio.bssweb.member.repository.MemberRepository
import hu.bsstudio.bssweb.metrics.model.BssMetrics
import hu.bsstudio.bssweb.video.repository.VideoRepository

class DefaultMetricsService(
    private val videoRepository: VideoRepository,
    private val eventRepository: EventRepository,
    private val memberRepository: MemberRepository
) : MetricsService {

    override fun getMetrics() = BssMetrics(
        videoCount = videoRepository.count(),
        eventCount = eventRepository.count(),
        memberCount = memberRepository.count()
    )
}
