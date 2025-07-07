plugins {
    id("testing-conventions")
}

dependencies {
    testImplementation("io.mockk:mockk-jvm:1.14.4")
    testImplementation("com.ninja-squad:springmockk:4.0.2")
    testImplementation("io.kotest:kotest-assertions-core-jvm:5.9.1")
}
