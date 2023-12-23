plugins {
    id("com.netflix.dgs.codegen")
}

tasks.generateJava {
    schemaPaths.add("$projectDir/src/main/resources/graphql")
    packageName = "hu.bsstudio.bssweb.codegen"
    generateClient = true
}
