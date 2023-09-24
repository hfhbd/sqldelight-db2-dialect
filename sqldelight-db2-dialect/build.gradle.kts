plugins {
    kotlin("jvm")
    id("com.alecstrong.grammar.kit.composer")
    id("org.jetbrains.kotlinx.binary-compatibility-validator")
    id("app.cash.licensee")
    id("publish")
    id("exclude")
    id("repos")
    id("java-test-fixtures")
}

grammarKit {
    intellijRelease.set(libs.versions.idea)
}

dependencies {
    api(libs.sqldelight.dialect)
    compileOnly(libs.sqldelight.compilerEnv)

    testImplementation(kotlin("test"))
    testImplementation(libs.sqldelight.compilerEnv)
    testImplementation(testFixtures(libs.sql.psi))
}

kotlin {
    jvmToolchain(17)

    explicitApi()

    compilerOptions {
        allWarningsAsErrors.set(true)
        progressiveMode.set(true)
    }
}

licensee {
    allow("Apache-2.0")
}
