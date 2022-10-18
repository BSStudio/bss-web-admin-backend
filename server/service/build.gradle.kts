plugins {
    id("spring-module-conventions")
    id("testing-conventions")
    id("ktlint-conventions")
}

dependencies {
    api(project(":server:data"))
    api(project(":server:model"))
    implementation("org.springframework:spring-context")
    api("org.springframework.boot:spring-boot-starter-data-jpa")
}
