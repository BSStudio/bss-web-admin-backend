plugins {
    id("hu.bsstudio.spotless-conventions")
    id("hu.bsstudio.kotlin-conventions")
    id("hu.bsstudio.spring-app-conventions")
}

dependencies {
    api(project(":server:web"))
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
}
