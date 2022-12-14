package hu.bsstudio.bssweb.video.service

import hu.bsstudio.bssweb.fileserver.client.FileApiClient
import hu.bsstudio.bssweb.fileserver.model.FileUpdate
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.time.LocalDate
import java.util.Optional
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class FileUpdatingVideoServiceTest {

    @MockK
    private lateinit var mockService: VideoService

    @MockK
    private lateinit var mockClient: FileApiClient

    @InjectMockKs
    private lateinit var underTest: FileUpdatingVideoService

    @Test
    internal fun `should return all videos`() {
        every { mockService.findAllVideos() } returns VIDEO_LIST

        val response = underTest.findAllVideos()

        Assertions.assertThat(response).isEqualTo(VIDEO_LIST)
    }

    @Test
    internal fun `should return all videos paged`() {
        every { mockService.findAllVideos(PAGEABLE) } returns PAGED_VIDEOS

        val response = underTest.findAllVideos(PAGEABLE)

        Assertions.assertThat(response).isEqualTo(PAGED_VIDEOS)
    }

    @Test
    internal fun `should insert new video`() {
        every { mockService.insertVideo(CREATE_VIDEO) } returns VIDEO
        every { mockClient.createVideoFolder(FILE_UPDATE) } returns FILE_UPDATE

        val response = underTest.insertVideo(CREATE_VIDEO)

        Assertions.assertThat(response).isEqualTo(VIDEO)
    }

    @Test
    internal fun `should change video visibility`() {
        val videoIds = listOf(VIDEO_ID)
        every { mockService.changeVideoVisibility(videoIds, false) } returns listOf(VIDEO_ID)

        val response = underTest.changeVideoVisibility(videoIds, false)

        Assertions.assertThat(response).isEqualTo(listOf(VIDEO_ID))
    }

    @Test
    internal fun `should find video by id`() {
        every { mockService.findVideoById(VIDEO_ID) } returns Optional.of(DETAILED_VIDEO)

        val response = underTest.findVideoById(VIDEO_ID)

        Assertions.assertThat(response).hasValue(DETAILED_VIDEO)
    }

    @Test
    internal fun `should return empty if video was not found`() {
        every { mockService.findVideoById(VIDEO_ID) } returns Optional.empty()

        val response = underTest.findVideoById(VIDEO_ID)

        Assertions.assertThat(response).isEmpty
    }

    @Test
    internal fun `should not update video if id was not found`() {
        every { mockService.updateVideo(VIDEO_ID, UPDATE_VIDEO) } returns Optional.empty()

        val response = underTest.updateVideo(VIDEO_ID, UPDATE_VIDEO)

        Assertions.assertThat(response).isEmpty
    }

    @Test
    internal fun `should update video`() {
        every { mockService.updateVideo(VIDEO_ID, UPDATE_VIDEO) } returns Optional.of(DETAILED_VIDEO)
        every { mockClient.updateVideoFolder(FILE_UPDATE) } returns FILE_UPDATE

        val response = underTest.updateVideo(VIDEO_ID, UPDATE_VIDEO)

        Assertions.assertThat(response).hasValue(DETAILED_VIDEO)
    }

    @Test
    internal fun `should remove entity by id`() {
        every { mockService.deleteVideoById(VIDEO_ID) } returns Unit

        underTest.deleteVideoById(VIDEO_ID)
    }

    private companion object {
        private val PAGEABLE = Pageable.unpaged()
        private val VIDEO_ID = UUID.fromString("01234567-0123-0123-0123-0123456789ab")
        private val VIDEO = Video(VIDEO_ID, "url", "title", LocalDate.of(2022, 1, 1), visible = true)
        private val VIDEO_LIST = listOf(VIDEO)
        private val PAGED_VIDEOS = PageImpl(listOf(VIDEO))
        private val CREATE_VIDEO = CreateVideo("url", "title")
        private val DETAILED_VIDEO = DetailedVideo(VIDEO_ID, "url", "title", "description", LocalDate.of(2022, 1, 1), visible = true, crew = listOf())
        private val UPDATE_VIDEO = UpdateVideo("updatedUrl", "updatedTitle", "updatedDescription", LocalDate.of(2022, 2, 2), visible = false)
        private val FILE_UPDATE = FileUpdate(VIDEO_ID, "url")
    }
}
