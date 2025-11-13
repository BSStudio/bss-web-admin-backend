plugins {
    `kotlin-dsl`
    alias(libs.plugins.spotless)
}

repositories {
    gradlePluginPortal()
    // TODO: remove when detekt stable is in gradle plugin portal
    maven {
        // https://central.sonatype.org/publish/publish-portal-snapshots/#consuming-via-gradle
        url = 'https://central.sonatype.com/repository/maven-snapshots/'
        content {
            includeModule("io.gitlab.arturbosch.detekt", "detekt-gradle-plugin")
        }
    }
}

dependencies {
    implementation(libs.plugin.springBoot)
    implementation(libs.plugin.spotless)
    implementation(libs.plugin.detekt)
    implementation(libs.kotlin.allopen)
    implementation(libs.kotlin.gradle.plugin)
    // required for kotlin plugin jpa
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.kotlin.noarg)
}

gradlePlugin {
    plugins.create("dependency-management") {
        id = "hu.bsstudio.gradle.dependency-management"
        implementationClass = "hu.bsstudio.gradle.DependencyManagementPlugin"
    }
    plugins.create("detekt-convention") {
        id = "hu.bsstudio.gradle.detekt-convention"
        implementationClass = "hu.bsstudio.gradle.DetektConventionPlugin"
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
        implementationClass = "hu.bsstudio.gradle.SpotlessConventionPlugin"
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

spotless {
    kotlin {
        ktlint()
        toggleOffOn()
    }
    kotlinGradle {
        ktlint()
        toggleOffOn()
    }
}
