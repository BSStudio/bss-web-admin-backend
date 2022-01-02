package hu.bsstudio.bssweb.video.repository

import hu.bsstudio.bssweb.video.entity.VideoEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface VideoRepository : CrudRepository<VideoEntity, String> {
    fun findAll(pageable: Pageable): Page<VideoEntity>
}
