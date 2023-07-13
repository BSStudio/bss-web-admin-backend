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
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID

@RequestMapping("/api/v1/member")
interface MemberOperation {

    @GetMapping
    fun getAllMembers(): ResponseEntity<List<Member>>

    @PostMapping
    fun createMember(@RequestBody member: CreateMember): ResponseEntity<Member>

    @PutMapping("/{memberId}")
    fun updateMember(@PathVariable memberId: UUID, @RequestBody updateMember: UpdateMember): ResponseEntity<Member>

    @PutMapping("/archive")
    fun archiveMembers(@RequestParam memberIds: List<UUID>, @RequestParam(defaultValue = "true") archive: Boolean): ResponseEntity<List<UUID>>

    @GetMapping("/{memberId}")
    fun getMemberById(@PathVariable memberId: UUID): ResponseEntity<Member>

    @DeleteMapping("/{memberId}")
    fun removeMember(@PathVariable memberId: UUID): ResponseEntity<Void>
}
