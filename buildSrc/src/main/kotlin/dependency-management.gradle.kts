plugins {
    id("io.spring.dependency-management")
}

repositories {
    mavenCentral()
}

dependencies {
    add("implementation", enforcedPlatform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    add("implementation", enforcedPlatform("org.springframework.cloud:spring-cloud-dependencies:2025.0.0"))
}
