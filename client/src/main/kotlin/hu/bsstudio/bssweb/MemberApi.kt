package hu.bsstudio.bssweb

import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import org.springframework.http.ResponseEntity
import java.util.UUID

interface MemberApi {
    @RequestMapping("GET /api/v1/member")
    fun getAllMembers(): ResponseEntity<List<Member>>

    @RequestMapping("POST /api/v1/member")
    fun postMember(createMember: CreateMember): ResponseEntity<Member>

    @RequestMapping("PUT /api/v1/member/{memberId}")
    fun updateMember(@Param memberId: UUID, updateMember: UpdateMember): ResponseEntity<Member>

    @RequestMapping("GET /api/v1/member/{memberId}")
    fun getMember(@Param memberId: UUID): ResponseEntity<Member>

    @RequestMapping("DELETE /api/v1/member/{memberId}")
    fun deleteMember(@Param memberId: UUID): ResponseEntity<Unit>
}
