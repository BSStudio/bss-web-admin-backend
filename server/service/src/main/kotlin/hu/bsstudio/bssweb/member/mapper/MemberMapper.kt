package hu.bsstudio.bssweb.member.mapper

import hu.bsstudio.bssweb.member.entity.MemberEntity
import hu.bsstudio.bssweb.member.entity.SimpleMemberEntity
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import hu.bsstudio.bssweb.member.model.SimpleMember
import java.util.UUID

class MemberMapper(private val idGenerator: () -> UUID = UUID::randomUUID) {

    fun entityToModel(entity: MemberEntity) = Member(
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

    fun modelToEntity(model: CreateMember) = MemberEntity(
        id = idGenerator.invoke(),
        url = model.url,
        name = model.name
    )
}
