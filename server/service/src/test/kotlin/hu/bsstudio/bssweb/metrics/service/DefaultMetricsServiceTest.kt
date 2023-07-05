package hu.bsstudio.bssweb.metrics.service

import hu.bsstudio.bssweb.event.repository.EventRepository
import hu.bsstudio.bssweb.member.repository.MemberRepository
import hu.bsstudio.bssweb.metrics.model.BssMetrics
import hu.bsstudio.bssweb.video.repository.VideoRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class DefaultMetricsServiceTest {

    @MockK
    private lateinit var videoRepository: VideoRepository

    @MockK
    private lateinit var eventRepository: EventRepository

    @MockK
    private lateinit var memberRepository: MemberRepository

    @InjectMockKs
    private lateinit var underTest: DefaultMetricsService

    @Test
    internal fun `should return counts`() {
        every { videoRepository.count() } returns VIDEO_COUNT
        every { eventRepository.count() } returns EVENT_COUNT
        every { memberRepository.count() } returns MEMBER_COUNT

        val result = underTest.getMetrics()

        assertThat(result).isEqualTo(BSS_METRICS)
    }

    private companion object {
        private const val VIDEO_COUNT = 6969L
        private const val EVENT_COUNT = 420L
        private const val MEMBER_COUNT = 125L
        private val BSS_METRICS = BssMetrics(videoCount = VIDEO_COUNT, eventCount = EVENT_COUNT, memberCount = MEMBER_COUNT)
    }
}
