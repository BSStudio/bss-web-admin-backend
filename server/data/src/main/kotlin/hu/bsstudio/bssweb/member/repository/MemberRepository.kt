package hu.bsstudio.bssweb.member.repository

import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface MemberRepository : JpaRepository<DetailedMemberEntity, UUID>
