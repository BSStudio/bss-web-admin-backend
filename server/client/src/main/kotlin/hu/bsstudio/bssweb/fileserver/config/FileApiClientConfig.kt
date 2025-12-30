package hu.bsstudio.bssweb.fileserver.config

import hu.bsstudio.bssweb.fileserver.client.FileApiClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import org.springframework.web.service.invoker.createClient
import java.time.Duration
import java.util.Base64

@Configuration
class FileApiClientConfig {
    @Bean
    fun factory(
        @Value($$"${bss.file-api.url}") baseUrl: String,
        @Value($$"${bss.file-api.username}") username: String,
        @Value($$"${bss.file-api.password}") password: String,
    ): HttpServiceProxyFactory {
        val auth = Base64.getEncoder().encodeToString("$username:$password".toByteArray())
        val requestFactory =
            SimpleClientHttpRequestFactory().apply {
                setConnectTimeout(Duration.ofSeconds(10))
                setReadTimeout(Duration.ofSeconds(30))
            }
        val restClient =
            RestClient
                .builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .defaultHeader("Authorization", "Basic $auth")
                .build()
        val adapter = RestClientAdapter.create(restClient)
        return HttpServiceProxyFactory.builderFor(adapter).build()
    }

    @Bean
    fun fileApiClient(factory: HttpServiceProxyFactory) = factory.createClient<FileApiClient>()
}
