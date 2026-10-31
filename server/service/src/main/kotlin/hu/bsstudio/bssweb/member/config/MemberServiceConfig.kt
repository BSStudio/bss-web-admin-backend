package hu.bsstudio.bssweb.member.config

import hu.bsstudio.bssweb.fileserver.client.FileApiClient
import hu.bsstudio.bssweb.member.mapper.MemberMapper
import hu.bsstudio.bssweb.member.repository.MemberRepository
import hu.bsstudio.bssweb.member.service.DefaultMemberService
import hu.bsstudio.bssweb.member.service.FileUpdatingMemberService
import hu.bsstudio.bssweb.member.service.MemberService
import hu.bsstudio.bssweb.revision.mapper.RevisionMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class MemberServiceConfig(
    private val repository: MemberRepository,
    private val fileClient: FileApiClient,
) {
    @Bean
    @Primary
    fun memberService(defaultMemberService: MemberService): MemberService = FileUpdatingMemberService(defaultMemberService, fileClient)

    @Bean
    fun defaultMemberService(
        memberMapper: MemberMapper,
        revisionMapper: RevisionMapper,
    ): MemberService = DefaultMemberService(repository, memberMapper, revisionMapper)

    @Bean
    fun memberMapper() = MemberMapper()
}
