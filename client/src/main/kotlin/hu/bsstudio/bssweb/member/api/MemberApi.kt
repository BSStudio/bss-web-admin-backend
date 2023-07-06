package hu.bsstudio.bssweb.member.api

import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping("/api/v1/member")
interface MemberApi {

    @GetMapping
    fun getAllMembers(): ResponseEntity<List<Member>>

    @PostMapping
    fun createMember(@RequestBody member: CreateMember): ResponseEntity<Member>

    @PutMapping("/{memberId}")
    fun updateMember(@PathVariable memberId: UUID, @RequestBody updateMember: UpdateMember): ResponseEntity<Member>

    @PutMapping("/archive")
    fun archiveMembers(@RequestParam memberIds: List<UUID>, @RequestParam archive: Boolean = true): ResponseEntity<List<UUID>>

    @GetMapping("/{memberId}")
    fun getMemberById(@PathVariable memberId: UUID): ResponseEntity<Member>

    @DeleteMapping("/{memberId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeMember(@PathVariable memberId: UUID): ResponseEntity<Void>
}
