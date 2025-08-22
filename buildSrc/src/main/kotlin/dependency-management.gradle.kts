plugins {
    id("io.spring.dependency-management")
}

repositories {
    mavenCentral()
}

dependencies {
    add("implementation", platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    add("implementation", platform("org.springframework.cloud:spring-cloud-dependencies:2025.0.0"))
}
