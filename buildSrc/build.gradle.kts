plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation(libs.plugin.springBoot)
    implementation(libs.plugin.spotless)
    implementation(kotlin("allopen", "2.2.10"))
    implementation(kotlin("gradle-plugin", "2.2.10"))
    // required for kotlin plugin jpa
    implementation(kotlin("reflect", "2.2.10"))
    implementation(kotlin("stdlib-jdk8", "2.2.10"))
    implementation(kotlin("noarg", "2.2.10"))
}

gradlePlugin {
    plugins.create("dependency-management") {
        id = "hu.bsstudio.gradle.dependency-management"
        implementationClass = "hu.bsstudio.gradle.DependencyManagementPlugin"
    }
    plugins.create("integration-test-convention") {
        id = "hu.bsstudio.gradle.integration-test-convention"
        implementationClass = "hu.bsstudio.gradle.IntegrationTestConventionPlugin"
    }
    plugins.create("jacoco-convention") {
        id = "hu.bsstudio.gradle.jacoco-convention"
        implementationClass = "hu.bsstudio.gradle.JacocoConventionPlugin"
    }
    plugins.create("java-convention") {
        id = "hu.bsstudio.gradle.java-convention"
        implementationClass = "hu.bsstudio.gradle.JavaConventionPlugin"
    }
    plugins.create("java-library-convention") {
        id = "hu.bsstudio.gradle.java-library-convention"
        implementationClass = "hu.bsstudio.gradle.JavaLibraryConventionPlugin"
    }
    plugins.create("kotlin-convention") {
        id = "hu.bsstudio.gradle.kotlin-convention"
        implementationClass = "hu.bsstudio.gradle.KotlinConventionPlugin"
    }
    plugins.create("spotless-convention") {
        id = "hu.bsstudio.gradle.spotless-convention"
        implementationClass = "hu.bsstudio.gradle.DependencyManagementPlugin"
    }
    plugins.create("spring-app-convention") {
        id = "hu.bsstudio.gradle.spring-app-convention"
        implementationClass = "hu.bsstudio.gradle.SpringAppConventionPlugin"
    }
    plugins.create("testing-convention") {
        id = "hu.bsstudio.gradle.test-convention"
        implementationClass = "hu.bsstudio.gradle.TestConventionPlugin"
    }
}