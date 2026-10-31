package hu.bsstudio.bssweb.video.service

import hu.bsstudio.bssweb.fileserver.client.FileApiClient
import hu.bsstudio.bssweb.fileserver.model.VideoFileUpdate
import hu.bsstudio.bssweb.revision.model.RevisionInfo
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
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class FileUpdatingVideoServiceTest(
    @param:MockK private val mockService: VideoService,
    @param:MockK private val mockClient: FileApiClient,
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
        every { VIDEO.urls } returns URLS
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
        every { DETAILED_VIDEO.urls } returns URLS
        every { mockClient.updateVideoFolder(FILE_UPDATE) } returns FILE_UPDATE

        val actual = underTest.updateVideo(VIDEO_ID, UPDATE_VIDEO)

        actual shouldBePresent { it shouldBeEqual DETAILED_VIDEO }
    }

    @Test
    internal fun `should remove entity by id`() {
        every { mockService.deleteVideoById(VIDEO_ID) } returns Unit

        underTest.deleteVideoById(VIDEO_ID)
    }

    @Test
    internal fun `should return deleted videos`() {
        every { mockService.findDeletedVideos(PAGEABLE) } returns PAGED_VIDEOS

        val actual = underTest.findDeletedVideos(PAGEABLE)

        actual shouldBeEqual PAGED_VIDEOS
    }

    @Test
    internal fun `should restore a video and update its folder`() {
        every { mockService.restoreVideo(VIDEO_ID) } returns Optional.of(DETAILED_VIDEO)
        every { DETAILED_VIDEO.id } returns VIDEO_ID
        every { DETAILED_VIDEO.urls } returns URLS
        every { mockClient.updateVideoFolder(FILE_UPDATE) } returns FILE_UPDATE

        val actual = underTest.restoreVideo(VIDEO_ID)

        actual shouldBePresent { it shouldBeEqual DETAILED_VIDEO }
        verify { mockClient.updateVideoFolder(FILE_UPDATE) }
    }

    @Test
    internal fun `should return the revisions of a video`() {
        every { mockService.findVideoRevisions(VIDEO_ID) } returns REVISION_INFOS

        val actual = underTest.findVideoRevisions(VIDEO_ID)

        actual shouldBeEqual REVISION_INFOS
    }

    @Test
    internal fun `should return a video as it was at a revision`() {
        every { mockService.findVideoRevision(VIDEO_ID, REVISION) } returns Optional.of(DETAILED_VIDEO)

        val actual = underTest.findVideoRevision(VIDEO_ID, REVISION)

        actual shouldBePresent { it shouldBeEqual DETAILED_VIDEO }
    }

    @Test
    internal fun `should restore a revision of a video and update its folder`() {
        every { mockService.restoreVideoRevision(VIDEO_ID, REVISION) } returns Optional.of(DETAILED_VIDEO)
        every { DETAILED_VIDEO.id } returns VIDEO_ID
        every { DETAILED_VIDEO.urls } returns URLS
        every { mockClient.updateVideoFolder(FILE_UPDATE) } returns FILE_UPDATE

        val actual = underTest.restoreVideoRevision(VIDEO_ID, REVISION)

        actual shouldBePresent { it shouldBeEqual DETAILED_VIDEO }
        verify { mockClient.updateVideoFolder(FILE_UPDATE) }
    }

    private companion object {
        private const val REVISION = 3L
        private val REVISION_INFOS = listOf(mockk<RevisionInfo>())
        private val PAGEABLE = mockk<Pageable>()
        private val VIDEO_ID = mockk<UUID>()
        private val URLS = listOf("url")
        private val VIDEO = mockk<Video>()
        private val VIDEO_LIST = listOf<Video>()
        private val PAGED_VIDEOS = Page.empty<Video>()
        private val CREATE_VIDEO = mockk<CreateVideo>()
        private val DETAILED_VIDEO = mockk<DetailedVideo>()
        private val UPDATE_VIDEO = mockk<UpdateVideo>()
        private val FILE_UPDATE = VideoFileUpdate(VIDEO_ID, URLS)
    }
}
