plugins {
    id("hu.bsstudio.gradle.java-conventions")
    id("hu.bsstudio.gradle.dependency-management")
    id("hu.bsstudio.gradle.spotless-conventions")
    id("hu.bsstudio.gradle.kotlin-conventions")
    id("integration-testing-conventions")
}

dependencies {
    integrationTestImplementation(project(":server:data"))
    integrationTestImplementation(project(":client"))
    integrationTestImplementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    integrationTestImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    integrationTestImplementation("org.springframework.boot:spring-boot-starter-json")
}
