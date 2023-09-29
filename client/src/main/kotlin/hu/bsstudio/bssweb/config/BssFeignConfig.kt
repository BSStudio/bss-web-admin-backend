package hu.bsstudio.bssweb.config

import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignAutoConfiguration

@EnableFeignClients("hu.bsstudio.bssweb.**.client", defaultConfiguration = [DefaultFeignConfig::class])
// todo remove below configuration, once Spring Cloud Feign is updated
@ImportAutoConfiguration(FeignAutoConfiguration::class, HttpMessageConvertersAutoConfiguration::class)
class BssFeignConfig
