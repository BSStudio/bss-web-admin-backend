package hu.bsstudio.bssweb.video.mapper

import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import hu.bsstudio.bssweb.video.entity.VideoEntity
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
import hu.bsstudio.bssweb.videocrew.mapper.VideoCrewMapper
import hu.bsstudio.bssweb.videocrew.model.SimpleCrew
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID

internal class VideoMapperTest {

    @MockK
    private lateinit var mockVideoCrewMapper: VideoCrewMapper

    private lateinit var underTest: VideoMapper

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        val idMapper = { VIDEO_ID }
        underTest = VideoMapper(mockVideoCrewMapper)
        underTest = VideoMapper(mockVideoCrewMapper, idMapper)
    }

    @Test
    internal fun `should map entity model to model`() {
        val result = underTest.entityToModel(videoEntity)

        assertThat(result).isEqualTo(video)
    }

    @Test
    internal fun `should map detailed entity to detailed model`() {
        every { mockVideoCrewMapper.entityToModel(CREW_ENTITY) } returns CREW_MODEL

        val result = underTest.entityToModel(detailedVideoEntity)

        assertThat(result).isEqualTo(detailedVideo)
    }

    @Test
    internal fun `should map map model to entity`() {
        val result = underTest.modelToEntity(createVideo)

        assertThat(result).isEqualTo(createdVideoEntity)
    }

    private companion object {
        private val VIDEO_ID = UUID.fromString("01234567-0123-0123-0123-0123456789ab")
        private const val URL = "url"
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
        private const val VIDEO_URL = "videoUrl"
        private const val THUMBNAIL_URL = "thumbnailUrl"
        private val UPLOADED_AT = LocalDate.of(2022, 1, 1)
        private const val VISIBLE = true
        private val CREW_ENTITY = VideoCrewEntity(VIDEO_ID, "position", UUID.fromString("01234567-0123-0123-0123-0123456789ab"))
        private val VIDEO_CREW = listOf(CREW_ENTITY)
        private val CREW_MODEL = SimpleCrew("position", UUID.fromString("01234567-0123-0123-0123-0123456789ab"))
        private val CREW = listOf(CREW_MODEL)
        private val videoEntity = VideoEntity(VIDEO_ID, URL, TITLE, UPLOADED_AT, VISIBLE)
        private val video = Video(VIDEO_ID, URL, TITLE, UPLOADED_AT, VISIBLE)
        private val detailedVideoEntity = DetailedVideoEntity(VIDEO_ID, URL, TITLE, DESCRIPTION, VIDEO_URL, THUMBNAIL_URL, UPLOADED_AT, VISIBLE, VIDEO_CREW)
        private val detailedVideo = DetailedVideo(VIDEO_ID, URL, TITLE, DESCRIPTION, VIDEO_URL, THUMBNAIL_URL, UPLOADED_AT, VISIBLE, CREW)
        private val createVideo = CreateVideo(URL, TITLE)
        private val createdVideoEntity = VideoEntity(id = VIDEO_ID, url = URL, title = TITLE)
    }
}
