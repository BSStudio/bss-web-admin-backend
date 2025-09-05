package hu.bsstudio.bssweb.label.operation

import hu.bsstudio.bssweb.label.model.CreateLabel
import hu.bsstudio.bssweb.label.model.Label
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.util.UUID

interface LabelOperation {
    @GetMapping("/api/v1/label")
    fun getAllLabels(): ResponseEntity<List<Label>>

    @PostMapping("/api/v1/label")
    fun createLabel(
        @RequestBody label: CreateLabel,
    ): ResponseEntity<Label>

    @DeleteMapping("/api/v1/label/{labelId}")
    fun removeLabel(
        @PathVariable labelId: UUID,
    ): ResponseEntity<Unit>
}
