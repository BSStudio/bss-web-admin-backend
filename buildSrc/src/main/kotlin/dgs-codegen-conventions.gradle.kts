plugins {
    id("com.netflix.dgs.codegen")
}

tasks.generateJava {
    schemaPaths.apply {
        clear()
        add("$projectDir/src/main/resources/graphql")
    }
    packageName = "hu.bsstudio.bssweb.codegen"
    generateClient = true
}
