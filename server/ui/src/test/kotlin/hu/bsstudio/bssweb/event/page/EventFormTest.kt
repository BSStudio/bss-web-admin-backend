package hu.bsstudio.bssweb.event.page

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class EventFormTest {
    @Test
    internal fun `should allow an event that ends on its first day or later`() {
        EventForm(dateFrom = DAY, dateTo = DAY).datesInOrder.shouldBeTrue()
        EventForm(dateFrom = DAY, dateTo = DAY.plusDays(1)).datesInOrder.shouldBeTrue()
    }

    @Test
    internal fun `should not allow an event that ends before it starts`() {
        EventForm(dateFrom = DAY, dateTo = DAY.minusDays(1)).datesInOrder.shouldBeFalse()
    }

    @Test
    internal fun `should leave a missing date to its own check`() {
        EventForm().datesInOrder.shouldBeTrue()
        EventForm(dateFrom = DAY).datesInOrder.shouldBeTrue()
    }

    @Test
    internal fun `should not turn a form without dates into an update`() {
        shouldThrow<IllegalArgumentException> { EventForm().toUpdateEvent() }
        shouldThrow<IllegalArgumentException> { EventForm(dateFrom = DAY).toUpdateEvent() }
    }

    private companion object {
        private val DAY = LocalDate.of(2026, 8, 12)
    }
}
