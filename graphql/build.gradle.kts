plugins {
    id("spring-app-conventions")
    id("testing-conventions")
    id("com.netflix.dgs.codegen") version "6.0.3"
}

dependencies {
    // graphql
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    // testing
    testImplementation("org.springframework.graphql:spring-graphql-test")
    testImplementation("org.springframework:spring-webflux")
}

tasks.generateJava {
    schemaPaths.add("$projectDir/src/main/resources/graphql")
    packageName = "hu.bsstudio.bssweb.codegen"
    generateClient = true
}
