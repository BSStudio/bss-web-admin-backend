package hu.bsstudio.bssweb.member.repository

import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
interface MemberRepository : JpaRepository<DetailedMemberEntity, UUID> {
    /**
     * Sets the status of the members in one statement, and returns how many it changed.
     * A bulk update skips `@UpdateTimestamp`, so [updatedAt] is set here.
     */
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update DetailedMemberEntity m set m.status = :status, m.updatedAt = :updatedAt where m.id in :memberIds")
    fun updateStatus(
        @Param("memberIds") memberIds: Collection<UUID>,
        @Param("status") status: MemberStatus,
        @Param("updatedAt") updatedAt: Instant,
    ): Int
}
