package hu.bsstudio.bssweb.member.service

import hu.bsstudio.bssweb.fileserver.client.FileApiClient
import hu.bsstudio.bssweb.fileserver.model.MemberFileUpdate
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
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
    ): List<UUID> = this.server.archiveMembers(memberIds, true)

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
}
