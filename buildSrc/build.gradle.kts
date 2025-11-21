plugins {
    `kotlin-dsl`
    alias(libs.plugins.spotless)
}

repositories {
    gradlePluginPortal()
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
    mapOf(
        "dependency-management" to "hu.bsstudio.gradle.DependencyManagementPlugin",
        "detekt-convention" to "hu.bsstudio.gradle.DetektConventionPlugin",
        "integration-test-convention" to "hu.bsstudio.gradle.IntegrationTestConventionPlugin",
        "jacoco-convention" to "hu.bsstudio.gradle.JacocoConventionPlugin",
        "java-convention" to "hu.bsstudio.gradle.JavaConventionPlugin",
        "java-library-convention" to "hu.bsstudio.gradle.JavaLibraryConventionPlugin",
        "kotlin-convention" to "hu.bsstudio.gradle.KotlinConventionPlugin",
        "spotless-convention" to "hu.bsstudio.gradle.SpotlessConventionPlugin",
        "spring-app-convention" to "hu.bsstudio.gradle.SpringAppConventionPlugin",
        "testing-convention" to "hu.bsstudio.gradle.TestConventionPlugin",
    ).forEach { (id, implementationClass) ->
        plugins.create(id) {
            this.id = "hu.bsstudio.gradle.$id"
            this.implementationClass = implementationClass
        }
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
