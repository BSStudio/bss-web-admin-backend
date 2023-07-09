package hu.bsstudio.bssweb.member.controller

import hu.bsstudio.bssweb.member.api.MemberApi
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import hu.bsstudio.bssweb.member.service.MemberService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class MemberController(val service: MemberService) : MemberApi {

    override fun getAllMembers(): ResponseEntity<List<Member>> {
        return service.findAllMembers()
            .let { ResponseEntity.ok(it) }
    }

    override fun createMember(member: CreateMember): ResponseEntity<Member> {
        return service.insertMember(member)
            .let { ResponseEntity(it, HttpStatus.CREATED) }
    }

    override fun updateMember(memberId: UUID, updateMember: UpdateMember): ResponseEntity<Member> {
        return service.updateMember(memberId, updateMember)
            .let { ResponseEntity.of(it) }
    }

    override fun archiveMembers(memberIds: List<UUID>, archive: Boolean): ResponseEntity<List<UUID>> {
        return service.archiveMembers(memberIds, archive)
            .let { ResponseEntity.ok(it) }
    }

    override fun getMemberById(memberId: UUID): ResponseEntity<Member> {
        return service.findMemberById(memberId)
            .let { ResponseEntity.of(it) }
    }

    override fun removeMember(memberId: UUID): ResponseEntity<Void> {
        service.removeMember(memberId)
        return ResponseEntity.noContent().build()
    }
}
