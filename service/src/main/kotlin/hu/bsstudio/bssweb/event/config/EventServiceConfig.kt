package hu.bsstudio.bssweb.event.config

import hu.bsstudio.bssweb.event.repository.EventRepository
import hu.bsstudio.bssweb.event.service.DefaultEventService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EventServiceConfig(val repository: EventRepository) {

    @Bean
    fun defaultEventService(): DefaultEventService {
        return DefaultEventService(repository)
    }
}
