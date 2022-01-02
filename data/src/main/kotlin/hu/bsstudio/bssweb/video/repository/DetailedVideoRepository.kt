package hu.bsstudio.bssweb.video.repository

import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DetailedVideoRepository : CrudRepository<DetailedVideoEntity, String>
