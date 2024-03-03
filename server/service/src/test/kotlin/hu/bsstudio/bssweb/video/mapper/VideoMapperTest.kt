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
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class VideoMapperTest(
    @MockK private val mockVideoCrewMapper: VideoCrewMapper,
) {
    @InjectMockKs
    private lateinit var underTest: VideoMapper

    @Test
    internal fun `should map entity model to model`() {
        val actual = underTest.entityToModel(VIDEO_ENTITY)

        actual shouldBeEqual VIDEO
    }

    @Test
    internal fun `should map detailed entity to detailed model`() {
        every { mockVideoCrewMapper.entityToModel(CREW_ENTITY) } returns CREW_MODEL

        val actual = underTest.entityToModel(DETAILED_VIDEO_ENTITY)

        actual shouldBeEqual DETAILED_VIDEO
    }

    @Test
    internal fun `should map map model to entity`() {
        val actual = underTest.modelToEntity(CREATE_VIDEO).apply { id = VIDEO_ID }

        actual shouldBeEqual CREATED_VIDEO_ENTITY
    }

    @Test
    internal fun `should map update model to entity`() {
        val actual = underTest.updateToEntity(DETAILED_VIDEO_ENTITY, UPDATE_VIDEO)

        actual shouldBeEqual UPDATED_VIDEO_ENTITY
    }

    private companion object {
        private val VIDEO_ID = mockk<UUID>()
        private val URLS = listOf("url")
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
        private val SHOOTING_DATE_START = mockk<LocalDate>()
        private val SHOOTING_DATE_END = mockk<LocalDate>()
        private const val VISIBLE = true
        private val CREW_ENTITY = mockk<DetailedVideoCrewEntity>()
        private val VIDEO_CREW = listOf(CREW_ENTITY)
        private val CREW_MODEL = mockk<VideoCrew>()
        private val CREW = listOf(CREW_MODEL)
        private val VIDEO_ENTITY = SimpleVideoEntity(TITLE, URLS, SHOOTING_DATE_START, SHOOTING_DATE_END, VISIBLE).apply { id = VIDEO_ID }
        private val VIDEO = Video(VIDEO_ID, URLS, TITLE, SHOOTING_DATE_START, SHOOTING_DATE_END, VISIBLE)
        private val DETAILED_VIDEO_ENTITY =
            DetailedVideoEntity(TITLE, URLS, DESCRIPTION, SHOOTING_DATE_START, SHOOTING_DATE_END, VISIBLE).apply {
                id = VIDEO_ID
                videoCrew = VIDEO_CREW
            }
        private val DETAILED_VIDEO =
            DetailedVideo(VIDEO_ID, URLS, TITLE, DESCRIPTION, SHOOTING_DATE_START, SHOOTING_DATE_END, VISIBLE, CREW)
        private val CREATE_VIDEO = CreateVideo(TITLE)
        private val CREATED_VIDEO_ENTITY = SimpleVideoEntity(urls = URLS, title = TITLE).apply { id = VIDEO_ID }
        private val NEW_URLS = listOf("newUrl")
        private const val NEW_TITLE = "NEW_TITLE"
        private const val NEW_DESCRIPTION = "NEW_DESCRIPTION"
        private val NEW_SHOOTING_DATE_START = mockk<LocalDate>()
        private val NEW_SHOOTING_DATE_END = mockk<LocalDate>()
        private const val NEW_VISIBLE = false
        private val UPDATE_VIDEO =
            UpdateVideo(NEW_URLS, NEW_TITLE, NEW_DESCRIPTION, NEW_SHOOTING_DATE_START, NEW_SHOOTING_DATE_END, NEW_VISIBLE)
        private val UPDATED_VIDEO_ENTITY =
            DetailedVideoEntity(NEW_TITLE, NEW_URLS, NEW_DESCRIPTION, NEW_SHOOTING_DATE_START, NEW_SHOOTING_DATE_END, NEW_VISIBLE).apply {
                id = VIDEO_ID
                videoCrew = VIDEO_CREW
            }
    }
}
