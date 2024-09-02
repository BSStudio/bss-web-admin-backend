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
    implementation(kotlin("allopen", "2.0.10"))
    implementation(kotlin("gradle-plugin", "2.0.10"))
    // required for kotlin plugin jpa
    implementation(kotlin("reflect", "2.0.10"))
    implementation(kotlin("stdlib-jdk8", "2.0.10"))
    implementation(kotlin("noarg", "2.0.10"))
}

spotless {
    kotlin {
        ktlint()
    }
}

gradlePlugin {
    val dependencyManagement by plugins.creating {
        id = "hu.bsstudio.dependency-management"
        implementationClass = "hu.bsstudio.DependencyManagement"
    }
    val integrationTestingConventions by plugins.creating {
        id = "hu.bsstudio.integration-testing-conventions"
        implementationClass = "hu.bsstudio.IntegrationTestingConventions"
    }
    val javaConventions by plugins.creating {
        id = "hu.bsstudio.java-conventions"
        implementationClass = "hu.bsstudio.JavaConventions"
    }
    val kotlinConventions by plugins.creating {
        id = "hu.bsstudio.kotlin-conventions"
        implementationClass = "hu.bsstudio.KotlinConventions"
    }
    val kotlinTestingConventions by plugins.creating {
        id = "hu.bsstudio.kotlin-testing-conventions"
        implementationClass = "hu.bsstudio.KotlinTestingConventions"
    }
    val spotlessConventions by plugins.creating {
        id = "hu.bsstudio.spotless-conventions"
        implementationClass = "hu.bsstudio.SpotlessConventions"
    }
    val springAppConventions by plugins.creating {
        id = "hu.bsstudio.spring-app-conventions"
        implementationClass = "hu.bsstudio.SpringAppConventions"
    }
    val springModuleConventions by plugins.creating {
        id = "hu.bsstudio.spring-module-conventions"
        implementationClass = "hu.bsstudio.SpringModuleConventions"
    }
    val testingConventions by plugins.creating {
        id = "hu.bsstudio.testing-conventions"
        implementationClass = "hu.bsstudio.TestingConventions"
    }
}
