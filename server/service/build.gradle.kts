plugins {
    id("hu.bsstudio.spotless-conventions")
    id("hu.bsstudio.kotlin-conventions")
    id("hu.bsstudio.spring-module-conventions")
    id("hu.bsstudio.kotlin-testing-conventions")
}

dependencies {
    api(project(":server:data"))
    api(project(":server:model"))
    api(project(":server:client"))
    implementation("org.springframework:spring-context")
    implementation("org.springframework.data:spring-data-jpa")
}
