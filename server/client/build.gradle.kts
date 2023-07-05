plugins {
    id("spring-module-conventions")
    id("testing-conventions")
    id("ktlint-conventions")
}

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
}
