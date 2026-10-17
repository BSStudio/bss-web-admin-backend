package hu.bsstudio.bssweb.video.page

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class VideoFormTest {
    @Test
    internal fun `should allow a shooting that ends on its first day or later`() {
        VideoForm(shootingDateStart = DAY, shootingDateEnd = DAY).datesInOrder.shouldBeTrue()
        VideoForm(shootingDateStart = DAY, shootingDateEnd = DAY.plusDays(1)).datesInOrder.shouldBeTrue()
    }

    @Test
    internal fun `should not allow a shooting that ends before it starts`() {
        VideoForm(shootingDateStart = DAY, shootingDateEnd = DAY.minusDays(1)).datesInOrder.shouldBeFalse()
    }

    @Test
    internal fun `should leave a missing date to its own check`() {
        VideoForm().datesInOrder.shouldBeTrue()
        VideoForm(shootingDateStart = DAY).datesInOrder.shouldBeTrue()
    }

    @Test
    internal fun `should not turn a form without dates into an update`() {
        shouldThrow<IllegalArgumentException> { VideoForm().toUpdateVideo() }
        shouldThrow<IllegalArgumentException> { VideoForm(shootingDateStart = DAY).toUpdateVideo() }
    }

    private companion object {
        private val DAY = LocalDate.of(2026, 8, 13)
    }
}
