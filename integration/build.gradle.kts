plugins {
    id("ktlint-conventions")
    id("integration-testing-conventions")
}

tasks.jacocoTestCoverageVerification {
    enabled = false
}

tasks.jacocoTestReport {
    enabled = false
}

dependencies {
    api(project(":server:data"))
    api(project(":client"))
    intTestImplementation("org.springframework.boot:spring-boot-testcontainers")
    intTestImplementation("org.testcontainers:junit-jupiter")
    intTestImplementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    intTestImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "hamcrest")     // require developers to use assertJ
    }
}
