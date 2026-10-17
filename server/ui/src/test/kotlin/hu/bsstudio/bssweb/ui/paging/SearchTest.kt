package hu.bsstudio.bssweb.ui.paging

import io.kotest.matchers.collections.shouldContainExactly
import org.junit.jupiter.api.Test

internal class SearchTest {
    private val events = listOf("Gólyatábor 2026" to "golyatabor-2026", "Őszi Qpa" to "oszi-qpa", "Gólyabál" to "golyabal")

    @Test
    internal fun `should ignore case and accents`() {
        events.search("GÓLYATABOR") { listOf(it.first) } shouldContainExactly listOf(events[0])
        events.search("őszi") { listOf(it.first) } shouldContainExactly listOf(events[1])
        events.search("osZi") { listOf(it.first) } shouldContainExactly listOf(events[1])
    }

    @Test
    internal fun `should need every word, in any of the texts`() {
        events.search(" golya   2026 ") { listOf(it.first, it.second) } shouldContainExactly listOf(events[0])
    }

    @Test
    internal fun `should keep everything for a blank query`() {
        events.search("  ") { listOf(it.first) } shouldContainExactly events
    }
}
