package hu.bsstudio.bssweb.member.operation

import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID

interface MemberOperation {
    @GetMapping("/api/v1/member")
    fun getAllMembers(): ResponseEntity<List<Member>>

    @PostMapping("/api/v1/member")
    fun createMember(
        @RequestBody member: CreateMember,
    ): ResponseEntity<Member>

    @PutMapping("/api/v1/member/{memberId}")
    fun updateMember(
        @PathVariable memberId: UUID,
        @RequestBody updateMember: UpdateMember,
    ): ResponseEntity<Member>

    @PutMapping("/api/v1/member/archive")
    fun archiveMembers(
        @RequestParam memberIds: List<UUID>,
        @RequestParam(defaultValue = "true") archive: Boolean,
    ): ResponseEntity<List<UUID>>

    @GetMapping("/api/v1/member/{memberId}")
    fun getMemberById(
        @PathVariable memberId: UUID,
    ): ResponseEntity<Member>

    @DeleteMapping("/api/v1/member/{memberId}")
    fun removeMember(
        @PathVariable memberId: UUID,
    ): ResponseEntity<Void>
}
