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
    plugins.create("java-convention") {
        id = "hu.bsstudio.gradle.java-conventions"
        implementationClass = "hu.bsstudio.gradle.JavaConventionPlugin"
    }
    plugins.create("java-library-conventions") {
        id = "hu.bsstudio.gradle.java-library-conventions"
        implementationClass = "hu.bsstudio.gradle.JavaLibraryConventionPlugin"
    }
    plugins.create("kotlin-conventions") {
        id = "hu.bsstudio.gradle.kotlin-conventions"
        implementationClass = "hu.bsstudio.gradle.KotlinConventionPlugin"
    }
    plugins.create("spotless-conventions") {
        id = "hu.bsstudio.gradle.spotless-conventions"
        implementationClass = "hu.bsstudio.gradle.DependencyManagementPlugin"
    }
    plugins.create("spring-app-conventions") {
        id = "hu.bsstudio.gradle.spring-app-conventions"
        implementationClass = "hu.bsstudio.gradle.SpringAppConventionPlugin"
    }
}