plugins {
    kotlin("jvm") version "1.7.10" apply false
    id("com.alecstrong.grammar.kit.composer") version "0.1.10" apply false
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    id("app.cash.sqldelight") version "2.0.0-SNAPSHOT" apply false
}

allprojects {
    repositories {
        mavenCentral()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
    }

    group = "app.softwork"
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
