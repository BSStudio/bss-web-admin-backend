package hu.bsstudio.bssweb.event.repository

import hu.bsstudio.bssweb.event.entity.EventEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : CrudRepository<EventEntity, String>
