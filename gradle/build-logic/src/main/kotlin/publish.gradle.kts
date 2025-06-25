plugins {
    id("java")
    id("maven-publish")
    id("signing")
    id("io.github.hfhbd.mavencentral")
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications.register<MavenPublication>("maven") {
        from(components["java"])
    }
    publications.withType<MavenPublication>().configureEach {
        pom {
            name.set("app.softwork DB2 JDBC Driver and SqlDelight Dialect")
            description.set("A DB2 JDBC driver including support for SqlDelight")
            url.set("https://github.com/hfhbd/sqldelight-db2-dialect")
            licenses {
                license {
                    name.set("Apache-2.0")
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

signing {
    val signingKey = providers.gradleProperty("signingKey")
    if (signingKey.isPresent) {
        useInMemoryPgpKeys(signingKey.get(), providers.gradleProperty("signingPassword").get())
        sign(publishing.publications)
    }
}

// https://youtrack.jetbrains.com/issue/KT-46466
val signingTasks = tasks.withType<Sign>()
tasks.withType<AbstractPublishToMaven>().configureEach {
    dependsOn(signingTasks)
}
