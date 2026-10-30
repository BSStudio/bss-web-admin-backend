package hu.bsstudio.bssweb

import hu.bsstudio.bssweb.config.FlywayConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Import

// @DataJpaTest leaves out the @Configuration classes it scans, so the ones the data module needs are imported here
@SpringBootApplication
@Import(FlywayConfig::class)
class DataTestConfiguration
