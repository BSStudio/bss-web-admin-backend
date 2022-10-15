package hu.bsstudio.bssweb.eventvideo.repository

import hu.bsstudio.bssweb.eventvideo.entity.EventVideoEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EventVideoRepository : CrudRepository<EventVideoEntity, EventVideoEntity>
