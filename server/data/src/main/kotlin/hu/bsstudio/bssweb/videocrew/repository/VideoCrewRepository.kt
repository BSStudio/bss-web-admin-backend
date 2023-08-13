package hu.bsstudio.bssweb.videocrew.repository

import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntityId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface VideoCrewRepository : JpaRepository<VideoCrewEntity, VideoCrewEntityId> {
    @Query("select distinct id.position from VideoCrewEntity")
    fun getPositions(): Set<String>
}
