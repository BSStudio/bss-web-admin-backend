package hu.bsstudio.bssweb.videocrew.repository

import hu.bsstudio.bssweb.videocrew.entity.VideoCrewEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface VideoCrewRepository : CrudRepository<VideoCrewEntity, VideoCrewEntity> {
    @Query("select distinct position from VideoCrewEntity")
    fun getPositions(): Set<String>
}
