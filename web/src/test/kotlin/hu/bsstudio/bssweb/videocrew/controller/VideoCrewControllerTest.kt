package hu.bsstudio.bssweb.videocrew.controller

import hu.bsstudio.bssweb.videocrew.model.SimpleCrew
import hu.bsstudio.bssweb.videocrew.model.VideoCrew
import hu.bsstudio.bssweb.videocrew.service.VideoCrewService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class VideoCrewControllerTest {

    @MockK
    private lateinit var mockService: VideoCrewService
    @InjectMockKs
    private lateinit var underTest: VideoCrewController

    @Test
    internal fun addPosition() {
        every { mockService.addPosition(VIDEO_CREW) } returns VIDEO_CREW_LIST

        val response = this.underTest.addPosition(VIDEO_ID, POSITION, MEMBER_ID)

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(response.body).isEqualTo(VIDEO_CREW_LIST)
    }

    @Test
    internal fun removePosition() {
        every { mockService.removePosition(VIDEO_CREW) } returns VIDEO_CREW_LIST

        val response = this.underTest.removePosition(VIDEO_ID, POSITION, MEMBER_ID)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(VIDEO_CREW_LIST)
    }

    private companion object {
        private val VIDEO_ID = UUID.fromString("01234567-0123-0123-0123-0123456789ab")
        private const val POSITION = "position"
        private val MEMBER_ID = UUID.fromString("01234567-0123-0123-0123-0123456789ab")
        private val VIDEO_CREW = VideoCrew(VIDEO_ID, POSITION, MEMBER_ID)
        private val SIMPLE_CREW = SimpleCrew(POSITION, MEMBER_ID)
        private val VIDEO_CREW_LIST = listOf(SIMPLE_CREW)
    }
}
