plugins {
    id("spotless-conventions")
    id("kotlin-conventions")
    id("spring-module-conventions")
}

dependencies {
    api(project(":server:operation"))
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
}
