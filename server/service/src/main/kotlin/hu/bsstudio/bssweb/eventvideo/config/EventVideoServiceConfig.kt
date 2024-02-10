package hu.bsstudio.bssweb.eventvideo.config

import hu.bsstudio.bssweb.event.service.EventService
import hu.bsstudio.bssweb.eventvideo.repository.EventVideoRepository
import hu.bsstudio.bssweb.eventvideo.service.DefaultEventVideoService
import hu.bsstudio.bssweb.eventvideo.service.EventVideoService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EventVideoServiceConfig(
    private val repository: EventVideoRepository,
    private val eventService: EventService,
) {
    @Bean
    fun eventVideoService(): EventVideoService {
        return DefaultEventVideoService(repository, eventService)
    }
}
