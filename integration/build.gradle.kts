plugins {
    id("ktlint-conventions")
    id("integration-testing-conventions")
}

dependencies {
    api(project(":server:data"))
    api(project(":client"))
    intTestImplementation("org.springframework.boot:spring-boot-testcontainers")
    intTestImplementation("org.testcontainers:junit-jupiter")
    intTestImplementation("org.springframework.cloud:spring-cloud-starter-openfeign")
}
