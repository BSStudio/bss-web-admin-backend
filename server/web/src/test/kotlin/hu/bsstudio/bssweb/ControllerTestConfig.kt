package hu.bsstudio.bssweb

import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration

@SpringBootApplication
@ImportAutoConfiguration(exclude = [SecurityAutoConfiguration::class])
class ControllerTestConfig
