package hu.bsstudio.bssweb.videocrew.repository

import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface VideoCrewRepository : CrudRepository<VideoCrewEntity, VideoCrewEntity> {
    fun findAllByVideoId(videoId: String): List<VideoCrewEntity>
}
