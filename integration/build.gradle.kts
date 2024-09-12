plugins {
    id("hu.bsstudio.spotless-conventions")
    id("hu.bsstudio.kotlin-conventions")
    id("hu.bsstudio.integration-testing-conventions")
}

dependencies {
    api(project(":server:data"))
    api(project(":client"))
    intTestImplementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    intTestImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    intTestImplementation("org.springframework.boot:spring-boot-starter-json")
}
