plugins {
    id("spotless-conventions")
    id("java-conventions")
    id("kotlin-conventions")
    id("spring-app-conventions")
}

dependencies {
    implementation(project(":server:web"))
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
}
