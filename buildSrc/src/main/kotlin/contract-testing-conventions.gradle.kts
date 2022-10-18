import gradle.kotlin.dsl.accessors._f9919a7247a31adace9503a7b3cb6fc8.testImplementation
import org.springframework.cloud.contract.verifier.config.TestMode

plugins {
    id("dependency-management")
    id("org.springframework.cloud.contract")
}

contracts {
    testMode.set(TestMode.MOCKMVC)
    baseClassForTests.set("hu.bsstudio.bssweb")
    generatedTestJavaSourcesDir.set(project.file("/generatedContracts"))
}

dependencies {
    testImplementation("org.springframework.cloud:spring-cloud-starter-contract-verifier")
}
