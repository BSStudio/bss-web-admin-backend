package hu.bsstudio.bssweb.member.mapper

import hu.bsstudio.bssweb.member.entity.MemberEntity
import hu.bsstudio.bssweb.member.model.CreateMember
import hu.bsstudio.bssweb.member.model.Member
import java.util.UUID

class MemberMapper(private val idGenerator: () -> UUID = UUID::randomUUID) {

    fun entityToModel(entity: MemberEntity): Member {
        return Member(
            id = entity.id,
            url = entity.url,
            name = entity.name,
            description = entity.description,
            joinedAt = entity.joinedAt,
            role = entity.role,
            status = entity.status,
            archived = entity.archived,
        )
    }

    fun modelToEntity(model: CreateMember): MemberEntity {
        return MemberEntity(id = idGenerator.invoke(), url = model.url, name = model.name)
    }
}
