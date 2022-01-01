package hu.bsstudio.bssweb.member.mapper

import hu.bsstudio.bssweb.member.entity.MemberEntity
import hu.bsstudio.bssweb.member.model.Member

class MemberMapper {

    fun entityToModel(entity: MemberEntity): Member {
        return Member(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            imageUrl = entity.imageUrl,
            role = entity.role,
            status = entity.status,
        )
    }

    fun modelToEntity(model: Member): MemberEntity {
        return MemberEntity(
            id = model.id,
            name = model.name,
            description = model.description,
            imageUrl = model.imageUrl,
            role = model.role,
            status = model.status,
        )
    }
}
