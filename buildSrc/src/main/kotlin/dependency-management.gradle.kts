plugins {
    id("java-conventions")
    id("io.spring.dependency-management")
}

repositories {
    mavenCentral()
}

dependencies {
    api(enforcedPlatform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    api(enforcedPlatform("org.springframework.cloud:spring-cloud-dependencies:2025.0.0"))
}
