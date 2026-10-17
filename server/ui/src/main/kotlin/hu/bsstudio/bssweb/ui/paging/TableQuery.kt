package hu.bsstudio.bssweb.ui.paging

import org.springframework.web.servlet.mvc.support.RedirectAttributes

/** Items per page when the address does not say. */
const val PAGE_SIZE = 25

/**
 * What a table shows: the search, the page, the page size and the order. Bulk action forms send it along
 * (the hidden fields under each table), so the list comes back the same after the action.
 */
data class TableQuery(
    val q: String = "",
    val page: Int = 0,
    val size: Int = PAGE_SIZE,
    val sort: String = "",
)

/** Adds the table's search, page, page size and order to the redirect's address. */
fun RedirectAttributes.backTo(table: TableQuery) {
    addAttribute("q", table.q)
    addAttribute("page", table.page)
    addAttribute("size", table.size)
    addAttribute("sort", table.sort)
}
