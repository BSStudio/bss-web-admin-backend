package hu.bsstudio.bssweb.member.config

import hu.bsstudio.bssweb.member.repository.MemberRepository
import hu.bsstudio.bssweb.member.service.DefaultMemberService
import hu.bsstudio.bssweb.member.service.MemberService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MemberServiceConfig(val repository: MemberRepository) {

    @Bean
    fun memberService(): MemberService {
        return DefaultMemberService(repository)
    }
}
