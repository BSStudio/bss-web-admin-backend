plugins {
    id("spring-module-conventions")
    id("ktlint-conventions")
}

dependencies {
    api(project(":server:operation"))
    api("org.springframework.cloud:spring-cloud-starter-openfeign")
}
