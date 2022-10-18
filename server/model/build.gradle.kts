plugins {
    id("spring-module-conventions")
    id("ktlint-conventions")
}
dependencies {
    api(project(":server:common"))
}
