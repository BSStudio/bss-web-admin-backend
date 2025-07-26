plugins {
    id("java-conventions")
    id("dependency-management")
    id("spotless-conventions")
    id("kotlin-conventions")
    id("integration-testing-conventions")
}

dependencies {
    integrationTestImplementation(project(":server:data"))
    integrationTestImplementation(project(":client"))
    integrationTestImplementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    integrationTestImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    integrationTestImplementation("org.springframework.boot:spring-boot-starter-json")
}
