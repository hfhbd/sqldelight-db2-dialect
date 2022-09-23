import org.jetbrains.grammarkit.tasks.*

plugins {
    kotlin("jvm")
    com.alecstrong.grammar.kit.composer
    org.jetbrains.kotlinx.`binary-compatibility-validator`
    app.cash.licensee
    repos
    publish
    exclude
}

val idea = "213.6777.52"

grammarKit {
    intellijRelease.set(idea)
}

val grammar: Configuration by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
}

dependencies {
   // grammar("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    implementation("com.alecstrong.sql.psi:core:0.5.0-SNAPSHOT")
    implementation("app.cash.sqldelight:dialect-api:2.0.0-SNAPSHOT")

    compileOnly("com.jetbrains.intellij.platform:ide-impl:$idea")

    testImplementation(kotlin("test"))
   // testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    testImplementation("app.cash.sqldelight:dialect-api:2.0.0-SNAPSHOT")
    testImplementation("com.jetbrains.intellij.platform:ide-impl:$idea")
    testImplementation("com.alecstrong.sql.psi:test-fixtures:0.5.0-SNAPSHOT")
}

tasks {
    withType<GenerateParserTask>().configureEach {
       // classpath.from(grammar)
    }
}

kotlin {
    explicitApi()

    target.compilations.all {
        kotlinOptions {
            allWarningsAsErrors = true
            jvmTarget = "1.8"
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
