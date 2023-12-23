package hu.bsstudio.bssweb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GraphQLApp

fun main(args: Array<String>) {
    runApplication<GraphQLApp>(*args)
}

 