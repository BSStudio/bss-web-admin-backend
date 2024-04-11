package hu.bsstudio.bssweb.config

import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients("hu.bsstudio.bssweb.**.client", defaultConfiguration = [DefaultFeignConfig::class])
class BssFeignConfig constructor() {
    init {
        println("BssFeignConfig")
    }
}
