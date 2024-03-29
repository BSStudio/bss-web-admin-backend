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
    flexmark {
        target("**/*.md")
        flexmark()
    }
    yaml {
        target("**/*.yml", "**/*.yaml")
        jackson()
    }
}
