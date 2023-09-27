package hu.bsstudio.bssweb

import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig


@SpringJUnitConfig(classes = [BssFeignConfig::class])
@TestPropertySource(
    properties = [
        "bss.client.url=http://localhost:9999",
        "bss.client.username=user",
        "bss.client.password=password"
    ]
)
open class IntegrationTest
