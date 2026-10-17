package hu.bsstudio.bssweb.video.page

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import org.junit.jupiter.api.Test
import org.springframework.dao.DataIntegrityViolationException

internal class HiddenEventTest {
    @Test
    internal fun `should find the trigger's refusal anywhere in the causes`() {
        DataIntegrityViolationException("could not execute statement", RuntimeException("ERROR: video_in_hidden_event: …"))
            .isVideoInHiddenEvent()
            .shouldBeTrue()
    }

    @Test
    internal fun `should not mistake another integrity error for it`() {
        DataIntegrityViolationException("duplicate key value violates unique constraint").isVideoInHiddenEvent().shouldBeFalse()
        DataIntegrityViolationException(null).isVideoInHiddenEvent().shouldBeFalse()
    }
}
