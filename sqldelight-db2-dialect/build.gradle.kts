@file:OptIn(ExperimentalAbiValidation::class)

import org.jetbrains.kotlin.gradle.dsl.abi.ExperimentalAbiValidation

plugins {
    kotlin("jvm")
    id("com.alecstrong.grammar.kit.composer")
    id("app.cash.licensee")
    id("publish")
    id("exclude")
    id("repos")
}

grammarKit {
    intellijRelease.set(libs.versions.idea)
}

dependencies {
    api(libs.sqldelight.dialect)
    compileOnly(libs.sqldelight.compilerEnv)
}

testing.suites.named("test", JvmTestSuite::class) {
    useKotlinTest()
    useJUnit()

    dependencies {
        implementation(libs.sqldelight.compilerEnv)
        implementation(testFixtures(libs.sql.psi))
        implementation(libs.sql.psi.env)
    }
}

kotlin {
    jvmToolchain(17)

    explicitApi()

    compilerOptions {
        allWarningsAsErrors.set(true)
        progressiveMode.set(true)
    }
    abiValidation {
        enabled.set(true)
    }
}

tasks.check {
    dependsOn(tasks.checkLegacyAbi)
}

licensee {
    allow("Apache-2.0")
}
