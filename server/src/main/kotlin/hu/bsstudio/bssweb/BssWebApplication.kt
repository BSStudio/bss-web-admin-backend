package hu.bsstudio.bssweb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["hu.bsstudio.bssweb"])
class BssWebApplication

fun main(args: Array<String>) {
    runApplication<BssWebApplication>(*args)
}
