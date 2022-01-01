package hu.bsstudio.bssweb.member.service

import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.entity.MemberEntity
import hu.bsstudio.bssweb.member.mapper.MemberMapper
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import hu.bsstudio.bssweb.member.repository.MemberRepository
import java.util.Optional
import java.util.UUID

class DefaultMemberService(val repository: MemberRepository) : MemberService {

    internal val mapper: MemberMapper = MemberMapper()

    override fun createMember(member: CreateMember): Member {
        val entity = MemberEntity(
            id = UUID.randomUUID(),
            name = member.name,
            description = "",
            imageUrl = null,
            role = "",
            status = MemberStatus.MEMBER_CANDIDATE_CANDIDATE
        )
        val createdEntity = repository.save(entity)
        return mapper.entityToModel(createdEntity)
    }

    override fun updateMember(member: UpdateMember): Member? {
        return null
    }

    override fun getMemberById(id: UUID): Optional<Member> {
        return repository.findById(id)
            .map(mapper::entityToModel)
    }

    override fun getAllMembers(): List<Member> {
        return repository.findAll()
            .map(mapper::entityToModel)
    }
}
