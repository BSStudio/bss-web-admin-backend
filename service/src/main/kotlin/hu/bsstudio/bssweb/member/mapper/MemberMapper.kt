package hu.bsstudio.bssweb.member.mapper

import hu.bsstudio.bssweb.member.entity.MemberEntity
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member

class MemberMapper {

    fun entityToModel(entity: MemberEntity): Member {
        return Member(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            imageUrl = entity.imageUrl,
            joinedAt = entity.joinedAt,
            role = entity.role,
            status = entity.status,
            archived = entity.archived,
        )
    }

    fun modelToEntity(model: CreateMember): MemberEntity {
        return MemberEntity(id = model.id, name = model.name)
    }
}
