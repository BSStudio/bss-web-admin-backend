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
import java.util.UUID

internal class EventMapperTest {

    @MockK
    private lateinit var mockVideoMapper: VideoMapper
    private lateinit var underTest: EventMapper

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        val idGenerator = { ID }
        underTest = EventMapper(mockVideoMapper)
        underTest = EventMapper(mockVideoMapper, idGenerator)
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
        private val ID = UUID.fromString("01234567-0123-0123-0123-0123456789AB")
        private const val URL = "URL"
        private const val TITLE = "TITLE"
        private const val DESCRIPTION = "DESCRIPTION"
        private val DATE = LocalDate.of(2021, 1, 1)
        private const val VISIBLE = true
        private val ENTITY = EventEntity(ID, URL, TITLE, DESCRIPTION, DATE, VISIBLE)
        private val MODEL = Event(ID, URL, TITLE, DESCRIPTION, DATE, VISIBLE)
        private val VIDEO_ENTITY = VideoEntity(id = ID, url = URL, title = TITLE)
        private val VIDEO_ENTITIES = listOf(VIDEO_ENTITY)
        private val VIDEO = Video(ID, URL, TITLE, LocalDate.of(2022, 1, 1), true)
        private val VIDEOS = listOf(VIDEO)
        private val DETAILED_ENTITY = DetailedEventEntity(ID, URL, TITLE, DESCRIPTION, DATE, VISIBLE, VIDEO_ENTITIES)
        private val DETAILED_MODEL = DetailedEvent(ID, URL, TITLE, DESCRIPTION, DATE, VISIBLE, VIDEOS)
        private val CREATE_EVENT = CreateEvent(URL, TITLE)
        private val CREATED_ENTITY = EventEntity(id = ID, url = URL, title = TITLE)
    }
}
