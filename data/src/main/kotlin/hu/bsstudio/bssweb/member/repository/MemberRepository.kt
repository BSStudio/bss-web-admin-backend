package hu.bsstudio.bssweb.member.repository

import hu.bsstudio.bssweb.member.entity.MemberEntity
import org.springframework.data.repository.CrudRepository

interface MemberRepository : CrudRepository<MemberEntity, String>
