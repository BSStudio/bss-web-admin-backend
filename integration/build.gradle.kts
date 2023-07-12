plugins {
    id("spring-module-conventions")
    id("ktlint-conventions")
    id("testing-conventions")
}

dependencies {
    api(project(":server:data"))
    api(project(":client"))
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
}
