package hu.bsstudio.bssweb.member.service

import hu.bsstudio.bssweb.fileserver.client.FileApiClient
import hu.bsstudio.bssweb.fileserver.model.MemberFileUpdate
import hu.bsstudio.bssweb.member.common.MemberStatus
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import hu.bsstudio.bssweb.revision.model.RevisionInfo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.Optional
import java.util.UUID

class FileUpdatingMemberService(
    private val server: MemberService,
    private val fileClient: FileApiClient,
) : MemberService {
    override fun findAllMembers(): List<Member> = this.server.findAllMembers()

    override fun insertMember(createMember: CreateMember): Member =
        this.server
            .insertMember(createMember)
            .also { fileClient.createMemberFolder(MemberFileUpdate(it.id, it.url)) }

    override fun archiveMembers(
        memberIds: List<UUID>,
        archive: Boolean,
    ): List<UUID> = this.server.archiveMembers(memberIds, archive)

    override fun changeMemberStatus(
        memberIds: List<UUID>,
        status: MemberStatus,
    ): List<UUID> = this.server.changeMemberStatus(memberIds, status)

    override fun findMemberById(memberIds: UUID): Optional<Member> = this.server.findMemberById(memberIds)

    override fun updateMember(
        memberId: UUID,
        updateMember: UpdateMember,
    ): Optional<Member> =
        this.server
            .updateMember(memberId, updateMember)
            .map {
                fileClient.updateMemberFolder(MemberFileUpdate(it.id, it.url))
                it
            }

    override fun removeMember(memberId: UUID) = this.server.removeMember(memberId)

    override fun findDeletedMembers(pageable: Pageable): Page<Member> = this.server.findDeletedMembers(pageable)

    override fun restoreMember(memberId: UUID): Optional<Member> = this.server.restoreMember(memberId).map(::updateFolder)

    override fun findMemberRevisions(memberId: UUID): List<RevisionInfo> = this.server.findMemberRevisions(memberId)

    override fun findMemberRevision(
        memberId: UUID,
        revision: Long,
    ): Optional<Member> = this.server.findMemberRevision(memberId, revision)

    override fun restoreMemberRevision(
        memberId: UUID,
        revision: Long,
    ): Optional<Member> = this.server.restoreMemberRevision(memberId, revision).map(::updateFolder)

    private fun updateFolder(member: Member): Member {
        fileClient.updateMemberFolder(MemberFileUpdate(member.id, member.url))
        return member
    }
}
