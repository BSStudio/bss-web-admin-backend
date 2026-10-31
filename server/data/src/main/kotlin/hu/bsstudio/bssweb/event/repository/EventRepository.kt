package hu.bsstudio.bssweb.event.repository

import hu.bsstudio.bssweb.event.entity.EventEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.history.RevisionRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

// a native query has to select the column of EventEntity.videoCount itself
private const val VIDEO_COUNT =
    "(select count(*) from event_video ev join video v on v.id = ev.video_id " +
        "where ev.event_id = event.id and v.deleted_at is null) as videoCount"

/** Every query leaves out the deleted events, except the two that look for them. */
@Repository
interface EventRepository :
    JpaRepository<EventEntity, UUID>,
    RevisionRepository<EventEntity, UUID, Long> {
    @Query(
        value = "select *, $VIDEO_COUNT from event where deleted_at is not null",
        countQuery = "select count(*) from event where deleted_at is not null",
        nativeQuery = true,
    )
    fun findAllDeleted(pageable: Pageable): Page<EventEntity>

    @Query(value = "select *, $VIDEO_COUNT from event where id = :id and deleted_at is not null", nativeQuery = true)
    fun findDeletedById(
        @Param("id") id: UUID,
    ): Optional<EventEntity>
}
