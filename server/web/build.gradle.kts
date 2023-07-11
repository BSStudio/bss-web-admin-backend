plugins {
    id("spring-module-conventions")
    id("testing-conventions")
    id("ktlint-conventions")
}

dependencies {
    api(project(":server:api"))
    api(project(":server:service"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation(libs.springdocOpenapiStarterWebmvcUi)
}
