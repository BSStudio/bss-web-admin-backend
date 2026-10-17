package hu.bsstudio.bssweb.ui.media

import org.slf4j.LoggerFactory
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestClientException
import java.time.Clock

private val logger = LoggerFactory.getLogger("hu.bsstudio.bssweb.ui.media.ImageUpload")

/**
 * Runs an upload to the file API and puts the outcome into the model for the image panel:
 * `uploadMessage` and a cache-busting `imageVersion` on success, `uploadError` when the file API refuses or is down.
 */
fun uploadImage(
    model: Model,
    successKey: String,
    clock: Clock = Clock.systemUTC(),
    upload: () -> Unit,
) {
    try {
        upload()
        model["uploadMessage"] = successKey
        model["imageVersion"] = "?v=${clock.millis()}"
    } catch (e: HttpStatusCodeException) {
        logger.warn("The file API refused an image: {}", e.statusCode, e)
        model["uploadError"] = "media.error.image"
    } catch (e: RestClientException) {
        logger.warn("The file API is not reachable", e)
        model["uploadError"] = "media.error.unavailable"
    }
}
