plugins {
    id("spring-app-conventions")
    id("testing-conventions")
    id("dgs-codegen-conventions")
}

dependencies {
    // graphql
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.netflix.graphql.dgs:graphql-dgs-extended-scalars")
    // testing
    testImplementation("org.springframework.graphql:spring-graphql-test")
    testImplementation("org.springframework:spring-webflux")
}
