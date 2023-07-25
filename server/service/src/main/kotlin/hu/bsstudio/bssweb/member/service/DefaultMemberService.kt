package hu.bsstudio.bssweb.member.service

import hu.bsstudio.bssweb.member.entity.MemberEntity
import hu.bsstudio.bssweb.member.mapper.MemberMapper
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import hu.bsstudio.bssweb.member.repository.MemberRepository
import java.util.Optional
import java.util.UUID

class DefaultMemberService(
    private val repository: MemberRepository,
    private val mapper: MemberMapper
) : MemberService {

    override fun findAllMembers(): List<Member> {
        return repository.findAll()
            .map(mapper::entityToModel)
    }

    override fun insertMember(createMember: CreateMember): Member {
        return createMember
            .let(mapper::modelToEntity)
            .let(repository::save)
            .let(mapper::entityToModel)
    }

    override fun archiveMembers(memberIds: List<UUID>, archive: Boolean): List<UUID> {
        return repository.findAllById(memberIds)
            .map { it.archived = archive; it }
            .map(repository::save)
            .map(MemberEntity::id)
    }

    override fun updateMember(memberId: UUID, updateMember: UpdateMember): Optional<Member> {
        return repository.findById(memberId)
            .map { updateMember(it, updateMember) }
            .map(repository::save)
            .map(mapper::entityToModel)
    }

    override fun findMemberById(memberIds: UUID): Optional<Member> {
        return repository.findById(memberIds)
            .map(mapper::entityToModel)
    }

    override fun removeMember(memberId: UUID) = repository.deleteById(memberId)

    private fun updateMember(memberEntity: MemberEntity, updateMember: UpdateMember): MemberEntity {
        memberEntity.url = updateMember.url
        memberEntity.name = updateMember.name
        memberEntity.nickname = updateMember.nickname
        memberEntity.description = updateMember.description
        memberEntity.joinedAt = updateMember.joinedAt
        memberEntity.role = updateMember.role
        memberEntity.status = updateMember.status
        memberEntity.archived = updateMember.archived
        return memberEntity
    }
}
