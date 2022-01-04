package hu.bsstudio.bssweb.video.controller

import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.video.service.VideoService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.time.LocalDate
import java.util.Optional

internal class VideoControllerTest {

    @MockK
    private lateinit var mockService: VideoService
    private lateinit var underTest: VideoController

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        this.underTest = VideoController(mockService)
    }

    @Test
    internal fun getAllVideos() {
        val page = 0
        val size = 10
        every { mockService.findAllVideos(page, size) } returns VIDEO_LIST

        val response = this.underTest.getAllVideos(page, size)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(VIDEO_LIST)
    }

    @Test
    internal fun createVideo() {
        every { mockService.insertVideo(CREATE_VIDEO) } returns VIDEO

        val response = this.underTest.createVideo(CREATE_VIDEO)

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(response.body).isEqualTo(VIDEO)
    }

    @Test
    internal fun archiveVideos() {
        val videoIds = listOf(VIDEO_ID)
        val unArchive = false
        every { mockService.archiveVideos(videoIds, unArchive) } returns videoIds

        val response = this.underTest.archiveVideos(videoIds, unArchive)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(videoIds)
    }

    @Test
    internal fun changeVideoVisibility() {
        val videoIds = listOf(VIDEO_ID)
        val visible = false
        every { mockService.changeVideoVisibility(videoIds, visible) } returns videoIds

        val response = this.underTest.changeVideoVisibility(videoIds, visible)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(videoIds)
    }

    @Test
    internal fun getVideo1() {
        every { mockService.findVideoById(VIDEO_ID) } returns Optional.of(DETAILED_VIDEO)

        val response = this.underTest.getVideo(VIDEO_ID)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(DETAILED_VIDEO)
    }

    @Test
    internal fun getVideo2() {
        every { mockService.findVideoById(VIDEO_ID) } returns Optional.empty()

        val response = this.underTest.getVideo(VIDEO_ID)

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.body).isEqualTo(null)
    }

    @Test
    internal fun updateVideo1() {
        every { mockService.updateVideo(VIDEO_ID, UPDATE_VIDEO) } returns Optional.of(DETAILED_VIDEO)

        val response = this.underTest.updateVideo(VIDEO_ID, UPDATE_VIDEO)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(DETAILED_VIDEO)
    }

    @Test
    internal fun updateVideo2() {
        every { mockService.updateVideo(VIDEO_ID, UPDATE_VIDEO) } returns Optional.empty()

        val response = this.underTest.updateVideo(VIDEO_ID, UPDATE_VIDEO)

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.body).isEqualTo(null)
    }

    @Test
    internal fun deleteVideo() {
        every { mockService.deleteVideoById(VIDEO_ID) } returns Unit

        this.underTest.deleteVideo(VIDEO_ID)
    }

    private companion object {
        private const val VIDEO_ID = "videoId"
        private val VIDEO = Video(VIDEO_ID, "title", LocalDate.of(2022, 1, 1), true, false)
        private val VIDEO_LIST = listOf(VIDEO)
        private val CREATE_VIDEO = CreateVideo(VIDEO_ID, "title")
        private val DETAILED_VIDEO = DetailedVideo(VIDEO_ID, "title", "description", null, null, LocalDate.of(2022, 1, 1), true, false, listOf())
        private val UPDATE_VIDEO = UpdateVideo("title", "description", null, null, LocalDate.of(2022, 1, 1), true, false)
    }
}
