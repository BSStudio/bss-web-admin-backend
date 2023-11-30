plugins {
    id("spring-app-conventions")
    id("ktlint-conventions")
}

dependencies {
    api(project(":server:web"))
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
}
