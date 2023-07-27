package hu.bsstudio.bssweb

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.TestPropertySource

@Configuration
@EnableAutoConfiguration
@EntityScan("hu.bsstudio.bssweb.**.entity")
@ComponentScan("hu.bsstudio.bssweb.**.repository")
class DataConfiguration
