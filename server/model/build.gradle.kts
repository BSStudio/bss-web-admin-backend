plugins {
    id("spring-module-conventions")
    id("testing-conventions")
    id("ktlint-conventions")
}
dependencies {
    api(project(":server:common"))
    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation("io.kotest:kotest-assertions-json:5.7.2")
}
