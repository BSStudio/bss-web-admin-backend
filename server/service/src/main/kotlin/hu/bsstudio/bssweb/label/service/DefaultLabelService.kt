package hu.bsstudio.bssweb.label.service

import hu.bsstudio.bssweb.label.mapper.LabelMapper
import hu.bsstudio.bssweb.label.model.CreateLabel
import hu.bsstudio.bssweb.label.model.Label
import hu.bsstudio.bssweb.label.repository.LabelRepository
import java.util.UUID

class DefaultLabelService(
    private val repository: LabelRepository,
    private val mapper: LabelMapper,
) : LabelService {
    override fun findAllLabels(): List<Label> {
        return repository.findAll()
            .map { mapper.toModel(it) }
    }

    override fun insertLabel(model: CreateLabel): Label {
        return mapper.toEntity(model)
            .let { repository.save(it) }
            .let { mapper.toModel(it) }
    }

    override fun removeLabel(id: UUID) {
        repository.deleteById(id)
    }
}
