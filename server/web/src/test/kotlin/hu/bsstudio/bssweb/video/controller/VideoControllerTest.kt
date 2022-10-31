package hu.bsstudio.bssweb.video.controller

import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.video.service.VideoService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class VideoControllerTest {

    @MockK
    private lateinit var mockService: VideoService
    @InjectMockKs
    private lateinit var underTest: VideoController

    @Test
    internal fun getAllVideos() {
        every { mockService.findAllVideos() } returns listOf(VIDEO)

        val response = this.underTest.getAllVideos()

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(listOf(VIDEO))
    }

    @Test
    internal fun getAllVideosPaged() {
        every { mockService.findAllVideos(PAGEABLE) } returns PAGED_VIDEOS

        val response = this.underTest.getAllVideos(PAGEABLE)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(PAGED_VIDEOS)
    }

    @Test
    internal fun createVideo() {
        every { mockService.insertVideo(CREATE_VIDEO) } returns VIDEO

        val response = this.underTest.createVideo(CREATE_VIDEO)

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(response.body).isEqualTo(VIDEO)
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
        private val PAGEABLE = Pageable.unpaged()
        private val VIDEO_ID = UUID.fromString("01234567-0123-0123-0123-0123456789ab")
        private val VIDEO = Video(id = VIDEO_ID, url = "url", title = "title", uploadedAt = LocalDate.of(2022, 1, 1), visible = true)
        private val PAGED_VIDEOS = PageImpl(listOf(VIDEO))
        private val CREATE_VIDEO = CreateVideo(url = "url", title = "title")
        private val DETAILED_VIDEO = DetailedVideo(id = VIDEO_ID, url = "url", title = "title", "description", uploadedAt = LocalDate.of(2022, 1, 1), visible = true, crew = listOf())
        private val UPDATE_VIDEO = UpdateVideo(url = "url", title = "title", "description", "videoUrl", "thumbnailUrl", uploadedAt = LocalDate.of(2022, 1, 1), visible = true)
    }
}
