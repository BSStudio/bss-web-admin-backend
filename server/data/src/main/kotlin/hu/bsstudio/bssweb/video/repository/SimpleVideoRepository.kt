package hu.bsstudio.bssweb.video.repository

import hu.bsstudio.bssweb.video.entity.SimpleVideoEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SimpleVideoRepository : JpaRepository<SimpleVideoEntity, UUID>
