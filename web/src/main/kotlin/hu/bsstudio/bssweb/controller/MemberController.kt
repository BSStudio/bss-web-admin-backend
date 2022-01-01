package hu.bsstudio.bssweb.controller

import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import hu.bsstudio.bssweb.member.service.MemberService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/member")
class MemberController(val service: MemberService) {

    @GetMapping
    fun getAllMembers(): ResponseEntity<List<Member>> {
        val members = service.getAllMembers()
        return ResponseEntity.ok(members)
    }

    @GetMapping("/{memberId}")
    fun getMemberById(@PathVariable memberId: String): ResponseEntity<Member> {
        val id = UUID.fromString(memberId)
        val member = service.getMemberById(id)
        return ResponseEntity.of(member)
    }

    @PostMapping
    fun createMember(@RequestBody member: CreateMember): ResponseEntity<Member> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(service.createMember(member))
    }

    @PutMapping
    fun updateMember(@RequestBody member: UpdateMember): ResponseEntity<Member> {
        val updatedMember = service.updateMember(member)
        return ResponseEntity.ok(updatedMember)
    }
}
