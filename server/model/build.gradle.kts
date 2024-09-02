plugins {
    id("hu.bsstudio.kotlin-conventions")
    id("hu.bsstudio.spring-module-conventions")
    id("hu.bsstudio.kotlin-testing-conventions")
}
dependencies {
    api(project(":server:common"))
    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation(libs.kotestAssertionsJson)
}
