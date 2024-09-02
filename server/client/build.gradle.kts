plugins {
    id("hu.bsstudio.kotlin-conventions")
    id("hu.bsstudio.spring-module-conventions")
    id("hu.bsstudio.kotlin-testing-conventions")
}

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation(libs.kotestAssertionsJson)
}
