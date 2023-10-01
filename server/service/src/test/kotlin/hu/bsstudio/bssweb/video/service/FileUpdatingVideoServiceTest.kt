package hu.bsstudio.bssweb.video.service

import hu.bsstudio.bssweb.fileserver.client.FileApiClient
import hu.bsstudio.bssweb.fileserver.model.FileUpdate
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.optional.shouldBeEmpty
import io.kotest.matchers.optional.shouldBePresent
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class FileUpdatingVideoServiceTest(
    @MockK private val mockService: VideoService,
    @MockK private val mockClient: FileApiClient
) {

    @InjectMockKs
    private lateinit var underTest: FileUpdatingVideoService

    @Test
    internal fun `should return all videos`() {
        every { mockService.findAllVideos() } returns VIDEO_LIST

        val actual = underTest.findAllVideos()

        actual shouldBeEqual VIDEO_LIST
    }

    @Test
    internal fun `should return all videos paged`() {
        every { mockService.findAllVideos(PAGEABLE) } returns PAGED_VIDEOS

        val actual = underTest.findAllVideos(PAGEABLE)

        actual shouldBeEqual PAGED_VIDEOS
    }

    @Test
    internal fun `should insert new video`() {
        every { mockService.insertVideo(CREATE_VIDEO) } returns VIDEO
        every { VIDEO.id } returns VIDEO_ID
        every { VIDEO.url } returns URL
        every { mockClient.createVideoFolder(FILE_UPDATE) } returns FILE_UPDATE

        val actual = underTest.insertVideo(CREATE_VIDEO)

        actual shouldBeEqual VIDEO
    }

    @Test
    internal fun `should change video visibility`() {
        val videoIds = listOf(VIDEO_ID)
        every { mockService.changeVideoVisibility(videoIds, false) } returns listOf(VIDEO_ID)

        val actual = underTest.changeVideoVisibility(videoIds, false)

        actual.shouldContainExactly(VIDEO_ID)
    }

    @Test
    internal fun `should find video by id`() {
        every { mockService.findVideoById(VIDEO_ID) } returns Optional.of(DETAILED_VIDEO)

        val actual = underTest.findVideoById(VIDEO_ID)

        actual shouldBePresent { it shouldBeEqual DETAILED_VIDEO }
    }

    @Test
    internal fun `should return empty if video was not found`() {
        every { mockService.findVideoById(VIDEO_ID) } returns Optional.empty()

        val actual = underTest.findVideoById(VIDEO_ID)

        actual.shouldBeEmpty()
    }

    @Test
    internal fun `should not update video if id was not found`() {
        every { mockService.updateVideo(VIDEO_ID, UPDATE_VIDEO) } returns Optional.empty()

        val actual = underTest.updateVideo(VIDEO_ID, UPDATE_VIDEO)

        actual.shouldBeEmpty()
    }

    @Test
    internal fun `should update video`() {
        every { mockService.updateVideo(VIDEO_ID, UPDATE_VIDEO) } returns Optional.of(DETAILED_VIDEO)
        every { DETAILED_VIDEO.id } returns VIDEO_ID
        every { DETAILED_VIDEO.url } returns URL
        every { mockClient.updateVideoFolder(FILE_UPDATE) } returns FILE_UPDATE

        val actual = underTest.updateVideo(VIDEO_ID, UPDATE_VIDEO)

        actual shouldBePresent { it shouldBeEqual DETAILED_VIDEO }
    }

    @Test
    internal fun `should remove entity by id`() {
        every { mockService.deleteVideoById(VIDEO_ID) } returns Unit

        underTest.deleteVideoById(VIDEO_ID)
    }

    private companion object {
        private val PAGEABLE = mockk<Pageable>()
        private val VIDEO_ID = mockk<UUID>()
        private const val URL = "url"
        private val VIDEO = mockk<Video>()
        private val VIDEO_LIST = mockk<List<Video>>()
        private val PAGED_VIDEOS = mockk<Page<Video>>()
        private val CREATE_VIDEO = mockk<CreateVideo>()
        private val DETAILED_VIDEO = mockk<DetailedVideo>()
        private val UPDATE_VIDEO = mockk<UpdateVideo>()
        private val FILE_UPDATE = FileUpdate(VIDEO_ID, URL)
    }
}
