plugins {
    id("com.diffplug.spotless")
}

spotless {
    kotlin {
        ktlint()
        toggleOffOn()
    }
    kotlinGradle{
        ktlint()
    }
}
