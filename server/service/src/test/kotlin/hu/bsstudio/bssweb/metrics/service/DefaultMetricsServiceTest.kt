package hu.bsstudio.bssweb.metrics.service

import hu.bsstudio.bssweb.event.repository.EventRepository
import hu.bsstudio.bssweb.label.repository.LabelRepository
import hu.bsstudio.bssweb.member.repository.MemberRepository
import hu.bsstudio.bssweb.metrics.model.BssMetrics
import hu.bsstudio.bssweb.video.repository.VideoRepository
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class DefaultMetricsServiceTest(
    @param:MockK private val videoRepository: VideoRepository,
    @param:MockK private val eventRepository: EventRepository,
    @param:MockK private val memberRepository: MemberRepository,
    @param:MockK private val labelRepository: LabelRepository,
) {
    @InjectMockKs
    private lateinit var underTest: DefaultMetricsService

    @Test
    internal fun `should return counts`() {
        every { videoRepository.count() } returns VIDEO_COUNT
        every { eventRepository.count() } returns EVENT_COUNT
        every { memberRepository.count() } returns MEMBER_COUNT
        every { labelRepository.count() } returns LABEL_COUNT

        val actual = underTest.getMetrics()

        actual shouldBeEqual BSS_METRICS
    }

    private companion object {
        private const val VIDEO_COUNT = 6969L
        private const val EVENT_COUNT = 420L
        private const val MEMBER_COUNT = 125L
        private const val LABEL_COUNT = 37L
        private val BSS_METRICS =
            BssMetrics(videoCount = VIDEO_COUNT, eventCount = EVENT_COUNT, memberCount = MEMBER_COUNT, labelCount = LABEL_COUNT)
    }
}
