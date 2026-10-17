package hu.bsstudio.bssweb.config

import org.hibernate.cfg.AvailableSettings
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Sets Hibernate's `hibernate.default_batch_fetch_size` to [BATCH_SIZE]: collections and lazy associations load in
 * batches instead of one query each.
 *
 * **Default.** Unset, as Spring Boot leaves it, Hibernate batches nothing except where `@BatchSize` asks for it, so
 * every collection is loaded with its own query. A video's URLs are an eager element collection: loading every
 * video (about 2,300) ran one `select … from video_url where video_id = ?` per video, 2,274 statements in 0.76 s.
 * With batches of 100 it runs 24 statements in 0.16 s.
 *
 * **How a batch is chosen.** When the code reaches an uninitialized collection, Hibernate loads it together with up
 * to [BATCH_SIZE] - 1 others of the same kind (the same role, e.g. `urls`) that are waiting in the persistence
 * context. On PostgreSQL the keys go in one array parameter (`where video_id = any (?)`), so the SQL stays the same
 * whatever the batch size.
 *
 * **Impact.** The setting is global, not only for video URLs: a detailed video's labels and crew, an event's videos,
 * and lazy to-one associations batch too. The mappers read every collection they load, so here batching changes how
 * many queries load the data, not how much data is loaded.
 *
 * **Choosing the size** trades round trips against how much one query loads:
 * - Higher means fewer queries, but each brings more rows into memory at once, and where the code needs only a few
 *   of the collections, the others are loaded and kept in the persistence context for nothing. Very large values
 *   (thousands) also make each query a large result set.
 * - Lower means more round trips: with 10, the 2,300 videos would take about 230 queries.
 *
 * Hibernate's docs give no number, only "a sensible batch size >1". 100 is the largest page of the admin's lists, so
 * a page loads its collections in one query, and a batch stays small: a video has only a handful of URLs. Batching
 * makes N+1 selects cheaper but doesn't remove them; a join fetch, or a search in the database, would.
 *
 * See [Batch fetching and subselect fetching](https://docs.hibernate.org/orm/7.4/introduction/html_single/Hibernate_Introduction.html#batch-subselect-fetch)
 * and [`FetchSettings.DEFAULT_BATCH_FETCH_SIZE`](https://docs.hibernate.org/orm/7.4/javadocs/org/hibernate/cfg/FetchSettings.html#DEFAULT_BATCH_FETCH_SIZE).
 */
@Configuration
class BatchFetchConfig {
    @Bean
    fun batchFetching() = HibernatePropertiesCustomizer { it[AvailableSettings.DEFAULT_BATCH_FETCH_SIZE] = BATCH_SIZE }

    private companion object {
        private const val BATCH_SIZE = 100
    }
}
