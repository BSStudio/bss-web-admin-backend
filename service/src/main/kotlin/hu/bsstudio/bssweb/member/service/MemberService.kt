package hu.bsstudio.bssweb.member.service

import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import java.util.Optional

interface MemberService {
    fun getAllMembers(): List<Member>
    fun createMember(member: CreateMember): Member
    fun archiveMembers(memberIds: List<String>, unArchive: Boolean): List<String>
    fun removeMember(memberId: String)
    fun getMemberById(memberIds: String): Optional<Member>
    fun updateMember(member: UpdateMember): Optional<Member>
}
