package hu.bsstudio.bssweb.member.service

import hu.bsstudio.bssweb.fileserver.client.FileApiClient
import hu.bsstudio.bssweb.fileserver.model.FileUpdate
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.UpdateMember
import java.util.Optional
import java.util.UUID

class FileUpdatingMemberService(private val server: MemberService, private val fileClient: FileApiClient) : MemberService {

    override fun findAllMembers(): List<Member> {
        return this.server.findAllMembers()
    }

    override fun insertMember(createMember: CreateMember): Member {
        return this.server.insertMember(createMember)
            .also { fileClient.createMemberFolder(FileUpdate(it.id, it.url)) }
    }

    override fun archiveMembers(memberIds: List<UUID>, archive: Boolean): List<UUID> {
        return this.server.archiveMembers(memberIds)
    }

    override fun findMemberById(memberIds: UUID): Optional<Member> {
        return this.server.findMemberById(memberIds)
    }

    override fun updateMember(memberId: UUID, updateMember: UpdateMember): Optional<Member> {
        return this.server.updateMember(memberId, updateMember)
            .map { fileClient.updateMemberFolder(FileUpdate(it.id, it.url)); it }
    }

    override fun removeMember(memberId: UUID) {
        return this.server.removeMember(memberId)
    }
}
