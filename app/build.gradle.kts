plugins {
    id("spring-app-conventions")
    id("ktlint-conventions")
}

dependencies {
    api(project(":web"))
}
