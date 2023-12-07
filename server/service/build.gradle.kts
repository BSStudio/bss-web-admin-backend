plugins {
    id("spring-module-conventions")
    id("testing-conventions")
}

dependencies {
    api(project(":server:data"))
    api(project(":server:model"))
    api(project(":server:client"))
    implementation("org.springframework:spring-context")
    implementation("org.springframework.data:spring-data-jpa")
}
