package hu.bsstudio.bssweb.event.repository

import hu.bsstudio.bssweb.event.entity.SimpleEventEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
interface SimpleEventRepository : JpaRepository<SimpleEventEntity, UUID> {
    /**
     * Shows or hides the events in one statement, without touching their other fields, and returns how many it
     * changed. A bulk update skips `@UpdateTimestamp`, so [updatedAt] is set here.
     */
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update SimpleEventEntity e set e.visible = :visible, e.updatedAt = :updatedAt where e.id in :eventIds")
    fun updateVisibility(
        @Param("eventIds") eventIds: Collection<UUID>,
        @Param("visible") visible: Boolean,
        @Param("updatedAt") updatedAt: Instant,
    ): Int
}
