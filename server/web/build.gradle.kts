plugins {
    id("spotless-conventions")
    id("kotlin-conventions")
    id("spring-module-conventions")
    id("kotlin-testing-conventions")
}

dependencies {
    api(project(":server:operation"))
    api(project(":server:service"))
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation(libs.springdocOpenapiStarterWebmvcUi)
    implementation("org.springframework.data:spring-data-commons")
    testImplementation("org.springframework.security:spring-security-test")
}
