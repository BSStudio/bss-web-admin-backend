package hu.bsstudio.bssweb

import hu.bsstudio.bssweb.config.EnversConfig
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.persistence.autoconfigure.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import

@EnableAutoConfiguration
@EntityScan("hu.bsstudio.bssweb.**.entity")
@ComponentScan("hu.bsstudio.bssweb.**.repository")
@Import(EnversConfig::class)
class DataConfig
