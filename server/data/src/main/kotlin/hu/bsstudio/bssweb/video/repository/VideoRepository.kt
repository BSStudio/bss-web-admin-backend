package hu.bsstudio.bssweb.video.repository

import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface VideoRepository : CrudRepository<SimpleVideoEntity, UUID> {
    fun findAll(pageable: Pageable): Page<SimpleVideoEntity>
}
