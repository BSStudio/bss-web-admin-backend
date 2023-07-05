package hu.bsstudio.bssweb.eventvideo.controller

import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.eventvideo.service.EventVideoService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import java.util.Optional
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class EventVideoControllerTest {

    @MockK
    private lateinit var mockService: EventVideoService

    @InjectMockKs
    private lateinit var underTest: EventVideoController

    @Test
    fun addVideoToEvent() {
        every { mockService.addVideoToEvent(EVENT_ID, VIDEO_ID) } returns Optional.of(DETAILED_EVENT)

        val response = underTest.addVideoToEvent(EVENT_ID, VIDEO_ID)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(DETAILED_EVENT)
    }

    @Test
    fun removeVideoFromEvent() {
        every { mockService.removeVideoFromEvent(EVENT_ID, VIDEO_ID) } returns Optional.of(DETAILED_EVENT)

        val response = underTest.removeVideoFromEvent(EVENT_ID, VIDEO_ID)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(DETAILED_EVENT)
    }

    private companion object {
        private val EVENT_ID = mockk<UUID>()
        private val VIDEO_ID = mockk<UUID>()
        private val DETAILED_EVENT = mockk<DetailedEvent>()
    }
}
