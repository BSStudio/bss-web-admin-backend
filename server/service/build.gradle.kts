plugins {
    id("spring-module-conventions")
    id("testing-conventions")
    id("ktlint-conventions")
}

dependencies {
    api(project(":server:data"))
    implementation("org.springframework:spring-context")
    api("org.springframework.boot:spring-boot-starter-data-jpa")
}
