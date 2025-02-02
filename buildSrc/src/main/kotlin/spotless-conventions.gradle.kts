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
    // TODO fix yaml with spotless update
    // yaml {
    //     target("**/*.yml", "**/*.yaml")
    //     jackson()
    //        .yamlFeature("WRITE_DOC_START_MARKER", false)
    //         .yamlFeature("MINIMIZE_QUOTES", true)
    // }
}
