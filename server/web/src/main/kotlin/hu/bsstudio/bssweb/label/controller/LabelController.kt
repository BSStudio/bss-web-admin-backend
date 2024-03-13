package hu.bsstudio.bssweb.label.controller

import hu.bsstudio.bssweb.label.model.CreateLabel
import hu.bsstudio.bssweb.label.model.Label
import hu.bsstudio.bssweb.label.operation.LabelOperation
import hu.bsstudio.bssweb.label.service.LabelService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.UUID

@RestController
class LabelController(val service: LabelService) : LabelOperation {
    override fun getAllLabels(): ResponseEntity<List<Label>> {
        return this.service.findAllLabels()
            .let { ResponseEntity.ok(it) }
    }

    override fun createLabel(label: CreateLabel): ResponseEntity<Label> {
        return this.service.insertLabel(label)
            .let { ResponseEntity.created(locationUri(it.id)).body(it) }
    }

    override fun removeLabel(labelId: UUID): ResponseEntity<Void> {
        this.service.removeLabel(labelId)
        return ResponseEntity.noContent().build()
    }

    private fun locationUri(id: UUID) =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .toUri()
}
