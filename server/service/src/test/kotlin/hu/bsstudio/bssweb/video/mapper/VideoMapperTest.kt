package hu.bsstudio.bssweb.video.mapper

import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
import hu.bsstudio.bssweb.video.model.CreateVideo
import hu.bsstudio.bssweb.video.model.DetailedVideo
import hu.bsstudio.bssweb.video.model.UpdateVideo
import hu.bsstudio.bssweb.video.model.Video
import hu.bsstudio.bssweb.videocrew.entity.DetailedVideoCrewEntity
import hu.bsstudio.bssweb.videocrew.mapper.VideoCrewMapper
import hu.bsstudio.bssweb.videocrew.model.VideoCrew
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class VideoMapperTest(
    @MockK private val mockVideoCrewMapper: VideoCrewMapper
) {

    @InjectMockKs
    private lateinit var underTest: VideoMapper

    @Test
    internal fun `should map entity model to model`() {
        val result = underTest.entityToModel(VIDEO_ENTITY)

        assertThat(result).isEqualTo(VIDEO)
    }

    @Test
    internal fun `should map detailed entity to detailed model`() {
        every { mockVideoCrewMapper.entityToModel(CREW_ENTITY) } returns CREW_MODEL

        val result = underTest.entityToModel(DETAILED_VIDEO_ENTITY)

        assertThat(result).isEqualTo(DETAILED_VIDEO)
    }

    @Test
    internal fun `should map map model to entity`() {
        val result = underTest.modelToEntity(CREATE_VIDEO).apply { id = VIDEO_ID }

        assertThat(result).isEqualTo(CREATED_VIDEO_ENTITY)
    }

    @Test
    internal fun `should map update model to entity`() {
        val result = underTest.updateToEntity(DETAILED_VIDEO_ENTITY, UPDATE_VIDEO)

        assertThat(result).isEqualTo(UPDATED_VIDEO_ENTITY)
    }

    private companion object {
        private val VIDEO_ID = mockk<UUID>()
        private const val URL = "url"
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
        private val UPLOADED_AT = mockk<LocalDate>()
        private const val VISIBLE = true
        private val CREW_ENTITY = mockk<DetailedVideoCrewEntity>()
        private val VIDEO_CREW = listOf(CREW_ENTITY)
        private val CREW_MODEL = mockk<VideoCrew>()
        private val CREW = listOf(CREW_MODEL)
        private val VIDEO_ENTITY = SimpleVideoEntity(URL, TITLE, UPLOADED_AT, VISIBLE).apply { id = VIDEO_ID }
        private val VIDEO = Video(VIDEO_ID, URL, TITLE, UPLOADED_AT, VISIBLE)
        private val DETAILED_VIDEO_ENTITY = DetailedVideoEntity(URL, TITLE, DESCRIPTION, UPLOADED_AT, VISIBLE).apply { id = VIDEO_ID; videoCrew = VIDEO_CREW }
        private val DETAILED_VIDEO = DetailedVideo(VIDEO_ID, URL, TITLE, DESCRIPTION, UPLOADED_AT, VISIBLE, CREW)
        private val CREATE_VIDEO = CreateVideo(URL, TITLE)
        private val CREATED_VIDEO_ENTITY = SimpleVideoEntity(url = URL, title = TITLE).apply { id = VIDEO_ID }
        private const val NEW_URL = "NEW_URL"
        private const val NEW_TITLE = "NEW_TITLE"
        private const val NEW_DESCRIPTION = "NEW_DESCRIPTION"
        private val NEW_UPLOADED_AT = mockk<LocalDate>()
        private const val NEW_VISIBLE = false
        private val UPDATE_VIDEO = UpdateVideo(NEW_URL, NEW_TITLE, NEW_DESCRIPTION, NEW_UPLOADED_AT, NEW_VISIBLE)
        private val UPDATED_VIDEO_ENTITY = DetailedVideoEntity(NEW_URL, NEW_TITLE, NEW_DESCRIPTION, NEW_UPLOADED_AT, NEW_VISIBLE).apply { id = VIDEO_ID; videoCrew = VIDEO_CREW }
    }
}
