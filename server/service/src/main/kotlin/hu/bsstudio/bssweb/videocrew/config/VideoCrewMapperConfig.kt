package hu.bsstudio.bssweb.videocrew.config

import hu.bsstudio.bssweb.member.mapper.MemberMapper
import hu.bsstudio.bssweb.videocrew.mapper.VideoCrewMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class VideoCrewMapperConfig(private val memberMapper: MemberMapper) {
    @Bean
    fun videoCrewMapper() = VideoCrewMapper(memberMapper)
}
