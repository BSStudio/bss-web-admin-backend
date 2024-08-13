plugins {
    id("spotless-conventions")
    id("kotlin-conventions")
    id("spring-module-conventions")
    id("kotlin-testing-conventions")
}
dependencies {
    api(project(":server:common"))
    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation(libs.kotestAssertionsJson)
}
