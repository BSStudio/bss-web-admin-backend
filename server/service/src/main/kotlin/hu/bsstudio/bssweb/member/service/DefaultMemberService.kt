package hu.bsstudio.bssweb.member.service

import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.entity.MemberEntity
import hu.bsstudio.bssweb.member.mapper.MemberMapper
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import hu.bsstudio.bssweb.member.repository.MemberRepository
import hu.bsstudio.bssweb.revision.mapper.RevisionMapper
import hu.bsstudio.bssweb.revision.model.RevisionInfo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.Optional
import java.util.UUID

// each method is one transaction, so a bulk change is all or nothing
@Transactional
class DefaultMemberService(
    private val repository: MemberRepository,
    private val mapper: MemberMapper,
    private val revisionMapper: RevisionMapper,
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

    override fun changeMemberStatus(
        memberIds: List<UUID>,
        status: MemberStatus,
    ): List<UUID> =
        repository
            .findAllById(memberIds)
            .map {
                it.status = status
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

    override fun removeMember(memberId: UUID) =
        repository.findById(memberId).ifPresent {
            it.deletedAt = Instant.now()
            repository.save(it)
        }

    override fun findDeletedMembers(pageable: Pageable): Page<Member> =
        repository
            .findAllDeleted(pageable)
            .map(mapper::entityToModel)

    override fun restoreMember(memberId: UUID): Optional<Member> =
        repository
            .findDeletedById(memberId)
            .map {
                it.deletedAt = null
                it
            }.map(repository::save)
            .map(mapper::entityToModel)

    override fun findMemberRevisions(memberId: UUID): List<RevisionInfo> =
        revisionMapper.revisionsToModel(repository.findRevisions(memberId), MemberEntity::deletedAt)

    override fun findMemberRevision(
        memberId: UUID,
        revision: Long,
    ): Optional<Member> =
        repository
            .findRevision(memberId, revision)
            .map { mapper.entityToModel(it.entity) }

    override fun restoreMemberRevision(
        memberId: UUID,
        revision: Long,
    ): Optional<Member> =
        repository
            .findRevision(memberId, revision)
            .flatMap { updateMember(memberId, mapper.entityToUpdate(it.entity)) }
}
