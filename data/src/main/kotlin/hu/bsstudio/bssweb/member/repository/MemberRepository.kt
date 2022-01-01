package hu.bsstudio.bssweb.member.repository

import hu.bsstudio.bssweb.member.entity.MemberEntity
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface MemberRepository : CrudRepository<MemberEntity, UUID>
