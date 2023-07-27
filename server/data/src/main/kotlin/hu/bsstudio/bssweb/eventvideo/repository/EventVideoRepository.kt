package hu.bsstudio.bssweb.eventvideo.repository

import hu.bsstudio.bssweb.eventvideo.entity.EventVideoEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EventVideoRepository : JpaRepository<EventVideoEntity, EventVideoEntity>
