package hu.bsstudio.bssweb.member.repository

import hu.bsstudio.bssweb.member.entity.MemberEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface MemberRepository : JpaRepository<MemberEntity, UUID>
