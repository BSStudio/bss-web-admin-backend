package hu.bsstudio.bssweb.label.operation

import hu.bsstudio.bssweb.label.model.CreateLabel
import hu.bsstudio.bssweb.label.model.Label
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.service.annotation.DeleteExchange
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange
import java.util.*

@HttpExchange("/api/v1/label")
interface LabelOperation {
    @GetExchange
    fun getAllLabels(): ResponseEntity<List<Label>>

    @PostExchange
    fun createLabel(
        @RequestBody label: CreateLabel,
    ): ResponseEntity<Label>

    @DeleteExchange("/{labelId}")
    fun removeLabel(
        @PathVariable labelId: UUID,
    ): ResponseEntity<Unit>
}
