package hu.bsstudio.bssweb

import hu.bsstudio.bssweb.config.BatchFetchConfig
import hu.bsstudio.bssweb.config.EnversConfig
import hu.bsstudio.bssweb.config.FlywayConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import

// @DataJpaTest leaves out the @Configuration classes it scans, so the ones the data module needs are imported here
@SpringBootApplication
@Import(BatchFetchConfig::class, EnversConfig::class, FlywayConfig::class)
class DataTestConfiguration
