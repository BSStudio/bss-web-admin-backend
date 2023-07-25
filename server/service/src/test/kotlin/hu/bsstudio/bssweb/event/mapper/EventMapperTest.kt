package hu.bsstudio.bssweb.event.mapper

import hu.bsstudio.bssweb.event.entity.DetailedEventEntity
import hu.bsstudio.bssweb.event.entity.EventEntity
import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
import hu.bsstudio.bssweb.video.mapper.VideoMapper
import hu.bsstudio.bssweb.video.model.Video
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
internal class EventMapperTest(
    @MockK private val mockVideoMapper: VideoMapper
) {

    @InjectMockKs
    private lateinit var underTest: EventMapper

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
        val result = underTest.modelToEntity(CREATE_EVENT).apply { id = ID }

        assertThat(result).isEqualTo(CREATED_ENTITY)
    }

    private companion object {
        private val ID = mockk<UUID>()
        private const val URL = "URL"
        private const val TITLE = "TITLE"
        private const val DESCRIPTION = "DESCRIPTION"
        private val DATE = mockk<LocalDate>()
        private const val VISIBLE = true
        private val ENTITY = EventEntity(URL, TITLE, DESCRIPTION, DATE, VISIBLE).also { it.id = ID }
        private val MODEL = Event(ID, URL, TITLE, DESCRIPTION, DATE, VISIBLE)
        private val VIDEO_ENTITY = mockk<SimpleVideoEntity>()
        private val VIDEO = mockk<Video>()
        private val DETAILED_ENTITY = DetailedEventEntity(URL, TITLE, DESCRIPTION, DATE, VISIBLE, listOf(VIDEO_ENTITY)).also { it.id = ID }
        private val DETAILED_MODEL = DetailedEvent(ID, URL, TITLE, DESCRIPTION, DATE, VISIBLE, listOf(VIDEO))
        private val CREATE_EVENT = CreateEvent(URL, TITLE)
        private val CREATED_ENTITY = EventEntity(url = URL, title = TITLE).also { it.id = ID }
    }
}
