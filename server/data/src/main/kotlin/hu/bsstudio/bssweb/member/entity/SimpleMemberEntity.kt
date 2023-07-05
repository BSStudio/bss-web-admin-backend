package hu.bsstudio.bssweb.member.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "member")
data class SimpleMemberEntity(
    @Id
    var id: UUID,
    var name: String,
    var nickname: String
)
