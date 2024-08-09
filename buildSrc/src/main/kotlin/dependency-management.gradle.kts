plugins {
    id("java-conventions")
    id("io.spring.dependency-management")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(enforcedPlatform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    implementation(enforcedPlatform("org.springframework.cloud:spring-cloud-dependencies:2023.0.3"))
}
