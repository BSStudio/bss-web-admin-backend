package hu.bsstudio.bssweb.member.service

import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import java.util.Optional
import java.util.UUID

interface MemberService {
    fun findAllMembers(): List<Member>
    fun insertMember(createMember: CreateMember): Member
    fun archiveMembers(memberIds: List<UUID>, archive: Boolean = true): List<UUID>
    fun findMemberById(memberIds: UUID): Optional<Member>
    fun updateMember(memberId: UUID, updateMember: UpdateMember): Optional<Member>
    fun removeMember(memberId: UUID)
}
