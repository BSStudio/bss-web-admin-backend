plugins {
    id("hu.bsstudio.gradle.java-convention")
    id("hu.bsstudio.gradle.dependency-management")
    id("hu.bsstudio.gradle.kotlin-convention")
    id("hu.bsstudio.gradle.spotless-convention")
    id("hu.bsstudio.gradle.integration-test-convention")
    id("hu.bsstudio.gradle.detekt-convention")
}

dependencies {
    integrationTestImplementation(project(":server:data"))
    integrationTestImplementation(project(":client"))
    integrationTestImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    integrationTestImplementation("org.springframework.boot:spring-boot-starter-json")
}
