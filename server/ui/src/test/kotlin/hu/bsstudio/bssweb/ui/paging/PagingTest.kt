package hu.bsstudio.bssweb.ui.paging

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageRequest

internal class PagingTest {
    private val items = (1..5).toList()

    @Test
    internal fun `should return the requested page`() {
        val page = items.toPage(PageRequest.of(0, 2))

        page.content shouldContainExactly listOf(1, 2)
        page.totalElements shouldBe 5
        page.totalPages shouldBe 3
    }

    @Test
    internal fun `should return a shorter last page`() {
        items.toPage(PageRequest.of(2, 2)).content shouldContainExactly listOf(5)
    }

    @Test
    internal fun `should return an empty page past the end`() {
        items.toPage(PageRequest.of(9, 2)).content.shouldBeEmpty()
    }
}
