package hu.bsstudio.bssweb.video.repository

import hu.bsstudio.bssweb.video.entity.DetailedVideoEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DetailedVideoRepository : CrudRepository<DetailedVideoEntity, UUID>
