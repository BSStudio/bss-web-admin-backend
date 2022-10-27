package hu.bsstudio.bssweb.videocrew.controller

import hu.bsstudio.bssweb.video.model.DetailedVideo
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
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class VideoCrewControllerTest {

    @MockK
    private lateinit var mockService: VideoCrewService
    @InjectMockKs
    private lateinit var underTest: VideoCrewController

    @Test
    internal fun getPositions() {
        every { mockService.getPositions() } returns listOf(POSITION)

        val response = this.underTest.getPositions()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(listOf(POSITION))
    }

    @Test
    internal fun addPosition() {
        every { mockService.addPosition(VIDEO_CREW) } returns Optional.of(DETAILED_VIDEO)

        val response = this.underTest.addPosition(VIDEO_ID, POSITION, MEMBER_ID)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(DETAILED_VIDEO)
    }

    @Test
    internal fun addPositionEmpty() {
        every { mockService.addPosition(VIDEO_CREW) } returns Optional.empty()

        val response = this.underTest.addPosition(VIDEO_ID, POSITION, MEMBER_ID)

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.body).isEqualTo(null)
    }

    @Test
    internal fun removePosition() {
        every { mockService.removePosition(VIDEO_CREW) } returns Optional.of(DETAILED_VIDEO)

        val response = this.underTest.removePosition(VIDEO_ID, POSITION, MEMBER_ID)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(DETAILED_VIDEO)
    }

    @Test
    internal fun removePositionEmpty() {
        every { mockService.removePosition(VIDEO_CREW) } returns Optional.empty()

        val response = this.underTest.removePosition(VIDEO_ID, POSITION, MEMBER_ID)

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.body).isEqualTo(null)
    }

    private companion object {
        private val VIDEO_ID = UUID.fromString("01234567-0123-0123-0123-0123456789ab")
        private const val POSITION = "position1"
        private val MEMBER_ID = UUID.fromString("11234567-0123-0123-0123-0123456789ab")
        private val VIDEO_CREW = VideoCrew(VIDEO_ID, POSITION, MEMBER_ID)
        private val SIMPLE_VIDEO_CREW = SimpleCrew(POSITION, MEMBER_ID)
        private val DETAILED_VIDEO = DetailedVideo(
            id = VIDEO_ID,
            url = "url",
            title = "title",
            description = "description",
            visible = true,
            videoUrl = "videoUrl",
            thumbnailUrl = "thumbnailUrl",
            uploadedAt = LocalDate.of(2022, 1, 1),
            crew = listOf(SIMPLE_VIDEO_CREW)
        )
    }
}
