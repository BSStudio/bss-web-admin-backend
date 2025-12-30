package hu.bsstudio.bssweb.member.operation

import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.DeleteExchange
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange
import org.springframework.web.service.annotation.PutExchange
import java.util.UUID

@HttpExchange("/api/v1/member")
interface MemberOperation {
    @GetExchange
    fun getAllMembers(): ResponseEntity<List<Member>>

    @PostExchange
    fun createMember(
        @RequestBody member: CreateMember,
    ): ResponseEntity<Member>

    @PutExchange("/{memberId}")
    fun updateMember(
        @PathVariable memberId: UUID,
        @RequestBody updateMember: UpdateMember,
    ): ResponseEntity<Member>

    @PutExchange("/archive")
    fun archiveMembers(
        @RequestParam memberIds: List<UUID>,
        @RequestParam(defaultValue = "true") archive: Boolean,
    ): ResponseEntity<List<UUID>>

    @GetExchange("/{memberId}")
    fun getMemberById(
        @PathVariable memberId: UUID,
    ): ResponseEntity<Member>

    @DeleteExchange("/{memberId}")
    fun removeMember(
        @PathVariable memberId: UUID,
    ): ResponseEntity<Unit>
}
