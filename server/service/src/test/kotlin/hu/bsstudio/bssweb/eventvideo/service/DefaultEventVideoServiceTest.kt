package hu.bsstudio.bssweb.eventvideo.service

import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.service.EventService
import hu.bsstudio.bssweb.eventvideo.entity.EventVideoEntity
import hu.bsstudio.bssweb.eventvideo.repository.EventVideoRepository
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.optional.shouldBePresent
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.Optional
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class DefaultEventVideoServiceTest(
    @MockK private val mockRepository: EventVideoRepository,
    @MockK private val mockEventService: EventService
) {

    @InjectMockKs
    private lateinit var underTest: DefaultEventVideoService

    @Test
    fun addVideoToEvent() {
        every { mockRepository.save(EVENT_VIDEO_ENTITY) } returns EVENT_VIDEO_ENTITY
        every { mockEventService.findEventById(EVENT_ID) } returns Optional.of(DETAILED_EVENT)

        val actual = underTest.addVideoToEvent(EVENT_ID, VIDEO_ID)

        actual shouldBePresent { it shouldBeEqual DETAILED_EVENT }
    }

    @Test
    fun removeVideoFromEvent() {
        every { mockRepository.deleteById(EVENT_VIDEO_ENTITY) } returns Unit
        every { mockEventService.findEventById(EVENT_ID) } returns Optional.of(DETAILED_EVENT)

        val actual = underTest.removeVideoFromEvent(EVENT_ID, VIDEO_ID)

        actual shouldBePresent { it shouldBeEqual DETAILED_EVENT }
    }

    private companion object {
        private val EVENT_ID = mockk<UUID>()
        private val VIDEO_ID = mockk<UUID>()
        private val EVENT_VIDEO_ENTITY = EventVideoEntity(eventId = EVENT_ID, videoId = VIDEO_ID)
        private val DETAILED_EVENT = mockk<DetailedEvent>()
    }
}
