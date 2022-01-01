package hu.bsstudio.bssweb.member.service

import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import java.util.Optional
import java.util.UUID

interface MemberService {
    fun createMember(member: CreateMember): Member?
    fun updateMember(member: UpdateMember): Member?
    fun getMemberById(id: UUID): Optional<Member>
    fun getAllMembers(): List<Member>
}
