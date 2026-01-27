package hu.bsstudio.bssweb.videocrew.operation

import hu.bsstudio.bssweb.video.model.DetailedVideo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.DeleteExchange
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PutExchange
import java.util.UUID

@HttpExchange("/api/v1/videoCrew")
interface VideoCrewOperation {
    @GetExchange("/position")
    fun getPositions(): ResponseEntity<List<String>>

    @PutExchange
    fun addPosition(
        @RequestParam videoId: UUID,
        @RequestParam position: String,
        @RequestParam memberId: UUID,
    ): ResponseEntity<DetailedVideo>

    @DeleteExchange
    fun removePosition(
        @RequestParam videoId: UUID,
        @RequestParam position: String,
        @RequestParam memberId: UUID,
    ): ResponseEntity<DetailedVideo>
}
