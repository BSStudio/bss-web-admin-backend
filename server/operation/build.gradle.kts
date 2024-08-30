plugins {
    id("hu.bsstudio.spotless-conventions")
    id("hu.bsstudio.kotlin-conventions")
    id("hu.bsstudio.spring-module-conventions")
    id("hu.bsstudio.kotlin-testing-conventions")
}

dependencies {
    api(project(":server:model"))
    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework:spring-web")
}
