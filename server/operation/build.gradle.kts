plugins {
    id("spring-module-conventions")
    id("testing-conventions")
    id("ktlint-conventions")
}

dependencies {
    api(project(":server:model"))
    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework:spring-web")
}
