plugins {
    kotlin("jvm")
    com.alecstrong.grammar.kit.composer
    org.jetbrains.kotlinx.`binary-compatibility-validator`
    app.cash.licensee
    publish
    exclude
}

val idea = "221.6008.13" // EE

grammarKit {
    intellijRelease.set(idea)
}

dependencies {
    api("app.softwork.sqldelight:dialect-api:2.0.0-SNAPSHOT")
    compileOnly("com.jetbrains.intellij.platform:ide-impl:$idea")

    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    testImplementation("com.jetbrains.intellij.platform:ide-impl:$idea") {
        exclude(group = "org.jetbrains.kotlinx", module = "kotlinx-coroutines-core")
    }
    testImplementation("app.softwork.sql.psi:test-fixtures:0.5.0-SNAPSHOT")
}

kotlin {
    jvmToolchain(11)

    explicitApi()

    target.compilations.configureEach {
        kotlinOptions.allWarningsAsErrors = true
    }

    sourceSets.configureEach {
        languageSettings.progressiveMode = true
    }
}

licensee {
    allow("Apache-2.0")
}
