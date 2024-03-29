package hu.bsstudio.bssweb.event.repository

import hu.bsstudio.bssweb.event.entity.DetailedEventEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DetailedEventRepository : JpaRepository<DetailedEventEntity, UUID>
