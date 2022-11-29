package hu.bsstudio.bssweb.member.config

import hu.bsstudio.bssweb.fileserver.client.FileApiClient
import hu.bsstudio.bssweb.member.mapper.MemberMapper
import hu.bsstudio.bssweb.member.repository.MemberRepository
import hu.bsstudio.bssweb.member.service.DefaultMemberService
import hu.bsstudio.bssweb.member.service.FileUpdatingMemberService
import hu.bsstudio.bssweb.member.service.MemberService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MemberServiceConfig(private val repository: MemberRepository,
                          private val fileClient: FileApiClient) {

    @Bean
    fun memberService(defaultMemberService: MemberService): MemberService {
        return FileUpdatingMemberService(defaultMemberService, fileClient)
    }

    @Bean
    fun defaultMemberService(memberMapper: MemberMapper): MemberService {
        return DefaultMemberService(repository, memberMapper)
    }

    @Bean
    fun memberMapper() = MemberMapper()
}
