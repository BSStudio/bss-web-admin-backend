plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

extra["springBootVersion"] = "2.7.0"
extra["springDependencyManagement"] = "1.0.11.RELEASE"
extra["gradleKtlintVersion"] = "10.3.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-gradle-plugin:${property("springBootVersion")}")
    implementation("io.spring.gradle:dependency-management-plugin:${property("springDependencyManagement")}")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:${property("gradleKtlintVersion")}")
    implementation("org.jetbrains.kotlin:kotlin-allopen")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-noarg")
}
