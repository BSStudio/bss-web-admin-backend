plugins {
    id("spring-app-conventions")
    id("ktlint-conventions")
    id("testing-conventions")
}

dependencies {
    api(project(":server:web"))
    runtimeOnly("io.micrometer:micrometer-registry-influx")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
}
