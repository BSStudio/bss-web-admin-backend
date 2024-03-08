package hu.bsstudio.bssweb.label.repository

import hu.bsstudio.bssweb.label.entity.LabelEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface LabelRepository : JpaRepository<LabelEntity, UUID> {
    fun findAllByName(names: List<String>): List<LabelEntity>
}
