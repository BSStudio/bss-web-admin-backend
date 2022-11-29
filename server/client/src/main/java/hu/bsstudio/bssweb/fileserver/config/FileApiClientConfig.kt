package hu.bsstudio.bssweb.fileserver.config

import hu.bsstudio.bssweb.fileserver.client.FileApiClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients(clients = [FileApiClient::class])
class FileApiClientConfig {
}
