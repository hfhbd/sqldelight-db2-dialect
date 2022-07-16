import java.util.*

plugins {
    kotlin("jvm")
    id("com.alecstrong.grammar.kit.composer")
    `maven-publish`
    signing
}

kotlin {
    explicitApi()
    sourceSets.all {
        languageSettings.progressiveMode = true
    }
    target.compilations.all {
        kotlinOptions.allWarningsAsErrors = true
    }
}

val idea = "211.7628.21"

grammarKit {
    intellijRelease.set(idea)
}

dependencies {
    compileOnly("app.cash.sqldelight:dialect-api:2.0.0-SNAPSHOT")
    compileOnly("com.jetbrains.intellij.platform:core-ui:$idea")
    compileOnly("com.jetbrains.intellij.platform:lang-impl:$idea")

    testImplementation(kotlin("test-junit"))
    testImplementation("app.cash.sqldelight:dialect-api:2.0.0-SNAPSHOT")
    testImplementation("com.jetbrains.intellij.java:java-psi:$idea")
    testImplementation("com.jetbrains.intellij.platform:core-impl:$idea")
    testImplementation("com.jetbrains.intellij.platform:core-ui:$idea")
    testImplementation("com.jetbrains.intellij.platform:lang-impl:$idea")
}

configurations.all {
    exclude(group = "com.jetbrains.rd")
    exclude(group = "com.github.jetbrains", module = "jetCheck")
    exclude(group = "org.roaringbitmap")
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications.create<MavenPublication>("mavenJava") {
        from(components["java"])
    }
    publications.all {
        this as MavenPublication
        pom {
            name.set("app.softwork Postgres Native Driver and SqlDelight Dialect")
            description.set("A Postgres native driver including support for SqlDelight")
            url.set("https://github.com/hfhbd/kotlinx-serialization-csv")
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    id.set("hfhbd")
                    name.set("Philip Wedemann")
                    email.set("mybztg+mavencentral@icloud.com")
                }
            }
            scm {
                connection.set("scm:git://github.com/hfhbd/SqlDelightNativePostgres.git")
                developerConnection.set("scm:git://github.com/hfhbd/SqlDelightNativePostgres.git")
                url.set("https://github.com/hfhbd/SqlDelightNativePostgres")
            }
        }
    }
}

(System.getProperty("signing.privateKey") ?: System.getenv("SIGNING_PRIVATE_KEY"))?.let {
    String(Base64.getDecoder().decode(it)).trim()
}?.let { key ->
    println("found key, config signing")
    signing {
        val signingPassword = System.getProperty("signing.password") ?: System.getenv("SIGNING_PASSWORD")
        useInMemoryPgpKeys(key, signingPassword)
        sign(publishing.publications)
    }
}
