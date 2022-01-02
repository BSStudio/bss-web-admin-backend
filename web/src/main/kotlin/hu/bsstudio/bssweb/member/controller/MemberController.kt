package hu.bsstudio.bssweb.member.controller

import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import hu.bsstudio.bssweb.member.service.MemberService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/member")
class MemberController(val service: MemberService) {

    @GetMapping
    fun getAllMembers(): ResponseEntity<List<Member>> {
        return service.getAllMembers()
            .let { ResponseEntity.ok(it) }
    }

    @PostMapping
    fun createMember(@RequestBody member: CreateMember): ResponseEntity<Member> {
        return service.createMember(member)
            .let { ResponseEntity(it, HttpStatus.CREATED) }
    }

    @PutMapping("")
    fun updateMember(@RequestBody member: UpdateMember): ResponseEntity<Member> {
        return service.updateMember(member)
            .let { ResponseEntity.of(it) }
    }

    @PutMapping("/archive")
    fun archiveMembers(
        @RequestParam memberIds: List<String>,
        @RequestParam unArchive: Boolean
    ): ResponseEntity<List<String>> {
        return service.archiveMembers(memberIds, unArchive)
            .let { ResponseEntity.ok(it) }
    }

    @GetMapping("/{memberId}")
    fun getMemberById(@PathVariable memberId: String): ResponseEntity<Member> {
        return service.getMemberById(memberId)
            .let { ResponseEntity.of(it) }
    }

    @DeleteMapping("/{memberId}")
    fun removeMember(@PathVariable memberId: String) {
        service.removeMember(memberId)
    }
}
