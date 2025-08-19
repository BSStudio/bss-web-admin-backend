package hu.bsstudio.bssweb.event.mapper

import hu.bsstudio.bssweb.event.entity.DetailedEventEntity
import hu.bsstudio.bssweb.event.entity.SimpleEventEntity
import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.DetailedEvent
import hu.bsstudio.bssweb.event.model.Event
import hu.bsstudio.bssweb.event.model.UpdateEvent
import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
import hu.bsstudio.bssweb.video.mapper.VideoMapper
import hu.bsstudio.bssweb.video.model.Video
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
internal class EventMapperTest(
    @param:MockK private val mockVideoMapper: VideoMapper,
) {
    @InjectMockKs
    private lateinit var underTest: EventMapper

    @Test
    internal fun `should map entity to model`() {
        val actual = underTest.entityToModel(ENTITY)

        actual shouldBeEqual MODEL
    }

    @Test
    internal fun `should map detailed entity to detailed model`() {
        every { mockVideoMapper.entityToModel(VIDEO_ENTITY) } returns VIDEO

        val actual = underTest.entityToModel(DETAILED_ENTITY)

        actual shouldBeEqual DETAILED_MODEL
    }

    @Test
    internal fun `should map model to entity`() {
        val actual = underTest.modelToEntity(CREATE_EVENT).apply { id = ID }

        actual shouldBeEqual CREATED_ENTITY
    }

    @Test
    internal fun `should map update to entity`() {
        val actual = underTest.updateToEntity(DETAILED_ENTITY, UPDATE_EVENT)

        actual shouldBeEqual UPDATED_ENTITY
    }

    private companion object {
        private val ID = mockk<UUID>()
        private const val URL = "URL"
        private const val TITLE = "TITLE"
        private const val DESCRIPTION = "DESCRIPTION"
        private val DATE_FROM = mockk<LocalDate>()
        private val DATE_TO = mockk<LocalDate>()
        private const val VISIBLE = true
        private val ENTITY = SimpleEventEntity(URL, TITLE, DESCRIPTION, DATE_FROM, DATE_TO, VISIBLE).apply { id = ID }
        private val MODEL = Event(ID, URL, TITLE, DESCRIPTION, DATE_FROM, DATE_TO, VISIBLE)
        private val VIDEO_ENTITY = mockk<SimpleVideoEntity>()
        private val VIDEO = mockk<Video>()
        private val DETAILED_ENTITY =
            DetailedEventEntity(URL, TITLE, DESCRIPTION, DATE_FROM, DATE_TO, VISIBLE).apply {
                id = ID
                videos = listOf(VIDEO_ENTITY)
            }
        private val DETAILED_MODEL = DetailedEvent(ID, URL, TITLE, DESCRIPTION, DATE_FROM, DATE_TO, VISIBLE, listOf(VIDEO))
        private val CREATE_EVENT = CreateEvent(URL, TITLE)
        private val CREATED_ENTITY = SimpleEventEntity(url = URL, title = TITLE).apply { id = ID }
        private const val NEW_URL = "NEW_URL"
        private const val NEW_TITLE = "NEW_TITLE"
        private const val NEW_DESCRIPTION = "NEW_DESCRIPTION"
        private val NEW_DATE_FROM = mockk<LocalDate>()
        private val NEW_DATE_TO = mockk<LocalDate>()
        private const val NEW_VISIBLE = false
        private val UPDATE_EVENT = UpdateEvent(NEW_URL, NEW_TITLE, NEW_DESCRIPTION, NEW_DATE_FROM, NEW_DATE_TO, NEW_VISIBLE)
        private val UPDATED_ENTITY =
            DetailedEventEntity(NEW_URL, NEW_TITLE, NEW_DESCRIPTION, NEW_DATE_FROM, NEW_DATE_TO, NEW_VISIBLE).apply {
                id = ID
                videos = listOf(VIDEO_ENTITY)
            }
    }
}
