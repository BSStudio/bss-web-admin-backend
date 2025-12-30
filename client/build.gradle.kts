plugins {
    id("hu.bsstudio.gradle.java-library-convention")
    id("hu.bsstudio.gradle.dependency-management")
    id("hu.bsstudio.gradle.kotlin-convention")
    id("hu.bsstudio.gradle.spotless-convention")
    id("hu.bsstudio.gradle.detekt-convention")
}

dependencies {
    api(project(":server:operation"))
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-web")
}
