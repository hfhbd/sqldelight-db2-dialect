import org.gradle.api.publish.maven.*
import org.gradle.api.tasks.bundling.*
import org.gradle.kotlin.dsl.*
import java.util.*

plugins {
    id("maven-publish")
    id("signing")
}

val emptyJar by tasks.registering(Jar::class)

publishing {
    publications.register<MavenPublication>("mavenJava") {
        from(components["java"])
    }
    publications.configureEach {
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
    String(Base64.getDecoder().decode(it)).trim()
}?.let { key ->
    println("found key, config signing")
    signing {
        val signingPassword = System.getProperty("signing.password") ?: System.getenv("SIGNING_PASSWORD")
        useInMemoryPgpKeys(key, signingPassword)
        sign(publishing.publications)
    }
}
