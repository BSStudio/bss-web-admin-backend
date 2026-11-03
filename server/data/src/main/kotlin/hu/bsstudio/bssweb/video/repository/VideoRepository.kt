package hu.bsstudio.bssweb.video.repository

import hu.bsstudio.bssweb.video.entity.VideoEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.history.RevisionRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

/** Every query leaves out the deleted videos, except the two that look for them. */
@Repository
interface VideoRepository :
    JpaRepository<VideoEntity, UUID>,
    RevisionRepository<VideoEntity, UUID, Long> {
    @Query(
        value = "select * from video where deleted_at is not null",
        countQuery = "select count(*) from video where deleted_at is not null",
        nativeQuery = true,
    )
    fun findAllDeleted(pageable: Pageable): Page<VideoEntity>

    @Query(value = "select * from video where id = :id and deleted_at is not null", nativeQuery = true)
    fun findDeletedById(
        @Param("id") id: UUID,
    ): Optional<VideoEntity>
}
