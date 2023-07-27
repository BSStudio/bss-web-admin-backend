package hu.bsstudio.bssweb.member.repository

import hu.bsstudio.bssweb.member.entity.DetailedMemberEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MemberRepository : JpaRepository<DetailedMemberEntity, UUID>
