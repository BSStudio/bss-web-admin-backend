package hu.bsstudio.bssweb.eventvideo.repository

import hu.bsstudio.bssweb.eventvideo.entity.EventVideoEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface EventVideoRepository : CrudRepository<EventVideoEntity, UUID>
