plugins {
    id("hu.bsstudio.spotless-conventions")
    id("hu.bsstudio.kotlin-conventions")
    id("hu.bsstudio.spring-module-conventions")
    id("hu.bsstudio.kotlin-testing-conventions")
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
