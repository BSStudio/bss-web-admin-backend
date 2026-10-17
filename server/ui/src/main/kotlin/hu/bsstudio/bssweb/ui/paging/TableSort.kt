package hu.bsstudio.bssweb.ui.paging

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.text.Collator
import java.util.Locale

private val HUNGARIAN: Collator = Collator.getInstance(Locale.forLanguageTag("hu"))

/** Orders text the Hungarian way (A, Á, B, …), whatever the page language: names and titles are Hungarian. */
fun <T> byText(text: (T) -> String): Comparator<T> = compareBy(HUNGARIAN, text)

/**
 * The columns a table can be sorted by, and how to compare two rows by each of them.
 * A click on a column header asks for `sort=<column>,asc` or `sort=<column>,desc`; any other order,
 * or none, gets the table's [default]. Rows that tie keep the default order.
 */
class TableSort<T>(
    private val default: Sort,
    private val columns: Map<String, Comparator<T>>,
) {
    /** The page to load from the database, in the requested order. */
    fun page(pageable: Pageable): Pageable = PageRequest.of(pageable.pageNumber, pageable.pageSize, withTies(requested(pageable)))

    /** Sorts a whole list in memory, in the requested order. */
    fun sort(
        items: List<T>,
        pageable: Pageable,
    ): List<T> = items.sortedWith(comparator(withTies(requested(pageable))))

    /** What the column headers show and link to. */
    fun header(pageable: Pageable): SortHeader {
        val sort = requested(pageable)
        val first = sort.first()
        return SortHeader(first.property, first.isAscending, chosen = sort != default)
    }

    private fun requested(pageable: Pageable): Sort =
        pageable.sort.takeIf { sort -> sort.isSorted && sort.all { it.property in columns } } ?: default

    private fun withTies(sort: Sort): Sort = Sort.by(sort.toList() + default.filter { sort.getOrderFor(it.property) == null })

    private fun comparator(sort: Sort): Comparator<T> =
        sort
            .map { order -> columns.getValue(order.property).let { if (order.isAscending) it else it.reversed() } }
            .reduce { first, then -> first.thenComparing(then) }
}

/** The order a table is shown in, as its column headers need it. */
class SortHeader(
    private val property: String,
    private val ascending: Boolean,
    chosen: Boolean,
) {
    /** The `sort` value that keeps this order in links and forms; empty for the default order. */
    val param = if (chosen) "$property,${direction(ascending)}" else ""

    /** The `sort` value a column header links to: the other direction for the current column, else ascending. */
    fun next(column: String) = "$column,${direction(column != property || !ascending)}"

    /** The `aria-sort` of a column header; only the current column has one. */
    fun aria(column: String) =
        when {
            column != property -> null
            ascending -> "ascending"
            else -> "descending"
        }

    private fun direction(ascending: Boolean) = if (ascending) "asc" else "desc"
}
