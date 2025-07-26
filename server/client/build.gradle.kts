plugins {
    id("spotless-conventions")
    id("java-library-conventions")
    id("kotlin-conventions")
    id("spring-module-conventions")
    id("testing-conventions")
}

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation(libs.kotestAssertionsJson)
}
