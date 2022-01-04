package hu.bsstudio.bssweb.event.mapper

import hu.bsstudio.bssweb.event.entity.EventEntity
import hu.bsstudio.bssweb.event.model.CreateEvent
import hu.bsstudio.bssweb.event.model.Event
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class EventMapperTest {

    lateinit var underTest: EventMapper

    @BeforeEach
    internal fun setUp() {
        this.underTest = EventMapper()
    }

    @Test
    internal fun `should map entity to model`() {
        val result = this.underTest.entityToModel(ENTITY)

        assertThat(result).isEqualTo(MODEL)
    }

    @Test
    internal fun `should map model to entity`() {
        val result = this.underTest.modelToEntity(CREATE_EVENT)

        assertThat(result).isEqualTo(CREATED_ENTITY)
    }

    private companion object {
        private const val ID: String = "ID"
        private const val NAME: String = "NAME"
        private const val DESCRIPTION: String = "DESCRIPTION"
        private val DATE: LocalDate = LocalDate.of(2021, 1, 1)
        private const val ARCHIVED: Boolean = true
        private val ENTITY: EventEntity = EventEntity(ID, NAME, DESCRIPTION, DATE, ARCHIVED)
        private val MODEL: Event = Event(ID, NAME, DESCRIPTION, DATE, ARCHIVED)
        private val CREATE_EVENT: CreateEvent = CreateEvent(ID, NAME)
        private val CREATED_ENTITY: EventEntity = EventEntity(ID, NAME)
    }
}
