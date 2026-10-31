package hu.bsstudio.bssweb.member.service

import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import hu.bsstudio.bssweb.revision.model.RevisionInfo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional
import java.util.UUID

interface MemberService {
    fun findAllMembers(): List<Member>

    fun insertMember(createMember: CreateMember): Member

    fun archiveMembers(
        memberIds: List<UUID>,
        archive: Boolean,
    ): List<UUID>

    /** Gives the members [status], and returns the ids of the ones it found, like [archiveMembers]. */
    fun changeMemberStatus(
        memberIds: List<UUID>,
        status: MemberStatus,
    ): List<UUID>

    fun findMemberById(memberIds: UUID): Optional<Member>

    fun updateMember(
        memberId: UUID,
        updateMember: UpdateMember,
    ): Optional<Member>

    fun removeMember(memberId: UUID)

    fun findDeletedMembers(pageable: Pageable): Page<Member>

    /** Brings back a deleted member, or returns empty if no deleted member has the id. */
    fun restoreMember(memberId: UUID): Optional<Member>

    /** The changes of the member, newest first, including the ones made while it was deleted. */
    fun findMemberRevisions(memberId: UUID): List<RevisionInfo>

    /** The member as it was at [revision]. */
    fun findMemberRevision(
        memberId: UUID,
        revision: Long,
    ): Optional<Member>

    /** Gives the member the fields it had at [revision]. A deleted member has to be restored first. */
    fun restoreMemberRevision(
        memberId: UUID,
        revision: Long,
    ): Optional<Member>
}
