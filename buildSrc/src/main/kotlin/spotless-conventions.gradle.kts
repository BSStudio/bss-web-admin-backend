plugins {
    id("com.diffplug.spotless")
}

spotless {
    kotlin {
        ktlint()
        toggleOffOn()
    }
    java {
        importOrder()
        removeUnusedImports()
        cleanthat()
        googleJavaFormat()
    }
    kotlinGradle{
        ktlint()
    }
}
