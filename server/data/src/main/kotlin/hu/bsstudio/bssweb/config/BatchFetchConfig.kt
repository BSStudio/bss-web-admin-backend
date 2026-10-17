package hu.bsstudio.bssweb.config

import org.hibernate.cfg.AvailableSettings
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Loads the collections of up to [BATCH_SIZE] entities with one query, instead of one query per entity.
 * A video's URLs are an element collection, so listing all videos used to run one
 * `select … from video_url` per video: 2,274 statements for a single search over 2,273 videos.
 * With batches it is 24. It applies to the whole app, so the API's lists get faster too.
 */
@Configuration
class BatchFetchConfig {
    @Bean
    fun batchFetching() = HibernatePropertiesCustomizer { it[AvailableSettings.DEFAULT_BATCH_FETCH_SIZE] = BATCH_SIZE }

    private companion object {
        private const val BATCH_SIZE = 100
    }
}
