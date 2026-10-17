package hu.bsstudio.bssweb.ui.paging

import java.text.Normalizer

private val ACCENTS = Regex("\\p{M}+")
private val SPACES = Regex("\\s+")

/** Lowercase and without accents, so "golya" finds "Gólyatábor". */
private fun String.searchable() = Normalizer.normalize(this, Normalizer.Form.NFD).replace(ACCENTS, "").lowercase()

/**
 * Keeps the items where every word of [query] appears in one of their [texts], ignoring case and accents.
 * A blank query keeps everything. The services have no search, so this filters the lists they return.
 */
fun <T> List<T>.search(
    query: String,
    texts: (T) -> List<String>,
): List<T> {
    val words = query.searchable().split(SPACES).filter(String::isNotEmpty)
    return filter { item ->
        val text = texts(item).joinToString(" ").searchable()
        words.all(text::contains)
    }
}
