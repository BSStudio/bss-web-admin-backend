plugins {
    id("spring-module-conventions")
    id("testing-conventions")
    id("contract-testing-conventions")
    id("ktlint-conventions")
}

dependencies {
    api(project(":server:service"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation(libs.springdocOpenapiUi)
}
