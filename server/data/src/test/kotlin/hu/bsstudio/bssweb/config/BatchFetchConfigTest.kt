package hu.bsstudio.bssweb.config

import io.kotest.matchers.maps.shouldContain
import org.hibernate.cfg.AvailableSettings
import org.junit.jupiter.api.Test

internal class BatchFetchConfigTest {
    @Test
    internal fun `should fetch collections in batches of 100`() {
        val properties = mutableMapOf<String, Any>()

        BatchFetchConfig().batchFetching().customize(properties)

        properties shouldContain (AvailableSettings.DEFAULT_BATCH_FETCH_SIZE to 100)
    }
}
