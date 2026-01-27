package hu.bsstudio.bssweb.config

import hu.bsstudio.bssweb.event.client.EventClient
import hu.bsstudio.bssweb.eventvideo.client.EventVideoClient
import hu.bsstudio.bssweb.label.client.LabelClient
import hu.bsstudio.bssweb.member.client.MemberClient
import hu.bsstudio.bssweb.metrics.client.MetricsClient
import hu.bsstudio.bssweb.video.client.VideoClient
import hu.bsstudio.bssweb.videocrew.client.VideoCrewClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import org.springframework.web.service.invoker.createClient

@Configuration
class BssWebAdminBackendClientConfig {
    @Bean
    fun factory(
        @Value($$"${bss.client.url}") baseUrl: String,
        @Value($$"${bss.client.token}") token: String,
    ): HttpServiceProxyFactory {
        val restClient =
            RestClient
                .builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer $token")
                .build()
        val adapter = RestClientAdapter.create(restClient)
        return HttpServiceProxyFactory.builderFor(adapter).build()
    }

    @Bean
    fun eventClient(factory: HttpServiceProxyFactory) = factory.createClient<EventClient>()

    @Bean
    fun eventVideoClient(factory: HttpServiceProxyFactory) = factory.createClient<EventVideoClient>()

    @Bean
    fun labelClient(factory: HttpServiceProxyFactory) = factory.createClient<LabelClient>()

    @Bean
    fun memberClient(factory: HttpServiceProxyFactory) = factory.createClient<MemberClient>()

    @Bean
    fun metricsClient(factory: HttpServiceProxyFactory) = factory.createClient<MetricsClient>()

    @Bean
    fun videoClient(factory: HttpServiceProxyFactory) = factory.createClient<VideoClient>()

    @Bean
    fun videoCrewClient(factory: HttpServiceProxyFactory) = factory.createClient<VideoCrewClient>()
}
