plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

extra["springBootVersion"] = "2.7.0"
extra["springDependencyManagement"] = "1.0.11.RELEASE"
extra["gradleKtlintVersion"] = "10.3.0"
extra["kotlinVersion"] = "1.6.10"

dependencies {
    implementation("org.springframework.boot:spring-boot-gradle-plugin:${property("springBootVersion")}")
    implementation("io.spring.gradle:dependency-management-plugin:${property("springDependencyManagement")}")
    implementation("org.jetbrains.kotlin:kotlin-allopen:${property("kotlinVersion")}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${property("kotlinVersion")}")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${property("kotlinVersion")}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${property("kotlinVersion")}")
    implementation("org.jetbrains.kotlin:kotlin-noarg:${property("kotlinVersion")}")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:${property("gradleKtlintVersion")}")
}
