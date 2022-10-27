plugins {
    kotlin("jvm") version "1.7.20" apply false
    id("com.alecstrong.grammar.kit.composer") version "0.1.10" apply false
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.12.1" apply false
    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
    id("app.cash.sqldelight") version "2.0.0-SNAPSHOT" apply false
    id("app.cash.licensee") version "1.5.0" apply false
}

allprojects {
    repositories {
        maven(url ="file://${rootDir.absolutePath}/localMaven")
        mavenCentral()
    }

    group = "app.softwork"
}

subprojects {
    if (this.name == "testing") {
        return@subprojects
    }

    plugins.apply("org.jetbrains.kotlinx.binary-compatibility-validator")

    plugins.apply("app.cash.licensee")
    configure<app.cash.licensee.LicenseeExtension> {
        allow("Apache-2.0")
        allow("MIT")
    }

    afterEvaluate {
        configure<org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension> {
            explicitApi()
            sourceSets {
                all {
                    languageSettings.progressiveMode = true
                }
            }
        }
    }

    plugins.apply("org.gradle.maven-publish")
    plugins.apply("org.gradle.signing")
    val emptyJar by tasks.creating(Jar::class) { }

    publishing {
        publications.all {
            this as MavenPublication
            artifact(emptyJar) {
                classifier = "javadoc"
            }
            pom {
                name.set("app.softwork DB2 JDBC Driver and SqlDelight Dialect")
                description.set("A DB2 JDBC driver including support for SqlDelight")
                url.set("https://github.com/hfhbd/sqldelight-db2-dialect")
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
                    connection.set("scm:git://github.com/hfhbd/sqldelight-db2-dialect.git")
                    developerConnection.set("scm:git://github.com/hfhbd/sqldelight-db2-dialect.git")
                    url.set("https://github.com/hfhbd/sqldelight-db2-dialect")
                }
            }
        }
    }

    (System.getProperty("signing.privateKey") ?: System.getenv("SIGNING_PRIVATE_KEY"))?.let {
        String(java.util.Base64.getDecoder().decode(it)).trim()
    }?.let { key ->
        println("found key, config signing")
        signing {
            val signingPassword = System.getProperty("signing.password") ?: System.getenv("SIGNING_PASSWORD")
            useInMemoryPgpKeys(key, signingPassword)
            sign(publishing.publications)
        }
    }
}

nexusPublishing {
    repositories {
        sonatype {
            username.set(System.getProperty("sonartype.apiKey") ?: System.getenv("SONARTYPE_APIKEY"))
            password.set(System.getProperty("sonartype.apiToken") ?: System.getenv("SONARTYPE_APITOKEN"))
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}
