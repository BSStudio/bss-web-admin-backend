package hu.bsstudio.bssweb.member.service

import hu.bsstudio.bssweb.member.entity.MemberEntity
import hu.bsstudio.bssweb.member.mapper.MemberMapper
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import hu.bsstudio.bssweb.member.repository.MemberRepository
import java.util.Optional

class DefaultMemberService(val repository: MemberRepository) : MemberService {

    internal val mapper: MemberMapper = MemberMapper()

    override fun getAllMembers(): List<Member> {
        return repository.findAll()
            .map(mapper::entityToModel)
    }

    override fun createMember(member: CreateMember): Member {
        return MemberEntity(id = member.id, name = member.name)
            .let(repository::save)
            .let(mapper::entityToModel)
    }

    override fun archiveMembers(memberIds: List<String>, unArchive: Boolean): List<String> {
        return repository.findAllById(memberIds)
            .map { it.copy(archived = !unArchive) }
            .map(repository::save)
            .map(MemberEntity::id)
    }

    override fun updateMember(memberId: String, updateMember: UpdateMember): Optional<Member> {
        return repository.findById(memberId)
            .map { updateMember(it, updateMember) }
            .map(repository::save)
            .map(mapper::entityToModel)
    }

    override fun getMemberById(memberIds: String): Optional<Member> {
        return repository.findById(memberIds)
            .map(mapper::entityToModel)
    }

    override fun removeMember(memberId: String) {
        repository.deleteById(memberId)
    }

    private fun updateMember(memberEntity: MemberEntity, updateMember: UpdateMember): MemberEntity {
        return memberEntity.copy(
            name = updateMember.name,
            description = updateMember.description,
            imageUrl = updateMember.imageUrl,
            joinedAt = updateMember.joinedAt,
            role = updateMember.role,
            status = updateMember.status,
            archived = updateMember.archived,
        )
    }
}
