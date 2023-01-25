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
    api("app.softwork.sql.psi:core:0.5.0-db2-SNAPSHOT")
    implementation("app.cash.sqldelight:dialect-api:2.0.0-alpha05") {
        exclude("com.alecstrong.sql.psi")
    }

    compileOnly("com.jetbrains.intellij.platform:ide-impl:$idea")

    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
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
