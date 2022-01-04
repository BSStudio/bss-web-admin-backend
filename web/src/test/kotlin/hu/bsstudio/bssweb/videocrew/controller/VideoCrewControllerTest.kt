package hu.bsstudio.bssweb.videocrew.controller

import hu.bsstudio.bssweb.videocrew.model.SimpleCrew
import hu.bsstudio.bssweb.videocrew.model.VideoCrew
import hu.bsstudio.bssweb.videocrew.service.VideoCrewService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

internal class VideoCrewControllerTest {

    @MockK
    private lateinit var mockService: VideoCrewService
    private lateinit var underTest: VideoCrewController

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        this.underTest = VideoCrewController(mockService)
    }

    @Test
    internal fun addPosition() {
        every { mockService.addPosition(VIDEO_CREW) } returns VIDEO_CREW_LIST

        val response = this.underTest.addPosition(VIDEO_ID, POSITION, MEMBER_ID)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
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
        private const val VIDEO_ID = "videoId"
        private const val POSITION = "position"
        private const val MEMBER_ID = "memberId"
        private val VIDEO_CREW = VideoCrew(VIDEO_ID, POSITION, MEMBER_ID)
        private val SIMPLE_CREW = SimpleCrew(VIDEO_ID, POSITION)
        private val VIDEO_CREW_LIST = listOf(SIMPLE_CREW)
    }
}
