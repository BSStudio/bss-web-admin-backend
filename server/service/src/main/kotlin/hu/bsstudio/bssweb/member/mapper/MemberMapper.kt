package hu.bsstudio.bssweb.member.mapper

import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import hu.bsstudio.bssweb.member.entity.SimpleMemberEntity
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.SimpleMember
import hu.bsstudio.bssweb.member.model.UpdateMember

class MemberMapper {

    fun entityToModel(entity: DetailedMemberEntity) = Member(
        id = entity.id,
        url = entity.url,
        name = entity.name,
        nickname = entity.nickname,
        description = entity.description,
        joinedAt = entity.joinedAt,
        role = entity.role,
        status = entity.status,
        archived = entity.archived
    )

    fun entityToModel(entity: SimpleMemberEntity) = SimpleMember(
        id = entity.id,
        name = entity.name,
        nickname = entity.nickname
    )

    fun modelToEntity(model: CreateMember) = DetailedMemberEntity(
        url = model.url,
        name = model.name
    )

    fun updateToEntity(memberEntity: DetailedMemberEntity, updateMember: UpdateMember): DetailedMemberEntity {
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
