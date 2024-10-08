plugins {
    id("spotless-conventions")
    id("kotlin-conventions")
    id("spring-module-conventions")
    id("kotlin-testing-conventions")
}

dependencies {
    api(project(":server:common"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.flywaydb:flyway-core")
    runtimeOnly("org.flywaydb:flyway-database-postgresql")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:junit-jupiter")
}
