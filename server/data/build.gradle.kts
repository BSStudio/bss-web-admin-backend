plugins {
    id("spring-module-conventions")
    id("testing-conventions")
    id("ktlint-conventions")
}

dependencies {
    api(project(":server:common"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    runtimeOnly("org.flywaydb:flyway-core")
    runtimeOnly("org.postgresql:postgresql")
    testRuntimeOnly("com.h2database:h2")
}
