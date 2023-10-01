package hu.bsstudio.bssweb.metrics.service

import hu.bsstudio.bssweb.event.repository.SimpleEventRepository
import hu.bsstudio.bssweb.member.repository.MemberRepository
import hu.bsstudio.bssweb.metrics.model.BssMetrics
import hu.bsstudio.bssweb.video.repository.SimpleVideoRepository
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class DefaultMetricsServiceTest(
    @MockK private val videoRepository: SimpleVideoRepository,
    @MockK private val eventRepository: SimpleEventRepository,
    @MockK private val memberRepository: MemberRepository
) {

    @InjectMockKs
    private lateinit var underTest: DefaultMetricsService

    @Test
    internal fun `should return counts`() {
        every { videoRepository.count() } returns VIDEO_COUNT
        every { eventRepository.count() } returns EVENT_COUNT
        every { memberRepository.count() } returns MEMBER_COUNT

        val actual = underTest.getMetrics()

        actual shouldBeEqual BSS_METRICS
    }

    private companion object {
        private const val VIDEO_COUNT = 6969L
        private const val EVENT_COUNT = 420L
        private const val MEMBER_COUNT = 125L
        private val BSS_METRICS = BssMetrics(videoCount = VIDEO_COUNT, eventCount = EVENT_COUNT, memberCount = MEMBER_COUNT)
    }
}
