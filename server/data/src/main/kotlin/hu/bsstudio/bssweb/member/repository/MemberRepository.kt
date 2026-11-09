package hu.bsstudio.bssweb.member.repository

import hu.bsstudio.bssweb.member.entity.MemberEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.history.RevisionRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

/** Every query leaves out the deleted members, except the two that look for them. */
@Repository
interface MemberRepository :
    JpaRepository<MemberEntity, UUID>,
    RevisionRepository<MemberEntity, UUID, Long> {
    @Query(
        value = "select * from member where deleted_at is not null",
        countQuery = "select count(*) from member where deleted_at is not null",
        nativeQuery = true,
    )
    fun findAllDeleted(pageable: Pageable): Page<MemberEntity>

    @Query(value = "select * from member where id = :id and deleted_at is not null", nativeQuery = true)
    fun findDeletedById(
        @Param("id") id: UUID,
    ): Optional<MemberEntity>
}
