package hu.bsstudio.bssweb.label.mapper

import hu.bsstudio.bssweb.label.entity.LabelEntity
import hu.bsstudio.bssweb.label.model.CreateLabel
import hu.bsstudio.bssweb.label.model.Label

class LabelMapper {
    fun entityToModel(entity: LabelEntity): Label {
        return Label(
            id = entity.id,
            name = entity.name,
            description = entity.description,
        )
    }

    fun modelToEntity(model: CreateLabel): LabelEntity {
        return LabelEntity(
            name = model.name,
            description = model.description,
        )
    }
}
