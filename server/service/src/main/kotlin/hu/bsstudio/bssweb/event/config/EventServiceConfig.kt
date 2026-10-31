package hu.bsstudio.bssweb.event.config

import hu.bsstudio.bssweb.event.mapper.EventMapper
import hu.bsstudio.bssweb.event.repository.EventRepository
import hu.bsstudio.bssweb.event.service.DefaultEventService
import hu.bsstudio.bssweb.revision.mapper.RevisionMapper
import hu.bsstudio.bssweb.video.mapper.VideoMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EventServiceConfig(
    private val repository: EventRepository,
    private val videoMapper: VideoMapper,
) {
    @Bean
    fun defaultEventService(
        eventMapper: EventMapper,
        revisionMapper: RevisionMapper,
    ): DefaultEventService = DefaultEventService(repository, eventMapper, revisionMapper)

    @Bean
    fun eventMapper() = EventMapper(videoMapper)
}
