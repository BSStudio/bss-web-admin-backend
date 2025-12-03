package hu.bsstudio.bssweb

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.persistence.autoconfigure.EntityScan
import org.springframework.context.annotation.ComponentScan

@EnableAutoConfiguration
@EntityScan("hu.bsstudio.bssweb.**.entity")
@ComponentScan("hu.bsstudio.bssweb.**.repository")
class DataConfig
