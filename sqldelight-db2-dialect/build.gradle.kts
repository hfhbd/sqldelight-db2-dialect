plugins {
    kotlin("jvm")
    com.alecstrong.grammar.kit.composer
    org.jetbrains.kotlinx.`binary-compatibility-validator`
    app.cash.licensee
    repos
    publish
    exclude
}

val idea = "221.6008.13"

grammarKit {
    intellijRelease.set(idea)
}

dependencies {
   // grammar("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    implementation("app.softwork.sql.psi:core:0.5.0-SNAPSHOT")
    implementation("app.cash.sqldelight:dialect-api:2.0.0-SNAPSHOT") {
        exclude("com.alecstrong.sql.psi")
    }

    compileOnly("com.jetbrains.intellij.platform:ide-impl:$idea")

    testImplementation(kotlin("test"))
   // testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    testImplementation("com.jetbrains.intellij.platform:ide-impl:$idea")
    testImplementation("app.softwork.sql.psi:test-fixtures:0.5.0-SNAPSHOT")
}

kotlin {
    explicitApi()

    target.compilations.all {
        kotlinOptions {
            allWarningsAsErrors = true
            jvmTarget = "11"
        }
    }

    sourceSets {
        all {
            languageSettings.progressiveMode = true
        }
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

licensee {
    allow("Apache-2.0")
    allow("MIT")
}
