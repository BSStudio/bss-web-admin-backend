package hu.bsstudio.bssweb.config

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.ComponentScan

@EnableAutoConfiguration
@EntityScan("hu.bsstudio.bssweb.**.entity")
@ComponentScan("hu.bsstudio.bssweb.**.repository")
class DataConfig
