plugins {
    id("java-conventions")
    id("io.spring.dependency-management")
}

repositories {
    mavenCentral()
}

dependencies {
    api(enforcedPlatform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    api(enforcedPlatform("org.springframework.shell:spring-shell-dependencies:3.3.2"))
    api(enforcedPlatform("org.springframework.cloud:spring-cloud-dependencies:2023.0.3"))
}
