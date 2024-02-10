package hu.bsstudio.bssweb.event.config

import hu.bsstudio.bssweb.event.mapper.EventMapper
import hu.bsstudio.bssweb.event.repository.DetailedEventRepository
import hu.bsstudio.bssweb.event.repository.SimpleEventRepository
import hu.bsstudio.bssweb.event.service.DefaultEventService
import hu.bsstudio.bssweb.video.mapper.VideoMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EventServiceConfig(
    private val repository: SimpleEventRepository,
    private val detailedRepository: DetailedEventRepository,
    private val videoMapper: VideoMapper,
) {
    @Bean
    fun defaultEventService(eventMapper: EventMapper): DefaultEventService {
        return DefaultEventService(repository, detailedRepository, eventMapper)
    }

    @Bean
    fun eventMapper() = EventMapper(videoMapper)
}
