plugins {
    id("spring-module-conventions")
    id("testing-conventions")
    id("ktlint-conventions")
}

dependencies {
    api(project(":server:common"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("com.h2database:h2")
}
