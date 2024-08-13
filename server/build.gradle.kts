plugins {
    id("spotless-conventions")
    id("kotlin-conventions")
    id("spring-app-conventions")
}

dependencies {
    api(project(":server:web"))
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
}
