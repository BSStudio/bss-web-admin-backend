package hu.bsstudio.bssweb.member.controller

import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import hu.bsstudio.bssweb.member.operation.MemberOperation
import hu.bsstudio.bssweb.member.service.MemberService
import hu.bsstudio.bssweb.revision.controller.restoring
import hu.bsstudio.bssweb.revision.model.RevisionInfo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.UUID

@RestController
class MemberController(
    val service: MemberService,
) : MemberOperation {
    override fun getAllMembers(): ResponseEntity<List<Member>> =
        service
            .findAllMembers()
            .let { ResponseEntity.ok(it) }

    override fun createMember(member: CreateMember): ResponseEntity<Member> =
        service
            .insertMember(member)
            .let { ResponseEntity.created(locationUri(it.id)).body(it) }

    override fun updateMember(
        memberId: UUID,
        updateMember: UpdateMember,
    ): ResponseEntity<Member> =
        service
            .updateMember(memberId, updateMember)
            .let { ResponseEntity.of(it) }

    override fun archiveMembers(
        memberIds: List<UUID>,
        archive: Boolean,
    ): ResponseEntity<List<UUID>> =
        service
            .archiveMembers(memberIds, archive)
            .let { ResponseEntity.ok(it) }

    override fun getMemberById(memberId: UUID): ResponseEntity<Member> =
        service
            .findMemberById(memberId)
            .let { ResponseEntity.of(it) }

    override fun removeMember(memberId: UUID): ResponseEntity<Unit> {
        service.removeMember(memberId)
        return ResponseEntity.noContent().build()
    }

    override fun getDeletedMembers(pageable: Pageable): ResponseEntity<Page<Member>> =
        service
            .findDeletedMembers(pageable)
            .let { ResponseEntity.ok(it) }

    override fun restoreMember(memberId: UUID): ResponseEntity<Member> = restoring { service.restoreMember(memberId) }

    override fun getMemberRevisions(memberId: UUID): ResponseEntity<List<RevisionInfo>> =
        service
            .findMemberRevisions(memberId)
            .let { ResponseEntity.ok(it) }

    override fun getMemberRevision(
        memberId: UUID,
        revision: Long,
    ): ResponseEntity<Member> =
        service
            .findMemberRevision(memberId, revision)
            .let { ResponseEntity.of(it) }

    override fun restoreMemberRevision(
        memberId: UUID,
        revision: Long,
    ): ResponseEntity<Member> = restoring { service.restoreMemberRevision(memberId, revision) }

    private fun locationUri(id: UUID) =
        ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .toUri()
}
