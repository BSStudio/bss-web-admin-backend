package hu.bsstudio.bssweb.event.mapper

import hu.bsstudio.bssweb.event.entity.DetailedEventEntity
import hu.bsstudio.bssweb.event.entity.EventEntity
import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.video.entity.VideoEntity
import hu.bsstudio.bssweb.video.mapper.VideoMapper
import hu.bsstudio.bssweb.video.model.Video
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class EventMapperTest {

    @MockK
    private lateinit var mockVideoMapper: VideoMapper
    private lateinit var underTest: EventMapper

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        underTest = EventMapper(mockVideoMapper)
    }

    @Test
    internal fun `should map entity to model`() {
        val result = underTest.entityToModel(ENTITY)

        assertThat(result).isEqualTo(MODEL)
    }

    @Test
    internal fun `should map detailed entity to detailed model`() {
        every { mockVideoMapper.entityToModel(VIDEO_ENTITY) } returns VIDEO
        val result = underTest.entityToModel(DETAILED_ENTITY)

        assertThat(result).isEqualTo(DETAILED_MODEL)
    }

    @Test
    internal fun `should map model to entity`() {
        val result = underTest.modelToEntity(CREATE_EVENT)

        assertThat(result).isEqualTo(CREATED_ENTITY)
    }

    private companion object {
        private const val ID = "ID"
        private const val NAME = "NAME"
        private const val DESCRIPTION = "DESCRIPTION"
        private val DATE = LocalDate.of(2021, 1, 1)
        private const val ARCHIVED = true
        private val ENTITY = EventEntity(ID, NAME, DESCRIPTION, DATE, ARCHIVED)
        private val MODEL = Event(ID, NAME, DESCRIPTION, DATE, ARCHIVED)
        private val VIDEO_ENTITY = VideoEntity("id", "title")
        private val VIDEO_ENTITIES = listOf(VIDEO_ENTITY)
        private val VIDEO = Video("id", "title", LocalDate.of(2022, 1, 1), true, false)
        private val VIDEOS = listOf(VIDEO)
        private val DETAILED_ENTITY = DetailedEventEntity(ID, NAME, DESCRIPTION, DATE, ARCHIVED, VIDEO_ENTITIES)
        private val DETAILED_MODEL = DetailedEvent(ID, NAME, DESCRIPTION, DATE, ARCHIVED, VIDEOS)
        private val CREATE_EVENT = CreateEvent(ID, NAME)
        private val CREATED_ENTITY = EventEntity(ID, NAME)
    }
}
