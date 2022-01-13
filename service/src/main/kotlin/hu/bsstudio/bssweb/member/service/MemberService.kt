package hu.bsstudio.bssweb.member.service

import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import java.util.Optional

interface MemberService {
    fun findAllMembers(): List<Member>
    fun insertMember(createMember: CreateMember): Member
    fun archiveMembers(memberIds: List<String>, archive: Boolean = true): List<String>
    fun findMemberById(memberIds: String): Optional<Member>
    fun updateMember(memberId: String, updateMember: UpdateMember): Optional<Member>
    fun removeMember(memberId: String)
}
