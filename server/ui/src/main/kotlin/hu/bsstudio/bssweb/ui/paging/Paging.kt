package hu.bsstudio.bssweb.ui.paging

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

/** Pages a list in memory, for services that return everything at once. */
fun <T : Any> List<T>.toPage(pageable: Pageable): Page<T> {
    val from = pageable.offset.toInt().coerceAtMost(size)
    val to = (from + pageable.pageSize).coerceAtMost(size)
    return PageImpl(subList(from, to), pageable, size.toLong())
}
