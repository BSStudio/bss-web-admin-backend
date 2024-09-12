plugins {
    id("hu.bsstudio.spotless-conventions")
    id("hu.bsstudio.kotlin-conventions")
    id("hu.bsstudio.spring-module-conventions")
}

dependencies {
    api(project(":server:operation"))
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
}
