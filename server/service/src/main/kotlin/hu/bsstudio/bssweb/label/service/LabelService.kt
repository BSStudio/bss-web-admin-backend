package hu.bsstudio.bssweb.label.service

import hu.bsstudio.bssweb.label.model.CreateLabel
import hu.bsstudio.bssweb.label.model.Label
import java.util.UUID

interface LabelService {
    fun findAllLabels(): List<Label>

    fun insertLabel(model: CreateLabel): Label

    fun removeLabel(id: UUID)
}
