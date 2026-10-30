package hu.bsstudio.bssweb.event.repository

import hu.bsstudio.bssweb.event.entity.EventEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface EventRepository : JpaRepository<EventEntity, UUID>
