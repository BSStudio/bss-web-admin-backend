plugins {
    id("kotlin-conventions")
    id("spring-app-conventions")
}

version = "1.0.0"

dependencies {
    api(project(":server:web"))
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
}
