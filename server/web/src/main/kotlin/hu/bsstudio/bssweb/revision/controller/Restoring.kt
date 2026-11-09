package hu.bsstudio.bssweb.revision.controller

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.Optional

/**
 * Answers 404 when [restore] finds nothing, and 409 when it would give the item a title or URL that another item has
 * taken since.
 */
internal fun <T : Any> restoring(restore: () -> Optional<T>): ResponseEntity<T> =
    try {
        ResponseEntity.of(restore())
    } catch (_: DataIntegrityViolationException) {
        ResponseEntity.status(HttpStatus.CONFLICT).build()
    }
