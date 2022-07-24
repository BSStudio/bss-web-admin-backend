package hu.bsstudio.bssweb.eventvideo.service

import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.service.EventService
import hu.bsstudio.bssweb.eventvideo.entity.EventVideoEntity
import hu.bsstudio.bssweb.eventvideo.repository.EventVideoRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class DefaultEventVideoServiceTest {

    @MockK
    private lateinit var mockRepository: EventVideoRepository
    @MockK
    private lateinit var mockEventService: EventService
    @InjectMockKs
    private lateinit var underTest: DefaultEventVideoService

    @Test
    fun addVideoToEvent() {
        every { mockRepository.save(EVENT_VIDEO_ENTITY) } returns EVENT_VIDEO_ENTITY
        every { mockEventService.findEventById(EVENT_ID) } returns Optional.of(DETAILED_EVENT)

        val response = underTest.addVideoToEvent(EVENT_ID, VIDEO_ID)

        assertThat(response).hasValue(DETAILED_EVENT)
    }

    @Test
    fun removeVideoFromEvent() {
        every { mockRepository.deleteById(EVENT_VIDEO_ENTITY) } returns Unit

        underTest.removeVideoFromEvent(EVENT_ID, VIDEO_ID)
    }

    private companion object {
        private val EVENT_ID = UUID.fromString("01234567-0123-0123-0123-0123456789ab")
        private val VIDEO_ID = UUID.fromString("11234567-0123-0123-0123-0123456789ab")
        private val EVENT_VIDEO_ENTITY = EventVideoEntity(eventId = EVENT_ID, videoId = VIDEO_ID)
        private val DETAILED_EVENT = DetailedEvent(id = EVENT_ID, url = "url", title = "title", description = "description", date = LocalDate.of(2022, 1, 1), visible = true, videos = listOf())
    }
}
