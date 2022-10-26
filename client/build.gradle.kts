plugins {
    id("spring-module-conventions")
    id("testing-conventions")
    id("ktlint-conventions")
}

dependencies {
    api(project(":server:model"))
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
}
