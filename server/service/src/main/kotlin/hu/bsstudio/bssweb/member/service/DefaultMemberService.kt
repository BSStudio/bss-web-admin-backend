package hu.bsstudio.bssweb.member.service

import hu.bsstudio.bssweb.member.mapper.MemberMapper
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import hu.bsstudio.bssweb.member.repository.MemberRepository
import java.util.Optional
import java.util.UUID

class DefaultMemberService(
    private val repository: MemberRepository,
    private val mapper: MemberMapper,
) : MemberService {
    override fun findAllMembers(): List<Member> =
        repository
            .findAll()
            .map(mapper::entityToModel)

    override fun insertMember(createMember: CreateMember): Member =
        createMember
            .let(mapper::modelToEntity)
            .let(repository::save)
            .let(mapper::entityToModel)

    override fun archiveMembers(
        memberIds: List<UUID>,
        archive: Boolean,
    ): List<UUID> =
        repository
            .findAllById(memberIds)
            .map {
                it.archived = archive
                it
            }.map(repository::save)
            .map { it.id }

    override fun updateMember(
        memberId: UUID,
        updateMember: UpdateMember,
    ): Optional<Member> =
        repository
            .findById(memberId)
            .map { mapper.updateToEntity(it, updateMember) }
            .map(repository::save)
            .map(mapper::entityToModel)

    override fun findMemberById(memberIds: UUID): Optional<Member> =
        repository
            .findById(memberIds)
            .map(mapper::entityToModel)

    override fun removeMember(memberId: UUID) = repository.deleteById(memberId)
}
